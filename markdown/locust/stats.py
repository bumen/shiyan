import hashlib
import time
from collections import namedtuple, OrderedDict
from copy import copy
from itertools import chain

import gevent
import six
from six.moves import xrange

from . import events
from .exception import StopLocust
from .log import console_logger

# STATS_NAME宽度设定
# /hello?keyWord=R201809131000, /hello?keyWord=R201809131001
STATS_NAME_WIDTH = 60

"""Default interval for how frequently the CSV file is written if this option
is configured."""
# CSV文件写入间隔设置(s)
CSV_STATS_INTERVAL_SEC = 2

"""Default interval for how frequently results are written to console."""
# 默认在console中打印频率间隔时间(s)
CONSOLE_STATS_INTERVAL_SEC = 2

"""
Default window size/resolution - in seconds - when calculating the current 
response time percentile
"""
# 页面刷新当前响应时间的间隔时间(s)
CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW = 10

# 缓存响应时间
CachedResponseTimes = namedtuple("CachedResponseTimes", ["response_times", "num_requests"])


class RequestStatsAdditionError(Exception):
    pass

## 从总请求中计算出，50%请求响应时间是多少秒，60%的请求响应时间是多少秒。90%的请求响应时间是多少秒
def calculate_response_time_percentile(response_times, num_requests, percent):
    """
    Get the response time that a certain number of percent of the requests
    finished within. Arguments:
    
    response_times: A StatsEntry.response_times dict
    num_requests: Number of request made (could be derived from response_times, 
                  but we save some CPU cycles by using the value which we already store)
    percent: The percentile we want to calculate. Specified in range: 0.0 - 1.0
    """
    # 获取已经完成某个比例的响应时间，例如50%, 90%等
    # response_times是一个StatsEntry.response_times变量，字典。
    # num_requests是已经发送的请求数目。
    # percent是我们指定的期望比例


    # 计算我们期望计算的那个请求的响应时间
    num_of_request = int((num_requests * percent))

    # 对响应时间进行排序，耗时从高到低一次查找，直到查找数目大于等于当前所需要的比例为值
    processed_count = 0
    for response_time in sorted(six.iterkeys(response_times), reverse=True):
        processed_count += response_times[response_time]
        if(num_requests - processed_count <= num_of_request):
            return response_time


def diff_response_time_dicts(latest, old):
    """
    Returns the delta between two {response_times:request_count} dicts.
    
    Used together with the response_times cache to get the response times for the 
    last X seconds, which in turn is used to calculate the current response time 
    percentiles.
    """
    new = {}
    for time in latest:
        diff = latest[time] - old.get(time, 0)
        # 非零则为True, 但diff只能是0或正数（增长了多少，因为lastest一直是累加的）
        if diff:
            new[time] = diff
    return new

