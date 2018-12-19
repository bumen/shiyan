include "common.thrift"

namespace * com.baw.thrift.cs

const i16 CLIENT_VERSION = 3

struct Config {
    1: optional string id = ""
    2: optional i32 size = 1048576 // 1M
    3: optional i32 value = 10800 //3600*3
    4: optional i64 time
    5: optional byte p
    6: required bool ok
    6: required binary data
}
