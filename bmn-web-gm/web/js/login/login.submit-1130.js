/* user-passport/1.0.0 login.submit-1130.js Date:2016-12-08 17:03:09 */
define(

    "login/login.submit-1130.js",

    [
        "login/login.status.js",
        "login/login.message.js",
        "login/login.pubSub.js",
        "login/login.countrycode-1130.js"
    ],

    function(require) {
    var c = require("login/login.status.js");
    var d = require("login/login.message.js");
    require("login/login.pubSub.js");
    require("login/login.countrycode-1130.js");
    var g = {
        elelist: {
            nameEle: $("#loginname"),
            pwdEle: $("#nloginpwd"),
            authEle: $("#authcode")
        },
        clear: function() {
            $("#o-authcode, #entry, .item-fore1").removeClass("item-error item-focus")
        },
        showError: function(a, b, c) {
            var d = '<div class="msg-warn"><b></b>' + a + "</div>";
            var e = '<div class="msg-error"><b></b>' + a + "</div>";
            $("#chkOpenCtrl").prop("checked");
            "warn" == b && ($(".msg-wrap .msg-error").hide(), $(".msg-wrap .msg-warn").replaceWith(d)), "error" == b && ($(".msg-wrap .msg-warn").hide(), $(".msg-wrap .msg-error").replaceWith(e)), g.clear();
            for (var h = 0; h < c.length; h++) $(c[h]).parent().addClass("item-error")
        },
        validateAll: function() {
            var nameValue = $.trim(this.elelist.nameEle.val());
            var pwdValue = $.trim(this.elelist.pwdEle.val());
            var f = false;//"undefined" != typeof pgeditor && pgeditor.pwdResult();
            var g = false;//"undefined" != typeof pgeditor && pgeditor.checkInstall();
            var h = $(".safe-chk").prop("checked");
            var i = $("#o-authcode").is(":visible");
            var j = $.trim($("#authcode").val());
            this.clear();
           /* b.length || e.length || h ?
                !b.length && g && !f.length && h ?
                void c.onError(d.NAME_PASSWORD_EMPTY,
                    [this.elelist.nameEle, this.elelist.pwdEle]) :

                    b.length ?
                        e.length || h ?
                            i && !j.length ?
                                void c.onError(d.AUTHCODE_EMPTY, this.elelist.authEle) :
                                (assemblyForm(), void loginSubmit(this.showError)) :
                            void c.onError(d.PASSWORD_EMPTY, this.elelist.pwdEle)
                :
                void c.onError(d.NAME_EMPTY, this.elelist.nameEle)

            :
            void c.onError(d.NAME_PASSWORD_EMPTY, [this.elelist.nameEle, this.elelist.pwdEle])*/

            if(nameValue.length || pwdValue.length || h ) {
                if(!nameValue.length && g && !f.length && h ) {
                    void c.onError(d.NAME_PASSWORD_EMPTY, [this.elelist.nameEle, this.elelist.pwdEle])
                } else {
                    if(nameValue.length) {
                        if(pwdValue.length || h ) {
                            if(i && !j.length) {
                                void c.onError(d.AUTHCODE_EMPTY, this.elelist.authEle)
                            } else {
                                (assemblyForm(), void loginSubmit(this.showError))
                            }
                        } else {
                            void c.onError(d.PASSWORD_EMPTY, this.elelist.pwdEle)
                        }
                    } else {
                        void c.onError(d.NAME_EMPTY, this.elelist.nameEle)
                    }
                }
            } else {
                void c.onError(d.NAME_PASSWORD_EMPTY, [this.elelist.nameEle, this.elelist.pwdEle])
            }

        },
        init: function() {
            var a = this;
            //登录btn
            $("#loginsubmit").bind("click", function() {
                a.validateAll.call(a)
            }),
            $("#loginname, #nloginpwd, #authcode").bind("keyup", function(a) {
                13 != a.keyCode || $(".sugcontent li").length || $("#loginsubmit").click()
            }),
            $(".login-form").delegate("#country_code_selector", "hover", function() {
                $("#focus").show()
            }, function() {
                $("#focus").hide()
            })
        }
    };
    return g.init()
});