## 被绑定的global_stats属性上。而global_stats被绑定到每一个LocustRunner上
class RequestStats(object):
    def __init__(self):
        # 每个不同请求一条记录
        self.entries = {}
        # 每个每个不同请求一条错误记录
        self.errors = {}
        # 所有请求总记录一条
        self.total = StatsEntry(self, "Total", None, use_response_times_cache=True)
        # 记录开始时间
        self.start_time = None

    # 返回总的请求次数：请求数是在locust接收到响应后累加。而不是请求时候就累加（应该是响应数）
    @property
    def num_requests(self):
        return self.total.num_requests

    #返回总失败次数
    @property
    def num_failures(self):
        return self.total.num_failures

    # 接收到的最后一个响应时间
    @property
    def last_request_timestamp(self):
        return self.total.last_request_timestamp

    # 记录响应
    def log_request(self, method, name, response_time, content_length):
        # 总记录
        self.total.log(response_time, content_length)
        # 单独记录
        self.get(name, method).log(response_time, content_length)

    # 记录错误
    def log_error(self, method, name, error):
        # 总记录
        self.total.log_error(error)
        # 单独记录
        self.get(name, method).log_error(error)

        # 单独记录错误
        # store error in errors dict
        key = StatsError.create_key(method, name, error)
        entry = self.errors.get(key)
        if not entry:
            entry = StatsError(method, name, error)
            self.errors[key] = entry
        entry.occured()

    # 记录时才创建，初始化StatsEntry的start_time, last_request_timestamp
    def get(self, name, method):
        """
        Retrieve a StatsEntry instance by name and method
        """
        entry = self.entries.get((name, method))
        if not entry:
            entry = StatsEntry(self, name, method)
            self.entries[(name, method)] = entry
        return entry
    
    def reset_all(self):
        """
        Go through all stats entries and reset them to zero
        """
        self.start_time = time.time()
        self.total.reset()
        for r in six.itervalues(self.entries):
            r.reset()
    
    def clear_all(self):
        """
        Remove all stats entries and errors
        """
        self.total = StatsEntry(self, "Total", None, use_response_times_cache=True)
        self.entries = {}
        self.errors = {}
        self.start_time = None
    
    def serialize_stats(self):
        return [self.entries[key].get_stripped_report() for key in six.iterkeys(self.entries) if not (self.entries[key].num_requests == 0 and self.entries[key].num_failures == 0)]
    
    def serialize_errors(self):
        return dict([(k, e.to_dict()) for k, e in six.iteritems(self.errors)])
        

