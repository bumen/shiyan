/* user-passport/1.0.0 login.authcode.js md5:c2da40b26074ce7aafab1967e7d2813c */
define(
    "login/login.authcode.js",

    [
        "util/util.reg.js",
        "login/login.status.js",
        "login/login.pubSub.js"
    ],
    function(require) {
    var reg = require("util/util.reg.js");
    var status = require("login/login.status.js");
    var pubSub = require("login/login.pubSub.js");
    var f = {
        "authcode.focus": function() {
            pubSub.subscribe("authcode.focus", function() {
                status.onEvent({
                    ele: h.ele.parent(),
                    addClass: "item-focus",
                    callback: function() {
                        var a = h.ele.val();
                        reg.isEmpty(a) || h.ele.siblings(".clear-btn").show()
                    }
                })
            })
        },
        "authcode.blur": function() {
            pubSub.subscribe("authcode.blur", function() {
                status.onEvent({
                    ele: h.ele.parent(),
                    removeClass: "item-focus"
                })
            })
        },
        "authcode.keyup": function() {
            pubSub.subscribe("authcode.keyup", function() {
                status.onEvent({
                    callback: function() {
                        var a = h.ele.val();
                        reg.isEmpty(a) ? h.ele.siblings(".clear-btn").hide() : h.ele.siblings(".clear-btn").show()
                    }
                })
            })
        },
        "authcode.disable": function() {
            pubSub.subscribe("authcode.disable", function() {
                h.ele.css("ime-mode", "disabled")
            })
        }
    };
    for (var g in f) f[g]();
    var h = {
        ele: $("#authcode"),
        init: function() {
            this.ele.bind("focus", function() {
                pubSub.publish("authcode.focus")
            }),
            this.ele.bind("blur", function() {
                pubSub.publish("authcode.blur")
            }),
            this.ele.bind("keyup", function() {
                pubSub.publish("authcode.keyup")
            }),
            pubSub.publish("authcode.disable")
        }
    };
    return h
});