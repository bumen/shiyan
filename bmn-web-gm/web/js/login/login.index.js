/* user-passport/1.0.0 login.index.2016-1130.js Date:2016-12-08 17:03:10 */
!function(){
    var configData = {
        paths: {
            "login" : "login",      //简化目录
            "ui" : "ui"
        },
        map:[
            ["seajs/user/passport/1.0.0/js", "login"]
        ],
        debug: 1

    };
    seajs.config(configData);

}();

seajs.use([
        "ui/ui.js",
        "login/login.username.js",
        "login/login.password.js",
        "login/login.authcode.js",
        "login/login.submit-1130.js",
        "ui/placeholder",
        "login/login.qrcode.2016.js"
    ],
    function (a0, a, b, c, d, e, f) {
    a.init(),
        b.init(),
        c.init(),
        f.init(),
        $("input[placeholder]").bind("keydown", function () {
            $(this).prev("txt").hide()
        }),
        $("txt").css("font-family", "Arial, Verdana, \u5b8b\u4f53"),
        document.msCapsLockWarningOff = !0,
        $("body").delegate("input:checkbox, a", "click", function () {
            $(this).css("outline", "rgb(109, 109, 109) none 0px")
        }),
        $("body").delegate("input:checkbox, a", "focusin", function () {
            $.browser.webkit ? $(this).css("outline", "rgb(77, 144, 254) auto 5px") : $(this).css("outline", "#aaa 1px dotted")
        }),
        $("body").delegate("input:checkbox, a", "focusout", function () {
            $(this).css("outline", "rgb(109, 109, 109) none 0px")
        }),
        $("body").delegate("#nloginpwd", "focus", function () {
            $(this).siblings("txt").hide()
        });
    var g = "placeholder" in document.createElement("input");
    g || $("input[placeholder]").placeholder()
});

var createCookie = function (a, b, c, d) {
    var d = d ? d : "/";
    if (c) {
        var e = new Date;
        e.setTime(e.getTime() + 24 * c * 60 * 60 * 1e3);
        var f = "; expires=" + e.toGMTString()
    } else var f = "";
    document.cookie = a + "=" + b + f + "; path=" + d
};
var readCookie = function (a) {
    var b = a + "=";
    var c = document.cookie.split(";");
    for (var d = 0; d < c.length; d++) {
        var e = c[d];
        for (;
            " " == e.charAt(0);) e = e.substring(1, e.length);
        if (0 == e.indexOf(b)) return e.substring(b.length, e.length)
    }
    return null
};
function getCookie(a) {
    var b = document.cookie.match(new RegExp("(^| )" + a + "=([^;]*)(;|$)"));
    return null != b ? unescape(b[2]) : null
}