class StatsEntry(object):
    """
    Represents a single stats entry (name and method)
    """
    
    name = None
    """ Name (URL) of this stats entry """
    
    method = None
    """ Method (GET, POST, PUT, etc.) """
    
    num_requests = None
    """ The number of requests made """
    
    num_failures = None
    """ Number of failed request """
    
    total_response_time = None
    """ Total sum of the response times """
    # 响应时间总和
    
    min_response_time = None
    """ Minimum response time """
    
    max_response_time = None
    """ Maximum response time """
    
    num_reqs_per_sec = None
    """ A {second => request_count} dict that holds the number of requests made per second """
    # 每秒多少个请求。其实是每秒多少个响应
    
    response_times = None
    """
    A {response_time => count} dict that holds the response time distribution of all
    the requests.
    
    The keys (the response time in ms) are rounded to store 1, 2, ... 9, 10, 20. .. 90, 
    100, 200 .. 900, 1000, 2000 ... 9000, in order to save memory.
    
    This dict is used to calculate the median and percentile response times.
    """
    # 相同响应时间有多少请求。即10秒响应时间的有20个请求
    
    use_response_times_cache = False
    """
    If set to True, the copy of the response_time dict will be stored in response_times_cache 
    every second, and kept for 20 seconds (by default, will be CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW + 10). 
    We can use this dict to calculate the *current*  median response time, as well as other response 
    time percentiles.
    """
    
    response_times_cache = None
    """
    If use_response_times_cache is set to True, this will be a {timestamp => CachedResponseTimes()} 
    OrderedDict that holds a copy of the response_times dict for each of the last 20 seconds.
    """
    
    total_content_length = None
    """ The sum of the content length of all the requests for this entry """
    
    start_time = None
    """ Time of the first request for this entry """
    # 第一个响应时间
    
    last_request_timestamp = None
    """ Time of the last request for this entry """
    # 最后一个响应时间
    
    def __init__(self, stats, name, method, use_response_times_cache=False):
        self.stats = stats
        self.name = name
        self.method = method
        self.use_response_times_cache = use_response_times_cache
        self.reset()
    
    def reset(self):
        self.start_time = time.time()
        self.num_requests = 0
        self.num_failures = 0
        self.total_response_time = 0
        self.response_times = {}
        self.min_response_time = None
        self.max_response_time = 0
        self.last_request_timestamp = int(time.time())
        self.num_reqs_per_sec = {}
        self.total_content_length = 0
        if self.use_response_times_cache:
            self.response_times_cache = OrderedDict()
            self._cache_response_times(int(time.time()))

    # 每次响应记录数据
    def log(self, response_time, content_length):
        # get the time
        # 精确到秒
        t = int(time.time())

        # 最快情况是每秒记录一次
        if self.use_response_times_cache and self.last_request_timestamp and t > self.last_request_timestamp:
            # see if we shall make a copy of the respone_times dict and store in the cache
            self._cache_response_times(t-1)

        # 一总有多少响应
        self.num_requests += 1
        # 记录每个时间戳有多少响应
        self._log_time_of_request(t)
        # 记录响应时间
        self._log_response_time(response_time)

        # increase total content-length
        self.total_content_length += content_length

    def _log_time_of_request(self, t):
        self.num_reqs_per_sec[t] = self.num_reqs_per_sec.setdefault(t, 0) + 1
        self.last_request_timestamp = t

    def _log_response_time(self, response_time):
        # 记录总响应时间
        self.total_response_time += response_time

        if self.min_response_time is None:
            self.min_response_time = response_time

        self.min_response_time = min(self.min_response_time, response_time)
        self.max_response_time = max(self.max_response_time, response_time)

        # to avoid to much data that has to be transfered to the master node when
        # running in distributed mode, we save the response time rounded in a dict
        # so that 147 becomes 150, 3432 becomes 3400 and 58760 becomes 59000
        # 避免分布式中master一下收到很多数据，则需要四舍五入
        if response_time < 100:
            rounded_response_time = response_time
        elif response_time < 1000:
            rounded_response_time = int(round(response_time, -1))
        elif response_time < 10000:
            rounded_response_time = int(round(response_time, -2))
        else:
            rounded_response_time = int(round(response_time, -3))

        # increase request count for the rounded key in response time dict
        self.response_times.setdefault(rounded_response_time, 0)
        self.response_times[rounded_response_time] += 1

    def log_error(self, error):
        self.num_failures += 1

    # 返回当前失败率：当前失败次数/成功+失败次数
    @property
    def fail_ratio(self):
        try:
            return float(self.num_failures) / (self.num_requests + self.num_failures)
        except ZeroDivisionError:
            if self.num_failures > 0:
                return 1.0
            else:
                return 0.0

    # 返回当前平均响应时间：总响应时间/总成功响应次数
    @property
    def avg_response_time(self):
        try:
            return float(self.total_response_time) / self.num_requests
        except ZeroDivisionError:
            return 0

    # 返回当前中间响应时间：即一半的请求在10s之下, 另一半在10s之上
    @property
    def median_response_time(self):
        if not self.response_times:
            return 0

        return median_from_dict(self.num_requests, self.response_times)

    # 返回当前平均响应数：计算最近10秒内的平均值。如果10秒内只有 3，6秒各返回5个，10请求。则 reqs=[5,10], avg=5+10/2
    @property
    def current_rps(self):
        if self.stats.last_request_timestamp is None:
            return 0
        slice_start_time = max(self.stats.last_request_timestamp - 12, int(self.stats.start_time or 0))

        reqs = [self.num_reqs_per_sec.get(t, 0) for t in range(slice_start_time, self.stats.last_request_timestamp-2)]
        return avg(reqs)

    # 返回当前总平均响应数：总请求成功数/（第一个响应时间到最后一个响应时间）的秒数
    @property
    def total_rps(self):
        if not self.stats.last_request_timestamp or not self.stats.start_time:
            return 0.0

        return self.num_requests / max(self.stats.last_request_timestamp - self.stats.start_time, 1)

    @property
    def avg_content_length(self):
        try:
            return self.total_content_length / self.num_requests
        except ZeroDivisionError:
            return 0
    
    def extend(self, other):
        """
        Extend the data from the current StatsEntry with the stats from another
        StatsEntry instance.
        将当前StateEntry与其他StateEntry合并
        """
        # 最后响应时间取最大的
        self.last_request_timestamp = max(self.last_request_timestamp, other.last_request_timestamp)
        # 开始响应时间取最小的
        self.start_time = min(self.start_time, other.start_time)

        # 成功请求数相加
        self.num_requests = self.num_requests + other.num_requests
        # 失败请求数相加
        self.num_failures = self.num_failures + other.num_failures
        # 总响应时间相加
        self.total_response_time = self.total_response_time + other.total_response_time
        self.max_response_time = max(self.max_response_time, other.max_response_time)
        self.min_response_time = min(self.min_response_time or 0, other.min_response_time or 0) or other.min_response_time
        self.total_content_length = self.total_content_length + other.total_content_length

        for key in other.response_times:
            self.response_times[key] = self.response_times.get(key, 0) + other.response_times[key]
        for key in other.num_reqs_per_sec:
            self.num_reqs_per_sec[key] = self.num_reqs_per_sec.get(key, 0) +  other.num_reqs_per_sec[key]

    # 将数据进行序列化打包为字典返回
    def serialize(self):
        return {
            "name": self.name,
            "method": self.method,
            "last_request_timestamp": self.last_request_timestamp,
            "start_time": self.start_time,
            "num_requests": self.num_requests,
            "num_failures": self.num_failures,
            "total_response_time": self.total_response_time,
            "max_response_time": self.max_response_time,
            "min_response_time": self.min_response_time,
            "total_content_length": self.total_content_length,
            "response_times": self.response_times,
            "num_reqs_per_sec": self.num_reqs_per_sec,
        }

    # 用于对数据进行解包，依次读取data的数据，并转换为一个Obj对象
    @classmethod
    def unserialize(cls, data):
        obj = cls(None, data["name"], data["method"])
        for key in [
            "last_request_timestamp",
            "start_time",
            "num_requests",
            "num_failures",
            "total_response_time",
            "max_response_time",
            "min_response_time",
            "total_content_length",
            "response_times",
            "num_reqs_per_sec",
        ]:
            setattr(obj, key, data[key])
        return obj
    
    def get_stripped_report(self):
        """
        Return the serialized version of this StatsEntry, and then clear the current stats.
        返回序列化后的数据并对记录进行重置
        """
        report = self.serialize()
        self.reset()
        return report

    # 返回的是对象的字符串形式
    def __str__(self):
        try:
            fail_percent = (self.num_failures/float(self.num_requests + self.num_failures))*100
        except ZeroDivisionError:
            fail_percent = 0
        
        return (" %-" + str(STATS_NAME_WIDTH) + "s %7d %12s %7d %7d %7d  | %7d %7.2f") % (
            (self.method and self.method + " " or "") + self.name,
            self.num_requests,
            "%d(%.2f%%)" % (self.num_failures, fail_percent),
            self.avg_response_time,
            self.min_response_time or 0,
            self.max_response_time,
            self.median_response_time or 0,
            self.current_rps or 0
        )

    # 获取不同百分比的请求下。占用多长响应时间。如：100个请求中，50%的请求响应时间是5s, 80%请求响应时间是10s
    def get_response_time_percentile(self, percent):
        """
        Get the response time that a certain number of percent of the requests
        finished within.
        
        Percent specified in range: 0.0 - 1.0
        """
        return calculate_response_time_percentile(self.response_times, self.num_requests, percent)

    # 用之前缓存的response_times与当前response_times比较，从而计算出最近相同响应时间的请求数变化比
    # 作用是计算20s内的一个变化。返回不同响应时间内请求占用比
    def get_current_response_time_percentile(self, percent):
        """
        Calculate the *current* response time for a certain percentile. We use a sliding 
        window of (approximately) the last 10 seconds (specified by CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW) 
        when calculating this.
        """
        if not self.use_response_times_cache:
            raise ValueError("StatsEntry.use_response_times_cache must be set to True if we should be able to calculate the _current_ response time percentile")
        # First, we want to determine which of the cached response_times dicts we should 
        # use to get response_times for approximately 10 seconds ago. 
        t = int(time.time())
        # Since we can't be sure that the cache contains an entry for every second. 
        # We'll construct a list of timestamps which we consider acceptable keys to be used 
        # when trying to fetch the cached response_times. We construct this list in such a way 
        # that it's ordered by preference by starting to add t-10, then t-11, t-9, t-12, t-8, 
        # and so on
        # 计算出时间，尽量可以匹配到缓存中的20条时间戳
        acceptable_timestamps = []
        for i in xrange(9):
            acceptable_timestamps.append(t-CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW-i)
            acceptable_timestamps.append(t-CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW+i)

        # 拿出20s内缓存中的一久的一条
        cached = None
        for ts in acceptable_timestamps:
            if ts in self.response_times_cache:
                cached = self.response_times_cache[ts]
                break
        
        if cached:
            # If we fond an acceptable cached response times, we'll calculate a new response 
            # times dict of the last 10 seconds (approximately) by diffing it with the current 
            # total response times. Then we'll use that to calculate a response time percentile 
            # for that timeframe
            # 先计算laster与old, response_times的差值：latest: {30=3; 40=3;500:4}, old: {30=2; 40=3;}, new={30=1; 500=4}
            # 再计算请求数差值。如：latest：10， old: 5, new: 10-5=5
            # 则这段时间内（20s内）增长了5个请求：分别响应时间：{30=1; 500=4}
            # 最后算出最近这段时间内new请求个数的响应占比
            return calculate_response_time_percentile(
                diff_response_time_dicts(self.response_times, cached.response_times), 
                self.num_requests - cached.num_requests, 
                percent,
            )

    # 返回百分比字符串
    def percentile(self, tpl=" %-" + str(STATS_NAME_WIDTH) + "s %8d %6d %6d %6d %6d %6d %6d %6d %6d %6d"):
        if not self.num_requests:
            raise ValueError("Can't calculate percentile on url with no successful requests")
        
        return tpl % (
            (self.method and self.method + " " or "") + self.name,
            self.num_requests,
            self.get_response_time_percentile(0.5),
            self.get_response_time_percentile(0.66),
            self.get_response_time_percentile(0.75),
            self.get_response_time_percentile(0.80),
            self.get_response_time_percentile(0.90),
            self.get_response_time_percentile(0.95),
            self.get_response_time_percentile(0.98),
            self.get_response_time_percentile(0.99),
            self.get_response_time_percentile(1.00)
        )

    # 由每次响应时触发执行
    def _cache_response_times(self, t):
        # 记录当前时间里，一共执行了多少请求，这些请求的响应时间分别是多少。
        self.response_times_cache[t] = CachedResponseTimes(
            response_times=copy(self.response_times),
            num_requests=self.num_requests,
        )
        
        
        # We'll use a cache size of CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW + 10 since - in the extreme case -
        # we might still use response times (from the cache) for t-CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW-10 
        # to calculate the current response time percentile, if we're missing cached values for the subsequent 
        # 20 seconds
        cache_size = CURRENT_RESPONSE_TIME_PERCENTILE_WINDOW + 10
        # 默认只记录最近的20次响应的。多余的删除
        if len(self.response_times_cache) > cache_size:
            # only keep the latest 20 response_times dicts
            for i in xrange(len(self.response_times_cache) - cache_size):
                self.response_times_cache.popitem(last=False)


