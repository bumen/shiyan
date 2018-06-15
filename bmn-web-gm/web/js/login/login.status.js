/* user-passport/1.0.0 login.status.js Date:2016-10-27 18:10:22 */
define("login/login.status.js",
    ["login/login.pubSub.js"],
    function(require) {

    var warnInfo = '<div class="msg-warn"><b></b>{info}</div>';
    var errorInfo = '<div class="msg-error"><b></b>{info}</div>';
    var pubSub = require("login/login.pubSub.js");
    var status = {
        onEvent: function(a) {
            var b = {
                ele: "",
                addClass: "",
                removeClass: "",
                callback: function() {}
            };
            a = $.extend(b, a),
            a.addClass && a.ele.addClass(a.addClass),
            a.removeClass && a.ele.removeClass(a.removeClass),
            a.callback()
        },
        onError: function(a, b) {
            var errorMsg = errorInfo.replace(/{info}/g, a);
            $(".msg-wrap .msg-warn").hide(),
            $(".msg-wrap .msg-error").replaceWith(errorMsg),
            $.each(b, function() {
                $(this).parent().addClass("item-error")
            });
            var e = $(".item-error").eq(0).find("input");
            var f = e.val();

            "loginname" == e.attr("id") ?
                setTimeout(function() {
                    e.val("").focus().val(f)
                }, 100) :
                e.val("").focus().val(f)
        },
        onWarn: function() {
            var a = warnInfo.replace(/{info}/g, name);
            $(".msg-wrap .msg-error").hide(),
            $(".msg-wrap .msg-warn").replaceWith(a),
            ele.parent().addClass("item-warn")
        },
        onSuccess: function() {},
        onClear: function(a) {
            $(".form .item").delegate(".clear-btn", "click", function() {
                var b = $(this).siblings("input[placeholder]");
                $(this).hide(),
                b.val(""),
                setTimeout(function() {
                    b.focus(),
                    b.parent().get(0) == $(".item-fore1 .clear-btn").get(0) && e.publish("username.focus")
                }, 500),
                a && a(),
                $(this).siblings("txt").show()
            })
        }
    };
    return status
});