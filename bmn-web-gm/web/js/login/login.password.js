/* user-passport/1.0.0 login.password.js md5:92333e364c93bcaf8f609a2bfd5061fb */
define(

    "login/login.password.js",

    [
        "util/util.reg.js",
        "login/login.status.js",
        "login/login.safity.js",
        "ui/tips.js",
        "login/login.pubSub.js"
    ],

    function(require) {
    var reg = require("util/util.reg.js");
    var status = require("login/login.status.js");
    var safe = require("login/login.safity.js");
    require("ui/tips.js");
    var pubSub = require("login/login.pubSub.js");
    //大小写提示
    var h = function(a) {
        a ? $(".capslock").show() : $(".capslock").hide()
    };
    var i = "";
    //判断大小写
    var j = function(a) {
        var b = a.keyCode || a.which;
        var c = a.shiftKey || 16 == b || false;
        var d = b >= 65 && 90 >= b && !c;   //大写
        var e = b >= 97 && 122 >= b && c;   //大写
        var f = b >= 65 && 90 >= b && c;    //小写
        var g = b >= 97 && 122 >= b && !c;  //小写
        return d || e ? (i = "on", h(true)) : f || g ? (i = "off", h(false)) : void 0
    };

    var k = function(a) {
        var b = a || window.event;
        20 == b.keyCode && "" != i && ("on" == i ? (i = "off", h(false)) : (i = "on", h(true)))
    };
    var l = {
        "password.focus": function() {
            pubSub.subscribe("password.focus", function() {
                status.onEvent({
                    ele: n.ele.parent(),
                    addClass: "item-focus",
                    callback: function() {
                        var a = n.ele.val();
                        var b = n.safeEleCheck.prop("checked");
                        reg.isEmpty(a) || b || n.ele.siblings(".clear-btn").show()
                    }
                })
            })
        },
        "password.blur": function() {
            pubSub.subscribe("password.blur", function() {
                status.onEvent({
                    ele: n.ele.parent(),
                    removeClass: "item-focus",
                    callback: function() {
                        var a = n.ele.val();
                        reg.isEmpty(a) && n.safeEle.next("txt").show()
                    }
                })
            })
        },
        "password.keyup": function() {
            pubSub.subscribe("password.keyup", function(a) {
                k(a), status.onEvent({
                    callback: function() {
                        var a = n.ele.val();
                        reg.isEmpty(a) ? n.ele.siblings(".clear-btn").hide() : n.ele.siblings(".clear-btn").show()
                    }
                })
            })
        },
        "password.keypress": function() {
            pubSub.subscribe("password.keypress", function(a) {
                j(a)
            })
        },
        "password.Tips": function() {
            $("#jdsafe").tips({
                width: 138,
                type: "hover",
                hasArrow: !1,
                hasClose: !1,
                autoResize: !1,
                diff: 0,
                tipsClass: "safetips",
                mouseleaveDelayTime: 100
            })
        },
        "password.clear": function() {
            pubSub.subscribe("password.clear", function() {
                h(false)
            })
        },
        "password.autologinInfo": function() {
            pubSub.subscribe("password.autologinInfo", function(a) {
                var b = a.prop("checked");
                safe.createAutoLoginInfo(b)
            })
        },
        "password.safeOcx.use": function() {
            pubSub.subscribe("password.safeOcx.use", function(a) {
                var b = a.prop("checked");
                var d = "undefined" != typeof pgeditor;
                var f = d && pgeditor.checkInstall();
                if (b ? ($("#entry").removeClass("item-error item-focus"), $(".capslock").hide()) : n.safeEle.hide(), f || b && safe.createInfo(), f && b && (n.safeEle.show(), n.safeEle.next("txt").hide(), n.ele.hide().val("")), f && !b) {
                    try {
                        d && pgeditor.pwdclear()
                    } catch (g) {}
                    n.safeEle.hide(), n.safeEle.next("txt").show(), n.ele.show();
                    var h = n.ele.val();
                    reg.isEmpty(h) ? n.ele.siblings(".clear-btn").hide() : n.ele.siblings(".clear-btn").show()
                }
            })
        },
        "passwordField.focus": function() {
            pubSub.subscribe("passwordField.focus", function() {
                n.ele.trigger("focus"),
                n.ocxEle.trigger("focus")
            })
        }
    };
    for (var m in l) l.hasOwnProperty(m) && l[m]();
    var n = {
        ele: $("#nloginpwd"),
        safeEle: $("#sloginpwd"),
        ocxEle: $("#_ocx_password"),
        safeEleCheck: $("#chkOpenCtrl"),
        autoLoginEle: $("#autoLogin"),
        otherUser: $(".more-slide"),
        init: function() {
            this.ele.bind("focus", function() {
                pubSub.publish("password.focus")
            }),
            this.ocxEle.live("focus", function() {
                pubSub.publish("password.focus")
            }),
            this.ele.bind("blur", function() {
                pubSub.publish("password.blur")
            }),
            this.ocxEle.live("blur", function() {
                pubSub.publish("password.blur")
            }),
            this.ele.bind("keyup", function(a) {
                pubSub.publish("password.keyup", a)
            }),
            this.ele.bind("keypress", function(a) {
                pubSub.publish("password.keypress", a)
            }),
            this.safeEleCheck.bind("click", function() {
                pubSub.publish("password.safeOcx.use", $(this))
            }),
            this.autoLoginEle.bind("click", function() {
                pubSub.publish("password.autologinInfo", $(this))
            }),
            this.otherUser.hover(function() {
                var a = $(this).attr("timer");
                var b = $(this);
                clearTimeout(a), $(this).attr("timer", setTimeout(function() {
                    b.addClass("more-hover")
                }, 500))
            }, function() {
                var a = $(this).attr("timer");
                clearTimeout(a), $(this).removeClass("more-hover")
            }),
            pubSub.publish("password.clear"),
            pubSub.publish("password.Tips")
        }
    };
    return n
});