class StatsError(object):
    def __init__(self, method, name, error, occurences=0):
        self.method = method
        self.name = name
        self.error = error
        self.occurences = occurences

    @classmethod
    def parse_error(cls, error):
        string_error = repr(error)
        target = "object at 0x"
        target_index = string_error.find(target)
        if target_index < 0:
            return string_error
        start = target_index + len(target) - 2
        end = string_error.find(">", start)
        if end < 0:
            return string_error
        hex_address = string_error[start:end]
        return string_error.replace(hex_address, "0x....")

    @classmethod
    def create_key(cls, method, name, error):
        key = "%s.%s.%r" % (method, name, StatsError.parse_error(error))
        return hashlib.md5(key.encode('utf-8')).hexdigest()

    def occured(self):
        self.occurences += 1

    def to_name(self):
        return "%s %s: %r" % (self.method, 
            self.name, repr(self.error))

    def to_dict(self):
        return {
            "method": self.method,
            "name": self.name,
            "error": StatsError.parse_error(self.error),
            "occurences": self.occurences
        }

    @classmethod
    def from_dict(cls, data):
        return cls(
            data["method"], 
            data["name"], 
            data["error"], 
            data["occurences"]
        )


def avg(values):
    return sum(values, 0.0) / max(len(values), 1)

