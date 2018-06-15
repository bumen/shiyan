/* user-passport/1.0.0 login.qrcode.2016.js Date:2017-03-23 14:08:06 */
define(

    "login/login.qrcode.2016.js",

    [],

    function(require, a) {

    $(".login-box");
    var d = $(".qrcode-login");
    var e = $(".qrcode-target");
    var f = $(".qrcode-error-2016 .refresh-btn");
    $(".login-box").next(".coagent");
    var h = $(".qrcode-img img");
    var i = $(".qrcode-main");
    var j = $(".qrcode-tips");
    var k = false;
    var l = false;
    var m = '<div class="qrcode-error02 hide" id="J-qrcoderror"  style="display: none;">' +
                '<a href="#none">' +
        '            <span class="error-icon"></span>' +
                    '<div class="txt">网络开小差咯' +
                        '<span class="ml10">刷新二维码</span>' +
                    '</div>' +
                '</a>' +
            '</div>';
    //请求二维码
    var n = function(a, b) {
        var c = "qrcode/show?appid=" + (a || 133) + "&size=" + (b || 147) + "&t=" + (new Date).getTime();
        var d = $("#J-qrcoderror");
        h[0].src = c,
        h[0].onload = function() {
            d.hide();
            j.show();
            h.show();
            k = true;
        },
        h[0].onerror = function() {
            j.hide();
            h.hide();
            d.show();
            k = false;
        }
    };
    //判断二维码失效
    var p = function(a) {
        var b = readCookie("qvp_smdl");
        var c = {
            appid: a || 133
        };
        b && (c.token = b);
        $.ajax({
            url: "qrcode/check",
            //dataType: "jsonp",
            //data: c,
            //scriptCharset: "utf-8",
            success: function(ret) {
                if (ret) {
                    var a = JSON.parse(ret);
                    var b = a.code;
                    var c = a.ticket;
                    switch (l = false, b) {
                        case 200:
                            s();
                            q(false, false, true);
                            c && r(c);
                            l = true;
                            break;
                        case 201:
                            q(true, false);
                            $(".qrcode-main").show();
                            break;
                        case 202:
                            s(),
                            q(false, false, true);
                            $(".err-cont").text("登录失败");
                            l = true;
                            break;
                        case 203:
                            i.show();
                            q(false, true);
                            k = false;
                            $(".err-cont").text("二维码已失效");
                            break;
                        case 204:
                            i.show();
                            q(false, true);
                            k = false;
                            $(".err-cont").text("二维码已失效");
                            break;
                        case 205:
                            i.show();
                            q(false, true);
                            k = false;
                            $(".err-cont").text("登录失败")
                    }
                }
            }
        })
    };
    var q = function(a, b, c) {
        $(".qrcode-desc").toggle(a),
        $(".qrcode-error-2016").toggle(b),
        $(".qrcode-succ").toggle(c || false),
        $(".qrcode-panel").toggle(!c)
    };
    //扫码
    var r = function(a) {
        $.getJSON("/uc/qrCodeTicketValidation", {
            t: a
        }, function(a) {
            if (a) {
                var b = a.returnCode;
                var c = a.url;
                switch (b) {
                    case 0:
                        window.location.assign(c);
                        break;
                    case 58:
                    case 59:
                        q(false, true),
                        $(".err-cont").text("二维码已失效");
                        break;
                    case 60:
                    case 70:
                        q(false, true),
                        $(".err-cont").text("登录失败")
                }
            }
        })
    };
    var s = function() {
        var a =
            '<div class="qrcode-succ">' +
                '<div class="tip-box icon-box">' +
                    '<span class="succ-icon"></span>' +
                    '<div class="item-fore al">' +
                        '<h3>扫描成功!</h3>' +
                        '<div class="ftx-03">请勿刷新本页面，按手机提示操作！</div>' +
                    '</div>' +
                '</div>' +
            '</div>';
        $(".qrcode-succ").size() ? $(".qrcode-succ").show() : i.after(a), i.hide()
    };
    var t = function() {
        $(".qrcode-img").stop(!0).animate({
            left: "0px"
        }, 300, function() {
            $(".qrcode-help").stop(!0).show()
        }), $(".qrcode-error-2016").stop(!0).animate({
            left: "-64px"
        }, 300, function() {
            $(".qrcode-help").stop(!0).show()
        }), setTimeout(function() {
            $(".qrcode-help").stop(!0).hide(), $(".qrcode-img").stop(!0).animate({
                left: "64px"
            }, 300), $(".qrcode-error-2016").stop(!0).animate({
                left: "0px"
            }, 300)
        }, 4e3)
    };
    a.init = function() {
        //扫码显示中，且没有错误，则显示动画
        $(".qrcode-error-2016").is(":hidden") && $(".login-tab-l a").hasClass("checked") && t(),
        //绑定“扫码登录”与“账户登录”两个选项的点击事件
        $(".login-tab").click(function() {
                //"login-tab-l"表示点击的“扫码登录”
                $(this).hasClass("login-tab-l") ?
                    (
                        //-1 != window.location.href.indexOf("/popupLogin2013") && $(".no-reg-buy").hide(), //没用
                        $(".login-box").hide(),     //账户登录隐藏
                        $(".qrcode-login").show().css("visibility", "visible"), //扫码登录显示
                        $("#country_code_layer").hide(),        //目前未用
                        t(),    //放动画
                        l ? (s(), q(false, false, true)) : (n(), q(true, false))
                     )
                    :
                    (
                        //-1 != window.location.href.indexOf("/popupLogin2013") && $(".no-reg-buy").show(), //没用
                        $(".login-box").show().css("visibility", "visible"),    //账户登录显示
                        $("#entry").css("visibility", "visible"),
                        //IE8兼容
                        $.browser.msie && /msie 8\.0/i.test(window.navigator.userAgent.toLowerCase()) &&
                        (
                            $(".item").each(function() {
                                if (0 != $(this).css("margin-bottom")) {
                                    if ($(this).next().is(".marginBottom"))
                                        return;
                                    $(this).after('<div class="marginBottom" style="height:' + $(this).css("margin-bottom") + '"></div>'),
                                    $(this).css("margin-bottom", "0px")
                                }
                             }),
                            $(".msg-wrap").after('<div class="marginBottom" style="height:' + $(".msg-wrap").css("margin-bottom") + '"></div>'),
                            $(".msg-wrap").css("margin-bottom", "0px")
                        ),
                        $(".qrcode-login").hide()   //扫码登录隐藏
                    ),
                $(".login-tab a").removeClass("checked"),   //登录选项卡设置选中状态
                $(this).find("a").addClass("checked")
            }),

        $("#J-qrcoderror").live("click", function() {
            f.click()
        }),
        //插入上面m
        !$("#J-qrcoderror").size() && h.after(m);

        //启动时，默认选则哪种登录选项（默认：扫码）
        "f" == $("#loginType").val() ? $(".login-tab-r").trigger("click") : $(".login-tab-l").trigger("click"),
        //绑定扫码登录下的“刷新”事件
        f.click(function() {
            clearTimeout(this.timer),
            this.timer = setTimeout(function() {
                n(),        //重新请求二维码
                i.show(),   //二维码显示
                q(true, false);   //显示状态
            }, 500)
        }),
        $("#J-back").live("click", function() {
            e.click()
        }),
        //鼠标放到二维码效果(会左右动）
        $(".qrcode-main").hover(
            function() {
                $(".qrcode-error-2016").is(":visible") && 64 == $(".qrcode-img").position().left || ($(".qrcode-img").stop(!0).animate({
                    left: "0px"
                }, 300, function() {
                    $(".qrcode-help").stop(!0).show()
                }), $(".qrcode-error-2016").stop(!0).animate({
                    left: "-64px"
                }, 300))
            },
            function() {
                $(".qrcode-error-2016").is(":visible") && 64 == $(".qrcode-img").position().left || ($(".qrcode-help").stop(!0).hide(), $(".qrcode-img").stop(!0).animate({
                    left: "64px"
                }, 300), $(".qrcode-error-2016").stop(!0).animate({
                    left: "0px"
                }, 300))
            }
        );
        setInterval(function() {
            k && d.is(":visible") && p();    //检测二维码是否失效
        }, 3e3);
    }
});