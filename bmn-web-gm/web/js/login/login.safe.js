/* user-passport/1.0.0 login.safity.js md5:d886da354111a3710d6c913e37c3ce9a */
define("login/login.safe.js",
    ["ui/dialog.js"],

    function(require) {
    require("ui/dialog.js");
    var d = '<div class="step">				<div class="mc">					<p>\u5b89\u5168\u63a7\u4ef6\u53ef\u63d0\u9ad8\u8d26\u6237\u5b89\u5168\u6027\uff0c\u52a0\u5bc6\u4fdd\u62a4\u60a8\u7684\u5bc6\u7801\u3002<a href="http://help.jd.com/help/question-61.html" target="_blank" class="ftx-05">\u4e86\u89e3\u66f4\u591a&gt;</a></p>					<div class="stepflex" id="#sflex03">						<dl class="first normal">							<dt class="s-num">1</dt>							<dd class="s-text">\u7acb\u5373\u4e0b\u8f7d<s></s><b></b></dd>						</dl>						<dl class="normal ">							<dt class="s-num">2</dt>							<dd class="s-text">\u624b\u52a8\u5b89\u88c5<s></s><b></b></dd>						</dl>						<dl class="normal last">							<dt class="s-num">3</dt>							<dd class="s-text">\u5237\u65b0\u9875\u9762\uff0c\u7ee7\u7eed\u8d2d\u7269<s></s><b></b></dd>						</dl>					</div>					<div class="ac mt20"><a href="http://static.360buyimg.com/securityctl/' + ("undefined" != typeof pgeditor && pgeditor.getLinkHtml()) + '" class="btn-3">\u7acb\u5373\u4e0b\u8f7d</a></div>				</div>			</div>';
    var e = '<div class="msg-warn"><b></b>\u516c\u5171\u573a\u6240\u4e0d\u5efa\u8bae\u81ea\u52a8\u767b\u5f55\uff0c\u4ee5\u9632\u8d26\u53f7\u4e22\u5931</div>';
    var f = '<div class="tip-box icon-box">							<span class="warn-icon m-icon"></span>							<div class="item-fore">								<h3 class="ftx-04">\u62b1\u6b49\uff0c\u672c\u63a7\u4ef6\u4ec5\u652f\u6301window\u7cfb\u7edf</h3>							</div>						</div>';
    var g = {
        dwidth: 372,
        dheight: 120
    };
    return "undefined" != typeof pgeditor && "nonsupport" == pgeditor.getLinkHtml() && (g.dwidth = 350, g.dheight = 60, d = f), $("#updata .up-two").click(function() {
        $("body").dialog({
            title: "\u63d0\u793a",
            width: g.dwidth,
            height: g.dheight,
            source: d,
            onReady: function() {
                $(".ui-dialog-close").click(function() {
                    $("#chkOpenCtrl").attr("checked", !1)
                })
            }
        })
    }), {
        createInfo: function() {
            $("body").dialog({
                title: "\u63d0\u793a",
                width: g.dwidth,
                height: g.dheight,
                source: d,
                onReady: function() {
                    $(".ui-dialog-close").click(function() {
                        $("#chkOpenCtrl").attr("checked", !1)
                    })
                }
            })
        },
        createAutoLoginInfo: function(a) {
            a ? ($(".msg-wrap .msg-error").hide(), $(".msg-wrap .msg-warn").replaceWith(e)) : ($(".msg-wrap .msg-warn").hide(), $(".msg-error").text().length && $(".msg-wrap .msg-error").show())
        }
    }
});