def median_from_dict(total, count):
    """
    total is the number of requests made
    count is a dict {response_time: count}
    """
    pos = (total - 1) / 2
    for k in sorted(six.iterkeys(count)):
        if pos < count[k]:
            return k
        pos -= count[k]

# 创建global_stats
global_stats = RequestStats()
"""
A global instance for holding the statistics. Should be removed eventually.
"""

# 请求成功触发
def on_request_success(request_type, name, response_time, response_length, **kwargs):
    global_stats.log_request(request_type, name, response_time, response_length)

# 请求失败触发，请记录请求，再记录错误
def on_request_failure(request_type, name, response_time, exception, **kwargs):
    global_stats.log_request(request_type, name, response_time, 0)
    global_stats.log_error(request_type, name, exception)

# slave上报状态
def on_report_to_master(client_id, data):
    data["stats"] = global_stats.serialize_stats()
    data["stats_total"] = global_stats.total.get_stripped_report()
    data["errors"] =  global_stats.serialize_errors()
    global_stats.errors = {}
# master接收slave上报状态
def on_slave_report(client_id, data):
    for stats_data in data["stats"]:
        entry = StatsEntry.unserialize(stats_data)
        request_key = (entry.name, entry.method)
        if not request_key in global_stats.entries:
            global_stats.entries[request_key] = StatsEntry(global_stats, entry.name, entry.method)
        global_stats.entries[request_key].extend(entry)

    for error_key, error in six.iteritems(data["errors"]):
        if error_key not in global_stats.errors:
            global_stats.errors[error_key] = StatsError.from_dict(error)
        else:
            global_stats.errors[error_key].occurences += error["occurences"]
    
    # save the old last_request_timestamp, to see if we should store a new copy
    # of the response times in the response times cache
    old_last_request_timestamp = global_stats.total.last_request_timestamp
    # update the total StatsEntry
    global_stats.total.extend(StatsEntry.unserialize(data["stats_total"]))
    if global_stats.total.last_request_timestamp > old_last_request_timestamp:
        # If we've entered a new second, we'll cache the response times. Note that there 
        # might still be reports from other slave nodes - that contains requests for the same 
        # time periods - that hasn't been received/accounted for yet. This will cause the cache to 
        # lag behind a second or two, but since StatsEntry.current_response_time_percentile() 
        # (which is what the response times cache is used for) uses an approximation of the 
        # last 10 seconds anyway, it should be fine to ignore this. 
        global_stats.total._cache_response_times(global_stats.total.last_request_timestamp)
    
