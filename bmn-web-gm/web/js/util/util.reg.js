/* user-passport/1.0.0 login.reg.js md5:581a79f6d8b2322edbbc4cb6fbeac56f */
define("util/util.reg.js", [], function() {
    var c = {
        isEmpty: function(a) {
            return new RegExp("^[\\s+]*$").test(a)
        },
        isNum: function(a) {
            return new RegExp("^([+-]?)\\d*\\.?\\d+$").test(a)
        },
        isChinese: function(a) {
            return new RegExp("^[\\u4e00-\\u9fa5]+$").test(a)
        },
        isDate: function(a) {
            return new RegExp("^\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}$").test(a)
        },
        isEmail: function(a) {
            return new RegExp("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$").test(a)
        },
        isLetter: function(a) {
            return new RegExp("^[A-Za-z]+$").test(a)
        },
        isPhone: function(a) {
            return new RegExp("^0?(13|15|18|14)[0-9]{9}$").test(a)
        },
        isUrl: function(a) {
            return new RegExp("^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$").test(a)
        },
        isUserName: function(a) {
            return new RegExp("/^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+$/").test(a)
        },
        formatMillisToDate: function(millis) {
            var date = new Date(millis);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? ('0' + m) : m;
            var d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            var h = date.getHours();
            h=h < 10 ? ('0' + h) : h;
            var minute = date.getMinutes();
            minute = minute < 10 ? ('0' + minute) : minute;
            var second=date.getSeconds();
            second=second < 10 ? ('0' + second) : second;
            return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
        }
    };
    return c
});