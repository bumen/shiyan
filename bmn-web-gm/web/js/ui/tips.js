/* jdf-1.0.0/ tips.js Date:2015-09-15 18:39:13 */ ! function(a) {
    a.ui.define("tips", {
        options: {
            hasCssLink: !0,
            baseVersion: "1.0.0",
            cssLinkVersion: "1.1.0",
            tipsClass: "ui-tips",
            type: "hover",
            align: ["top", "left"],
            autoWindow: !0,
            autoResize: !0,
            hasArrow: !0,
            hasClose: !0,
            closeName: null,
            currClass: "",
            zIndex: 100,
            onClose: null,
            diff: 8,
            width: null,
            source: "",
            mouseenterHideAll: !0,
            mouseleaveDelayTime: 500,
            callback: function() {}
        },
        init: function() {
            var b = this;
            var c = this.options;
            if (this.create(), a(document).click(function(c) {
                    var d = c.target;
                    a(d).is(b.el) || a(d).parents().is(b.el) || a(d).is(".ui-tips") || a(d).parents(".ui-tips").is(".ui-tips") || !a(".ui-tips:visible").length || b.hide()
                }), "fixed" == c.type && this.setInit(this.el), "hover" == c.type) {
                var d = function() {
                    clearTimeout(b.mouseleave)
                };
                var e = function() {
                    b.mouseleave = setTimeout(function() {
                        b.hide()
                    }, c.mouseleaveDelayTime)
                };
                this.el.bind("mouseenter", function() {
                    b.hideAll(), d(), b.setInit(a(this))
                }), this.el.bind("mouseleave", function() {
                    e()
                }), this.tips.bind("mouseenter", function() {
                    d()
                }), this.tips.bind("mouseleave", function() {
                    e()
                })
            }
            "click" == c.type && this.el.bind("mouseup", function() {
                b.setInit(a(this))
            })
        },
        show: function() {
            a(this.tips).appendTo("fixed" == this.options.type ? this.el : "body"), this.el.addClass(this.options.currClass)
        },
        hide: function() {
            a(this.tips).detach(), this.el.removeClass(this.options.currClass), this.options.onClose && this.options.onClose.call(null, this.el, this.tips)
        },
        hideAll: function() {
            this.options.mouseenterHideAll && a(".ui-tips").detach()
        },
        setInit: function(a) {
            var b = this.options.source || a.attr("data-tips");
            b && (this.show(), this.tips.find(".ui-tips-main").html(b), this.update(), this.bind(), this.options.callback.call(this.tips.find(".ui-tips-main"), this.el, this.tips))
        },
        bind: function() {
            var b = this;
            var c = this.options;
            b.tips.find(".ui-tips-close").bind("click", function() {
                b.hide()
            }), b.tips.find(b.options.closeName).bind("click", function() {
                b.hide()
            }), c.autoResize && (a(window).bind("resize", function() {
                b.update()
            }), a(window).scroll(function() {
                b.update()
            }))
        },
        create: function() {
            var b = this.options;
            var c = "";
            var d = b.zIndex + this.guid;
            b.hasArrow && (c = '<span class="ui-tips-arrow" style="z-index:' + d + '"></span>');
            var e = "";
            this.options.hasClose && (e = '<span class="ui-tips-close" style="z-index:' + d + '">x</span>');
            var f = ['<div class="' + b.tipsClass + ' ui-tips-top" style="z-index:' + d + '" id="uis-tips-' + this.guid + '">', '	<div class="ui-tips-main">', "	</div>", c, e, "</div>"].join("");
            this.tips = a(f), this.bindTag = !0
        },
        update: function() {
            var a = this.options;
            this.align(a.align[0], a.align[1])
        },
        align: function(b, c) {
            var d = this.options;
            var e = d.diff;
            var f = this.el.offset();
            var g = this.el.outerWidth();
            var h = this.el.outerHeight();
            var i = f.top;
            var j = f.left;
            var k = this.tips;
            var l = "ui-tips ui-tips-top";
            var m = k.outerWidth();
            var n = k.outerHeight(); - 1 != a.inArray(b, ["top", "bottom", "left", "right"]) && (l = "ui-tips ui-tips-" + b), -1 != a.inArray(c, ["top", "bottom", "left", "right", "center"]) && (("top" == b || "bottom" == b) && (j + m > a.page.clientWidth() && d.autoWindow && (c = "right"), m > j && d.autoWindow && (c = "left"), i - n - e < a(document).scrollTop() && d.autoWindow && (b = "top", l = "ui-tips ui-tips-" + b), i + n + h + e > a(document).scrollTop() + a.page.clientHeight() && d.autoWindow && (b = "bottom", l = "ui-tips ui-tips-" + b), l += " ui-tips-x-" + c, "top" == b && (i = i + h + e), "bottom" == b && (i = i - n - e), "left" == c && (j = j), "center" == c && (j = j - m / 2 + g / 2), "right" == c && (j = j - m + g)), ("left" == b || "right" == b) && (j + m + g + e > a.page.clientWidth() && d.autoWindow && (b = "right", l = "ui-tips ui-tips-" + b), m > j && d.autoWindow && (b = "left", l = "ui-tips ui-tips-" + b), i - n + h + e < a(document).scrollTop() && d.autoWindow && (c = "top"), i + n - e > a(document).scrollTop() + a.page.clientHeight() && d.autoWindow && (c = "bottom"), l += " ui-tips-y-" + c, "left" == b && (j = j + g + 2 * e), "right" == b && (j = j - m - e), "top" == c && (i = i - h + e), "center" == c && (i = i - n / 2 + h), "bottom" == c && (i = i - n + h / 2 + 2 * e))), "ui-tips" != this.options.tipsClass && (l += " " + this.options.tipsClass), k.addClass(l), k.css({
                top: i,
                left: j
            }), d.width && k.css({
                width: d.width
            })
        }
    })
}(jQuery);