# 都通过事件触发
events.request_success += on_request_success
events.request_failure += on_request_failure
events.report_to_master += on_report_to_master
events.slave_report += on_slave_report

# 打印所有stats
def print_stats(stats):
    console_logger.info((" %-" + str(STATS_NAME_WIDTH) + "s %7s %12s %7s %7s %7s  | %7s %7s") % ('Name', '# reqs', '# fails', 'Avg', 'Min', 'Max', 'Median', 'req/s'))
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))
    total_rps = 0
    total_reqs = 0
    total_failures = 0
    for key in sorted(six.iterkeys(stats)):
        r = stats[key]
        total_rps += r.current_rps
        total_reqs += r.num_requests
        total_failures += r.num_failures
        console_logger.info(r)
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))

    try:
        fail_percent = (total_failures/float(total_reqs))*100
    except ZeroDivisionError:
        fail_percent = 0

    console_logger.info((" %-" + str(STATS_NAME_WIDTH) + "s %7d %12s %42.2f") % ('Total', total_reqs, "%d(%.2f%%)" % (total_failures, fail_percent), total_rps))
    console_logger.info("")

# 打印响应时间占比
def print_percentile_stats(stats):
    console_logger.info("Percentage of the requests completed within given times")
    console_logger.info((" %-" + str(STATS_NAME_WIDTH) + "s %8s %6s %6s %6s %6s %6s %6s %6s %6s %6s") % ('Name', '# reqs', '50%', '66%', '75%', '80%', '90%', '95%', '98%', '99%', '100%'))
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))
    for key in sorted(six.iterkeys(stats)):
        r = stats[key]
        if r.response_times:
            console_logger.info(r.percentile())
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))
    
    total_stats = global_stats.total
    if total_stats.response_times:
        console_logger.info(total_stats.percentile())
    console_logger.info("")

# 打印错误
def print_error_report():
    if not len(global_stats.errors):
        return
    console_logger.info("Error report")
    console_logger.info(" %-18s %-100s" % ("# occurrences", "Error"))
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))
    for error in six.itervalues(global_stats.errors):
        console_logger.info(" %-18i %-100s" % (error.occurences, error.to_name()))
    console_logger.info("-" * (80 + STATS_NAME_WIDTH))
    console_logger.info("")

# 每2秒打印一次错误
def stats_printer():
    from . import runners
    while True:
        print_stats(runners.locust_runner.request_stats)
        gevent.sleep(CONSOLE_STATS_INTERVAL_SEC)

# 每2秒写入cvs
def stats_writer(base_filepath):
    """Writes the csv files for the locust run."""
    while True:
        write_stat_csvs(base_filepath)
        gevent.sleep(CSV_STATS_INTERVAL_SEC)


def write_stat_csvs(base_filepath):
    """Writes the requests and distribution csvs."""
    with open(base_filepath + '_requests.csv', "w") as f:
        f.write(requests_csv())

    with open(base_filepath + '_distribution.csv', 'w') as f:
        f.write(distribution_csv())


def sort_stats(stats):
    return [stats[key] for key in sorted(six.iterkeys(stats))]


def requests_csv():
    from . import runners

    """Returns the contents of the 'requests' tab as CSV."""
    rows = [
        ",".join([
            '"Method"',
            '"Name"',
            '"# requests"',
            '"# failures"',
            '"Median response time"',
            '"Average response time"',
            '"Min response time"',
            '"Max response time"',
            '"Average Content Size"',
            '"Requests/s"',
        ])
    ]

    for s in chain(sort_stats(runners.locust_runner.request_stats), [runners.locust_runner.stats.total]):
        rows.append('"%s","%s",%i,%i,%i,%i,%i,%i,%i,%.2f' % (
            s.method,
            s.name,
            s.num_requests,
            s.num_failures,
            s.median_response_time,
            s.avg_response_time,
            s.min_response_time or 0,
            s.max_response_time,
            s.avg_content_length,
            s.total_rps,
        ))
    return "\n".join(rows)

def distribution_csv():
    """Returns the contents of the 'distribution' tab as CSV."""
    from . import runners

    rows = [",".join((
        '"Name"',
        '"# requests"',
        '"50%"',
        '"66%"',
        '"75%"',
        '"80%"',
        '"90%"',
        '"95%"',
        '"98%"',
        '"99%"',
        '"100%"',
    ))]
    for s in chain(sort_stats(runners.locust_runner.request_stats), [runners.locust_runner.stats.total]):
        if s.num_requests:
            rows.append(s.percentile(tpl='"%s",%i,%i,%i,%i,%i,%i,%i,%i,%i,%i'))
        else:
            rows.append('"%s",0,"N/A","N/A","N/A","N/A","N/A","N/A","N/A","N/A","N/A"' % s.name)

    return "\n".join(rows)
