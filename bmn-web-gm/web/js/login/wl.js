/*Mon May 15 2017 08:21:45*/


function setCookieMills(a, b, c) {
    var d = new Date;
    d.setTime(d.getTime() + c);
    var e = window.document.domain.indexOf("360buy") >= 0 ? ".360buy.com" : ".jd.com";
    document.cookie = a + "=" + escape(b) + ";expires=" + d.toGMTString() + ";path=/;domain=" + e
}
function getCookie(a) {
    var b = document.cookie.match(new RegExp("(^| )" + a + "=([^;]*)(;|$)"));
    return null != b ? unescape(b[2]) : null
}
function deleteCookie(a) {
    var b = getCookie(a);
    null != b && setCookieMills(a, "", -1)
}
function seClick(a, b, c) {
    var d = "seWids" + a,
        e = getCookie(d);
    if (null != e) {
            var f = e.toString().indexOf(c);
            f < 0 && (e = e + "," + c)
        } else e = c;
    setCookieMills(d, e, 864e5),
    privateLogWLJS(2, 2, b, c)
}
function appendJSONCookie(cookieName, key, wid, Mills) {
    var ns = eval("(" + getCookie(cookieName) + ")");
    null != ns && "" != ns || (ns = {}),
    null == ns[key] && (ns[key] = "");
    var pos = ns[key].indexOf(wid);
    pos < 0 && (ns[key] = ns[key] + "," + wid),
    setCookieMills(cookieName, $.toJSON(ns), Mills)
}
function reBook(a, b, c) {
    var d = "_rtbook",
        e = b.toString().split("#")[0];
    appendJSONCookie(d, a, e, 864e5),
    privateLogWLJS(3, a, e, c)
}
function fe(a, b, c) {
    privateLogWLJS("f", a, b, c)
}
function reClick2012(a, b, c) {
    var d = "reHome2012",
        e = b.toString().split("#")[0];
    appendJSONCookie(d, a, e, 864e5),
    privateLogWLJS(3, a, e, c)
}
function reClickCube(a, b) {
    var c = "_rdCube";
    appendJSONCookie(c, "p" + a, b, 864e5)
}
function mark(a, b) {
    privateLogWLJS(1, b, a)
}
function isMeta(a) {
    if (a.metaKey || a.altKey || a.ctrlKey || a.shiftKey) return !0;
    var b = a.which,
        c = a.button;
    return b || void 0 === c ? 2 === b || 2 === c : 1 & !c && 2 & !c && 4 & c
}
function HashMap() {
    this.values = {}
}
function RecommendTrans(recName, tag, logtype) {
    for (var cookieNames = recName.split(","), i = 0; i < cookieNames.length; i++) {
        var recCookies = eval("(" + getCookie(cookieNames[i]) + ")");
        for (var k in recCookies)"" != recCookies[k] && ("cai2012" == k ? loginfo(recCookies[k], k.toString(), "R", logtype) : loginfo(recCookies[k], k.toString(), tag, logtype))
    }
}
function simpleMold(a, b, c, d, e) {
    for (var f = 0; f < a.length; f++) {
        var g = getCookie(c + a[f]);
        null != g && "" != g && loginfo(g, a[f], b, d, e)
    }
}
function complexMold(cookieArrary, tag, prefix, logtype, flag) {
    for (var i = 0; i < cookieArrary.length; i++) {
        var items = eval("(" + getCookie(prefix + cookieArrary[i]) + ")");
        if (null != items) for (var k in items)"" != items[k] && loginfo(items[k], k.toString(), tag, logtype, flag)
    }
}
function loginfo(a, b, c, d, e) {
    for (var f = a.split(","), g = SucInfo_OrderId, h = SucInfo_OrderType, i = SucInfo_OrderDetail, j = 0; j < f.length; j++) if (f[j].length > 0) {
        var k = f[j].toString().split("#")[0];
        SucInfoMethod.Contains(k) && (e ? (privateLogWLJS(d, c, b.concat(".o"), g, h, i, k + ":" + SucInfoMethod.GetSkuNum(k)), privateLogWLJS("4", "R" + b.concat(".o"), g, h, i, k, SucInfoMethod.GetSkuNum(k))) : privateLogWLJS(d, c + b, g, h, i, k, SucInfoMethod.GetSkuNum(k)))
    }
}
function isChecked() {
    return SucInfo_OrderId = window.SucInfo_OrderId || JA.util.getParameter(document.location.href, "suc_orderid") || void 0,
    SucInfo_OrderType = window.SucInfo_OrderType || JA.util.getParameter(document.location.href, "suc_ordertype") || void 0,
    SucInfo_OrderDetail = window.SucInfo_OrderDetail || decodeURIComponent(JA.util.getParameter(document.location.href, "suc_sku")) || void 0,
    SucInfo_OrderId && SucInfo_OrderDetail
}
function funLoad() {
    var a = getCookie("pin");
    null != a && a.length > 0 && setCookieMills("rpin", a, 2592e5)
}
function Clublog() {
    var a = this.location.pathname.toLowerCase(),
        b = this.location.hostname.toLowerCase();
    a.indexOf("/cart.html", 0) >= 0 || a.indexOf("shoppingcart", 0) >= 0 ? privateLogWLJS("R2&Page", "Show") : a.indexOf("user_home", 0) >= 0 ? privateLogWLJS("R3&Page", "Show") : a.indexOf("initcart.html", 0) >= 0 || a.indexOf("addtocart.html", 0) >= 0 || a.indexOf("initcart.aspx", 0) >= 0 ? privateLogWLJS("R4R5&Page", "Show") : a.indexOf("normal/list.action", 0) >= 0 || a.indexOf("orderlist.aspx", 0) >= 0 ? privateLogWLJS("DDR&Page", "Show") : "home.360buy.com" == b && "/" == a && privateLogWLJS("R3&Page", "Show")
}
function getHistory() {
    var a = decodeURIComponent(escape(getCookie("pin"))),
        b = getCookie("_ghis"),
        c = window.document.location.host.toLowerCase().indexOf("360buy.com") >= 0 ? "360buy" : "jd";
    if (null == b && null != a) {
            var d = "//gh." + c + ".com/BuyHistory.aspx?mid=" + encodeURIComponent(a);
            $.ajax({
                url: d,
                type: "GET",
                dataType: "jsonp",
                success: function (a) {
                    var b = a.SSkus,
                        c = a.UserInsterest;
                    b.toString().length > 0 && setCookieMills("_ghis", b.toString().substring(0, 51)),
                    c.toString().length > 0 && setCookieMills("_ghit", c)
                }
            })
        }
}
function privateLogWLJS(a, b) {
    var c = Array.prototype.slice.call(arguments);
    c = c && c.slice(2),
    JA && JA.tracker.ngloader("other.000000", {
        t1: a,
        t2: b,
        p0: encodeURIComponent(JA.util.join(c))
    })
}
function log(a, b) {
    var c = Array.prototype.slice.call(arguments),
        d = c;
    c = c && c.slice(2),
    JA && JA.tracker.ngloader("other.000000", {
            t1: a,
            t2: b,
            p0: encodeURIComponent(JA.util.join(c))
        }),
    JA && JA.tracker.isCanPrey() && JA && JA.tracker.ngloader("other.000000", {
            t1: "logservice_check",
            t2: "wl",
            p0: encodeURIComponent(JA.util.join(d))
        })
}
function logJSON(a, b, c) {
    return !!JA && void JA.tracker.ngloaderJSON("other.000000", {
        t1: a,
        t2: b,
        p0: c
    }, "toWarriors")
}
function expLogJSON(a, b, c) {
    return !!JA && void JA.tracker.ngloaderJSON("exp_log.100000", {
        t1: a,
        t2: b,
        p0: c
    }, "toWarriors")
}!
function ($) {
    var escapeable = /["\\\x00-\x1f\x7f-\x9f]/g,
        meta = {
            "\b": "\\b",
            "\t": "\\t",
            "\n": "\\n",
            "\f": "\\f",
            "\r": "\\r",
            '"': '\\"',
            "\\": "\\\\"
        };
    $.toJSON = "object" == typeof JSON && JSON.stringify ? JSON.stringify : function (a) {
            if (null === a) return "null";
            var b = typeof a;
            if ("undefined" !== b) {
                if ("number" === b || "boolean" === b) return "" + a;
                if ("string" === b) return $.quoteString(a);
                if ("object" === b) {
                    if ("function" == typeof a.toJSON) return $.toJSON(a.toJSON());
                    if (a.constructor === Date) {
                        var c = a.getUTCMonth() + 1,
                            d = a.getUTCDate(),
                            e = a.getUTCFullYear(),
                            f = a.getUTCHours(),
                            g = a.getUTCMinutes(),
                            h = a.getUTCSeconds(),
                            i = a.getUTCMilliseconds();
                        return c < 10 && (c = "0" + c),
                        d < 10 && (d = "0" + d),
                        f < 10 && (f = "0" + f),
                        g < 10 && (g = "0" + g),
                        h < 10 && (h = "0" + h),
                        i < 100 && (i = "0" + i),
                        i < 10 && (i = "0" + i),
                        '"' + e + "-" + c + "-" + d + "T" + f + ":" + g + ":" + h + "." + i + 'Z"'
                    }
                    if (a.constructor === Array) {
                        for (var j = [], k = 0; k < a.length; k++) j.push($.toJSON(a[k]) || "null");
                        return "[" + j.join(",") + "]"
                    }
                    var l, m, n = [];
                    for (var o in a) {
                        if (b = typeof o, "number" === b) l = '"' + o + '"';
                        else {
                            if ("string" !== b) continue;
                            l = $.quoteString(o)
                        }
                        b = typeof a[o],
                        "function" !== b && "undefined" !== b && (m = $.toJSON(a[o]), n.push(l + ":" + m))
                    }
                    return "{" + n.join(",") + "}"
                }
            }
        },
    $.evalJSON = "object" == typeof JSON && JSON.parse ? JSON.parse : function (src) {
            return eval("(" + src + ")")
        },
    $.secureEvalJSON = "object" == typeof JSON && JSON.parse ? JSON.parse : function (src) {
            var filtered = src.replace(/\\["\\\/bfnrtu]/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, "");
            if (/^[\],:{}\s]*$/.test(filtered)) return eval("(" + src + ")");
            throw new SyntaxError("Error parsing JSON, source is not valid.")
        },
    $.quoteString = function (a) {
            return a.match(escapeable) ? '"' + a.replace(escapeable, function (a) {
                var b = meta[a];
                return "string" == typeof b ? b : (b = a.charCodeAt(), "\\u00" + Math.floor(b / 16).toString(16) + (b % 16).toString(16))
            }) + '"' : '"' + a + '"'
        }
}(jQuery || $),


function () {
    function a(a) {
        for (k = $(a).attr("clstag"); !k && (a = a.parentNode, a && "BODY" != a.nodeName);) k = $(a).attr("clstag");
        return !!k
    }
    function b(a) {
        var b = 0;
        return a && a.length > 500 && (b = a.indexOf("?"), b && (a = a.substring(0, b))),
        a
    }
    function c(a) {
        return a.pageX ? a.pageX : a.clientX ? a.clientX + (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft) : -1
    }
    function d(a) {
        return a.pageY ? a.pageY : a.clientY ? a.clientY + (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) : -1
    }
    function e() {
        if (!l) {
            var a = document.domain.indexOf("jd.com") >= 0 ? document.getElementsByClassName("w") : "";
            l = a && a.length > 0 ? a[a.length - 1].offsetWidth : window.screen.width >= 1210 ? 1210 : 990
        }
        return document.body.clientWidth > l ? Math.round((document.body.clientWidth - l) / 2) : 0
    }
    function f(a, b) {
        var c = Array.prototype.slice.call(arguments);
        c = c && c.slice(2),
        JA && JA.tracker.ngloader("magic.000001", {
            t1: a,
            t2: b,
            p0: encodeURIComponent(JA.util.join(c))
        })
    }
    var g = function (a) {
        for (var b = "", c = ""; a && 1 == a.nodeType; a = a.parentNode) {
            if (c = "", a.parentNode) for (var d = a.parentNode.childNodes, e = 0, f = 0, g = d.length; e < g; e++) {
                var h = d[e];
                a.tagName === h.tagName && f++,
                a == h && (c = f > 1 ? "[" + f + "]" : "")
            }
            b = "/" + a.tagName.toLowerCase() + c + b
        }
        return b
    },
        h = function (a) {
            for (var b = "", c = ""; a && 1 == a.nodeType; a = a.parentNode) {
                if ("" !== a.id) {
                    b = '//*[@id="' + a.id + '"]' + b;
                    break
                }
                if (c = "", a.parentNode) for (var d = a.parentNode.childNodes, e = 0, f = 0, g = d.length; e < g; e++) {
                    var h = d[e];
                    a.tagName === h.tagName && f++,
                    a == h && (c = f > 1 ? "[" + f + "]" : "")
                }
                b = "/" + a.tagName.toLowerCase() + c + b
            }
            return b
        },
        i = function (a) {
            var b = !0,
                c = a.children;
            return c.length > 0 && (b = !1),
            b
        },
        j = function (b) {
            var c = !1;
            if (b && b.tagName) {
                var d = b.tagName.toLowerCase();
                c = !("html" === d || "body" === d || b.id && "tol_selected_xelemts_area" === b.id || b.parentElement && b.parentElement.id && "tol_selected_xelemts_area" === b.parentElement.id) && ( !! a(b) || ("a" === d || i(b)))
            }
            return c
        },
        k = "";
    document.getElementsByClassName || (document.getElementsByClassName = function (a) {
            for (var b = document.getElementsByTagName("*"), c = [], d = 0; d < b.length; d++) for (var e = b[d], f = e.className.split(" "), g = 0; g < f.length; g++) if (f[g] == a) {
                c.push(e);
                break
            }
            return c
        });
    var l, m = function (a) {
            var b = {};
            return b.scrollWidth = document.body.scrollWidth,
            b.scrollHeight = document.body.scrollHeight,
            b.offsetLeft = e(),
            b.x = parseInt(c(a)),
            b.y = parseInt(d(a)),
            b.offsetLeft > 0 && (b.x = parseInt(b.x - b.offsetLeft)),
            b
        },
        n = function (a) {
            try {
                var c = a.target || a.srcElement;
                if (j(c)) {
                    for (var d = m(a), e = c, l = c.tagName.toLowerCase(), n = h(c) || "-", o = c.parentNode ? h(c.parentNode) : "-", p = c.parentNode ? g(c.parentNode) + "/" : "-", q = g(c) || "-", r = q ? q.split("/").length : 1, s = e.tagName.toLowerCase();
                    "a" != s && (e = e.parentNode, e && "BODY" != e.nodeName);) s = e.tagName.toLowerCase();
                    var t = "-",
                        u = "-";
                    if (k) var v = k.split("|"),
                        t = v[2],
                        u = v[3];
                    var w = e && e.href ? b(e.href) : "-",
                        x = c.innerText.substring(.2) || "-",
                        y = c.src ? b(c.src) : "-";
                    "a" !== l || i(c) || (x = "-"),
                    f("magictree", "X", encodeURIComponent(n), encodeURIComponent(o), encodeURIComponent(p), r, d.x + "x" + d.y, d.scrollWidth + "x" + d.scrollHeight, d.offsetLeft, t, u, encodeURIComponent(w), encodeURIComponent(x), encodeURIComponent(y))
                }
            } catch (a) {
                privateLogWLJS("ERROR", "AT_xpathReport", encodeURIComponent(a))
            }
        },
        o = function (a, b) {
            var c = b || document.location.href,
                d = new RegExp("(?:^|&|[?]|[/])" + a + "=([^&]*)"),
                e = d.exec(c);
            return e ? decodeURIComponent(e[1]) : null
        },
        p = function (a) {
            var b = document.createElement("script");
            b.type = "application/javascript",
            b.src = a,
            b.charset = "UTF-8",
            document.getElementsByTagName("head")[0].appendChild(b)
        },
        q = function (a) {
            var b = document.createElement("link");
            b.type = "text/css",
            b.rel = "stylesheet",
            b.href = a,
            document.getElementsByTagName("head")[0].appendChild(b)
        },
        r = o("typepar");
    if (!r || "query" !== r && "apply" !== r) document.onclick = function (a) {
            if (a = a || event, (a.clientX || a.clientY || a.pageX || a.pageY) && (a.offsetX || a.offsetY)) {
                try {
                    n(a)
                } catch (a) {
                    privateLogWLJS("ERROR", "AT_Document_Onclick")
                }
                for (var b = document, c = window, d = tag = a.srcElement || a.target, e = $(tag).attr("clstag"), f = $(tag).attr("href"), g = ""; !e && (tag = tag.parentNode, tag && "BODY" != tag.nodeName);) e = $(tag).attr("clstag"),
                f || (f = $(tag).attr("href"), d = tag);
                if (e) {
                    var h = e.split("|"),
                        i = h[1],
                        j = h[2],
                        k = h[3];
                    if ("keycount" === i && JA) {
                            var l = JA.util.Nt();
                            f ? (JA.tracker.aloading(j, k, ["Q", f]), JA.tracker.ngloader("other.000000", {
                                t1: j,
                                t2: k,
                                p0: JA.util.join(["Q", f]),
                                cb: l.jdcb
                            })) : (JA.tracker.aloading(j, k, ["Q"]), JA.tracker.ngloader("other.000000", {
                                t1: j,
                                t2: k,
                                p0: JA.util.join(["Q"]),
                                cb: l.jdcb
                            })),
                            g = j + "|" + k,
                            f && /^(http:\/\/|https:\/\/|\/\/).*/.test(f) && "_blank" !== $(d).attr("target") && !isMeta(a) && (a.preventDefault ? a.preventDefault() : a.returnValue = !1, setTimeout(function () {
                                var a = b.createElement("a");
                                a.href = f,
                                a.target = "_self",
                                b.body.appendChild(a),
                                "undefined" != typeof a.click ? a.click() : c.location.href = f,
                                b.body.removeChild(a)
                            }, 200))
                        }
                }
                var m = this.location.hostname.toLowerCase();
                if (/(sale|mall|jmall|pop).(jd|360buy).(com|hk)/.test(m) || c.ja_heat_map) {
                    var o = 0,
                        p = 0,
                        q = c.screen.width >= 1210 && "item.jd.com" == m ? 1210 : 990,
                        r = b.body.clientWidth > q ? Math.round((b.body.clientWidth - q) / 2) : 0;
                    a.pageX || a.pageY ? (o = a.pageX, p = a.pageY) : (o = a.clientX + b.body.scrollLeft - b.body.clientLeft, p = a.clientY + b.body.scrollTop - b.body.clientTop),
                    privateLogWLJS("d", "c", g || "-", o + "x" + p, b.body.scrollWidth + "x" + b.body.scrollHeight, r)
                }
            }
        };
    else try {
            q("//magicforest.jd.com/x.css"),
            p("//magicforest.jd.com/tol.min.js")
        } catch (a) {
            privateLogWLJS("ERROR", "AT_loadCSS_OR_loadJS")
        }
}(),
HashMap.prototype.Set = function (a, b) {
    this.values[a] = b
},
HashMap.prototype.Get = function (a) {
    return this.values[a]
},
HashMap.prototype.Contains = function (a) {
    return this.values.hasOwnProperty(a)
},
HashMap.prototype.Remove = function (a) {
    delete this.values[a]
};
var SucInfoMethod = {
    Init: function () {
        this.orderDetailMap = new HashMap,
        this.rSM = new HashMap;
        for (var a = SucInfo_OrderDetail.toString().split(","), b = 0; b < a.length; b++) {
            var c = a[b].split(":");
            this.orderDetailMap.Set(c[0], c[1]),
            this.sku = c[0]
        }
    },
    GetSkuNum: function (a) {
        return this.orderDetailMap.Get(a)
    },
    Contains: function (a) {
        return this.orderDetailMap.Contains(a)
    },
    GetDefaultSku: function () {
        return this.sku
    },
    ARS: function (a) {
        this.rSM.Set(a, 0)
    },
    RSContains: function (a) {
        return this.rSM.Contains(a) ? 1 : 0
    }
};
!
function () {
    function HashMap() {
        this.values = {}
    }
    function SortedHashMap(a, b) {
        this.IComparer = a,
        this.IGetKey = b,
        this.a = [],
        this.h = new HashMap
    }
    function ThirdType(a, b, c) {
        this.t = a,
        this.v = 5,
        this.s = 0,
        arguments.length > 1 && (this.s = b),
        arguments.length > 2 && (this.v = c)
    }
    HashMap.prototype.Set = function (a, b) {
        this.values[a] = b
    },
    HashMap.prototype.Get = function (a) {
        return this.values[a]
    },
    HashMap.prototype.Contains = function (a) {
        return this.values.hasOwnProperty(a)
    },
    HashMap.prototype.Remove = function (a) {
        delete this.values[a]
    },
    SortedHashMap.prototype.Add = function (a, b) {
        this.ContainsKey(a) && this.Remove(a),
        this.a.push(b),
        this.a.sort(this.IComparer);
        for (var c = 0; c < this.a.length; c++) {
            var a = this.IGetKey(this.a[c]);
            this.h.Set(a, c)
        }
    },
    SortedHashMap.prototype.Insert = function (a, b) {
        for (var c = 0, d = this.a.length; c < d; c++) if (this.a[c].s === a.s) {
            this.a.splice(c, 1);
            break
        }
        this.a.length >= b && this.a.splice(b - 1, 1),
        this.a.unshift(a)
    },
    SortedHashMap.prototype.Get = function (a) {
        return this.a[this.h.Get(a)]
    },
    SortedHashMap.prototype.Count = function () {
        return this.a.length
    },
    SortedHashMap.prototype.Remove = function (a) {
        if (this.h.Contains(a)) {
            var b = this.h.Get(a);
            this.a.splice(b, 1),
            this.h.Remove(a)
        }
    },
    SortedHashMap.prototype.ContainsKey = function (a) {
        return this.h.Contains(a)
    },
    SortedHashMap.prototype.Clear = function () {
        this.a = [],
        this.h = new HashMap
    },
    SortedHashMap.prototype.GetJson = function () {
        return $.toJSON(this.a)
    },
    ThirdType.prototype.Increase = function () {
        this.v = this.v + 2
    },
    ThirdType.prototype.Decrease = function () {
        this.v = this.v - 1
    },
    ThirdType.prototype.SetSku = function (a) {
        this.s = a
    },
    Ttracker = {
        IComparer: function (a, b) {
            return b.v - a.v
        },
        IGetKey: function (a) {
            return a.t
        },
        isbook: function (a) {
            return a > 1e7 && a < 2e7
        },
        trace: function () {
            if ("object" == typeof pageConfig && "object" == typeof pageConfig.product) {
                var a = pageConfig.product.cat instanceof Array && pageConfig.product.cat[2];
                if (a) {
                    var b = $("#name").attr("PshowSkuid") || pageConfig.product.skuid;
                    this.view(a, b),
                    this.viewtypewid()
                }
            }
        },
        viewtypewid: function () {
            var a = Ttracker.util.Vv("typewid");
            a && Ttracker.util.Wv("typewid", "", -63072e6)
        },
        viewhisotry: function (t, s, cname) {
            var nview = {
                t: t,
                s: s
            },
                bookmap = new SortedHashMap(this.IComparer, this.IGetKey),
                bview = Ttracker.util.Vv(cname);
            if (bview) try {
                    if (bview.indexOf(".") > 0) for (var viewarray = bview.split("|"), j = viewarray.length - 1; j >= 0; j--) {
                        var book = viewarray[j].split(".");
                        bookmap.Insert({
                            t: Number(book[0]),
                            s: Number(book[1])
                        }, 8)
                    } else {
                        var bviews = eval("(" + bview + ")");
                        if (bviews.length > 0 && void 0 != bviews[0].d) Ttracker.util.Wv(cname, "", -63072e6);
                        else for (var i = bviews.length - 1; i >= 0; i--) bookmap.Insert(bviews[i], 8)
                    }
                } catch (a) {
                    Ttracker.util.Wv(cname, "", -63072e6)
                }
            bookmap.Insert(nview, 8);
            for (var cvalue = "", k = 0, klen = bookmap.a.length; k < klen; k++) cvalue += bookmap.a[k].t + "." + bookmap.a[k].s + (k == klen - 1 ? "" : "|");
            cvalue && Ttracker.util.Wv(cname, cvalue, 63072e6)
        },
        viewrate: function (t, s, cname) {
            var ntw = {
                t: t,
                s: s,
                v: 5
            },
                sitesortmap = new SortedHashMap(this.IComparer, this.IGetKey),
                vrate = Ttracker.util.Vv(cname);
            if (vrate) try {
                    if (vrate.indexOf(".") > 0) for (var ratearray = vrate.split("|"), j = ratearray.length - 1; j >= 0; j--) {
                        var tw = ratearray[j].split("."),
                            tv = Number(tw[2] || 0),
                            tid = Number(tw[0]);
                        tv = t === tid ? tv : tv - 1,
                        sitesortmap.Add(Number(tw[0]), {
                                t: Number(tw[0]),
                                s: Number(tw[1]),
                                v: tv
                            }, 8)
                    } else {
                        var vrates = eval("(" + vrate + ")");
                        if (vrates.length > 0 && void 0 != vrates[0].d) Ttracker.util.Wv(cname, "", -63072e6);
                        else for (var i = 0; i < vrates.length; i++) {
                            var rate = vrates[i];
                            rate.t != t && (rate.v -= 1),
                            sitesortmap.Add(rate.t, rate)
                        }
                    }
                } catch (a) {
                    Ttracker.util.Wv(cname, "", -63072e6)
                }
            if (sitesortmap.ContainsKey(t)) {
                    var curtt = sitesortmap.Get(t);
                    curtt.s = s ? s : curtt.s,
                    curtt.v += 2
                } else sitesortmap.Add(t, ntw);
            if (sitesortmap.Count() > 8) {
                    var del = sitesortmap.a[sitesortmap.Count() - 1];
                    sitesortmap.Remove(del.t)
                }
            for (var cvalue = "", k = 0, klen = sitesortmap.a.length; k < klen; k++) cvalue += sitesortmap.a[k].t + "." + sitesortmap.a[k].s + "." + sitesortmap.a[k].v + (k == klen - 1 ? "" : "|");
            cvalue && Ttracker.util.Wv(cname, cvalue, 63072e6)
        },
        view: function (a, b) {
            var c = Number(a),
                d = Number(b),
                e = this;
            $.ajax({
                    url: "//diviner.jd.com/cookie?ck=" + c + "." + d,
                    dataType: "jsonp",
                    success: function (a) {
                        "object" == typeof a && 0 == a.errCode && (e.util.Wv("atw", "", -63072e6), e.isbook(d) && (e.util.Wv("btw", "", -63072e6), e.util.Wv("bview", "", -63072e6)))
                    }
                }),
            $.ajax({
                    url: "//x.jd.com/aview?ck=" + c + "." + d,
                    dataType: "jsonp",
                    success: function (a) {
                        "object" == typeof a && 0 == a.errCode && e.util.Wv("aview", "", -63072e6)
                    }
                })
        }
    },
    Ttracker.util = {
        Wv: function (a, b, c) {
            var d = window.document.domain.indexOf("360buy") >= 0 ? ".360buy.com" : ".jd.com";
            a = a + "=" + b + "; path=/; ",
            c && (a += "expires=" + new Date((new Date).getTime() + c).toGMTString() + "; "),
            a += "domain=" + d + ";",
            document.cookie = a
        },
        Vv: function (a) {
            for (var b = [], c = document.cookie.split(";"), a = RegExp("^\\s*" + a + "=\\s*(.*?)\\s*$"), d = 0; d < c.length; d++) {
                var e = c[d].match(a);
                e && b.push(e[1])
            }
            return b[0]
        }
    },
    Ttracker.trace()
}(),


function () {
    function a(a) {
        return (a ? "_" : "") + o++
    }
    var b = window,
        c = document,
        d = encodeURIComponent,
        e = decodeURIComponent,
        f = void 0,
        g = "push",
        h = "join",
        i = "split",
        j = "length",
        k = "indexOf",
        l = "toLowerCase",
        m = "0.1",
        n = {};
    n.util = {
            join: function (a) {
                if (a instanceof Array) {
                    for (var b = "", c = 0, d = a.length; c < d; c++) b += a[c] + (c == d - 1 ? "" : "|||");
                    return b
                }
                return a
            },
            getParameter: function (a, b) {
                var c = new RegExp("(?:^|&|[?]|[/])" + b + "=([^&]*)"),
                    e = c.exec(a);
                return e ? d(e[1]) : ""
            },
            Wv: function (a, b, d, e) {
                a = a + "=" + b + "; path=/; ",
                e && (a += "expires=" + new Date((new Date).getTime() + e).toGMTString() + "; "),
                d && (a += "domain=" + d + ";"),
                c.cookie = a
            },
            Vv: function (a) {
                for (var b = [], d = c.cookie[i](";"), e = RegExp("^\\s*" + a + "=\\s*(.*?)\\s*$"), f = 0; f < d[j]; f++) {
                    var h = d[f].match(e);
                    h && b[g](h[1])
                }
                return b
            }
        };
    var o = 0,
        p = a(),
        q = a(),
        r = a(),
        s = a(),
        t = a(),
        u = a(),
        v = a(),
        w = a(),
        x = a(),
        y = a(),
        z = a(),
        A = a(),
        B = a(),
        C = a(),
        D = a(),
        E = a(),
        F = a(),
        G = a(),
        H = a(),
        I = a(),
        J = a(),
        K = a(),
        L = a(),
        M = a(),
        N = a(),
        O = a(),
        P = a(),
        Q = a(),
        R = a(),
        S = a(),
        T = a(),
        U = a(),
        V = a(),
        W = a(),
        X = a(),
        Y = a(),
        Z = a(),
        _ = function () {
            var a = {};
            this.set = function (b, c) {
                a[b] = c
            },
            this.get = function (b) {
                return a[b] !== f ? a[b] : null
            },
            this.m = function (b) {
                var c = this.get(b),
                    d = c == f || "" === c ? 0 : 1 * c;
                a[b] = d + 1
            },
            this.set(p, "UA-J2011-1");
            var b = window.document.domain.indexOf("360buy") >= 0 ? "360buy.com" : "jd.com";
            this.set(s, b),
            this.set(r, da()),
            this.set(t, Math.round((new Date).getTime() / 1e3)),
            this.set(u, 15552e6),
            this.set(v, 1296e6),
            this.set(w, 18e5),
            this.set(C, fa());
            var c = ia();
            this.set(D, c.name),
            this.set(E, c.version),
            this.set(F, ja());
            var d = ea();
            this.set(G, d.D),
            this.set(H, d.C),
            this.set(I, d.language),
            this.set(J, d.javaEnabled),
            this.set(K, d.characterSet),
            this.set(Q, aa),
            this.set(V, (new Date).getTime());
            var e = n.util.Vv("pin");
            this.set(Y, e[j] ? e[0] : "-");
            var g, h = "";
            (g = n.util.Vv("pinId")) && g[j] && (h = g[0]),
            this.set(Z, h || "-")
        },
        aa = ["i.easou.com:q", "m.baidu.com:word", "m.sm.cn:q", "m.so.com:q", "wap.sogou.com:keyword", "m.sogou.com:keyword", "page.roboo.com:q", "ask.com:q", "baidu:word", "baidu:wd", "bing:q", "easou:q", "google:q", "roboo:word", "roboo:q", "sm.cn:q", "so.com:q", "sogou:keyword", "sogou:query", "yahoo:p", "yandex:text", "yicha:key"],
        ba = function () {
            return Math.round((new Date).getTime() / 1e3)
        },
        ca = function () {
            return (new Date).getTime() + "" + parseInt(2147483647 * Math.random())
        },
        da = function () {
            return ha(c.domain)
        },
        ea = function () {
            var a = {},
                d = b.navigator,
                e = b.screen;
            return a.D = e ? e.width + "x" + e.height : "-",
            a.C = e ? e.colorDepth + "-bit" : "-",
            a.language = (d && (d.language || d.browserLanguage) || "-")[l](),
            a.javaEnabled = d && d.javaEnabled() ? 1 : 0,
            a.characterSet = c.characterSet || c.charset || "-",
            a
        },
        fa = function () {
            var a, b, c, d;
            if (c = "ShockwaveFlash", (a = (a = window.navigator) ? a.plugins : f) && a[j] > 0) for (b = 0; b < a[j] && !d; b++) c = a[b],
            c.name[k]("Shockwave Flash") > -1 && (d = c.description[i]("Shockwave Flash ")[1]);
            else {
                c = c + "." + c;
                try {
                    b = new ActiveXObject(c + ".7"),
                    d = b.GetVariable("$version")
                } catch (a) {}
                if (!d) try {
                    b = new ActiveXObject(c + ".6"),
                    d = "WIN 6,0,21,0",
                    b.AllowScriptAccess = "always",
                    d = b.GetVariable("$version")
                } catch (a) {}
                if (!d) try {
                    b = new ActiveXObject(c),
                    d = b.GetVariable("$version")
                } catch (a) {}
                d && (d = d[i](" ")[1][i](","), d = d[0] + "." + d[1] + " r" + d[2])
            }
            var e = n.util.Vv("_r2");
            a = d ? d + (e[j] > 0 ? "_" + e[0] : "") : "-";
            var g = n.util.Vv("limgs");
            return a += g[j] > 0 ? "_" + g[0] : ""
        },
        ga = function (a) {
            return f == a || "-" == a || "" == a
        },
        ha = function (a) {
            var b, c = 1,
                d = 0;
            if (!ga(a)) for (c = 0, b = a[j] - 1; b >= 0; b--) d = a.charCodeAt(b),
            c = (c << 6 & 268435455) + d + (d << 14),
            d = 266338304 & c,
            c = 0 != d ? c ^ d >> 21 : c;
            return c
        },
        ia = function () {
            var a = {
                name: "other",
                version: "0"
            },
                b = navigator.userAgent.toLowerCase();
            browserRegExp = {
                    se360: /360se/,
                    se360_2x: /qihu/,
                    ie: /msie[ ]([\w.]+)/,
                    firefox: /firefox[|\/]([\w.]+)/,
                    chrome: /chrome[|\/]([\w.]+)/,
                    safari: /version[|\/]([\w.]+)(\s\w.+)?\s?safari/,
                    opera: /opera[|\/]([\w.]+)/
                };
            for (var c in browserRegExp) {
                    var d = browserRegExp[c].exec(b);
                    if (d) {
                        a.name = c,
                        a.version = d[1] || "0";
                        break
                    }
                }
            return a
        },
        ja = function () {
            var a = /(win|android|linux|nokia|ipad|iphone|ipod|mac|sunos|solaris)/.exec(navigator.platform.toLowerCase());
            return null == a ? "other" : a[0]
        },
        ka = function () {
            for (var a = "", b = ["jwotest_product", "jwotest_list", "jwotest_cart", "jwotest_orderinfo", "jwotest_homepage", "jwotest_other1", "jwotest_other2", "jwotest_other3"], c = 0, d = b.length; c < d; c++) {
                var f = n.util.Vv(b[c]);
                if (0 != f[j]) {
                    var g = e(f[0]).match(/=(.*?)&/gi),
                        i = [];
                    null != g && ($.each(g, function (a, b) {
                            i.push(0 == a ? "T" + b.substring(1, b.length - 1) : b.substring(1, b.length - 1))
                        }), a += i[h]("-") + ";")
                }
            }
            return a
        },
        la = function (a) {
            a.set(x, c.location.hostname),
            a.set(y, c.title.replace(/\$/g, "")),
            a.set(z, c.location.pathname),
            a.set(A, c.referrer.replace(/\$/g, "")),
            a.set(B, c.location.href);
            var b = n.util.Vv("__jda"),
                d = b[j] > 0 ? b[0][i](".") : null;
            a.set(q, d && !ga(d[1]) ? d[1] : ca()),
            a.set(L, d ? d[2] : a.get(t)),
            a.set(M, d ? d[3] : a.get(t)),
            a.set(N, d ? d[4] : a.get(t)),
            a.set(O, d ? d[5] : 1);
            var e = n.util.Vv("__jdv"),
                f = e[j] > 0 ? e[0][i]("|") : null;
            a.set(R, f ? f[1] : "direct"),
            a.set(S, f ? f[2] : "-"),
            a.set(T, f ? f[3] : "none"),
            a.set(U, f ? f[4] : "-");
            var g = n.util.Vv("__jdb"),
                h = g[j] > 0 ? g[0][i](".") : null,
                k = h && 4 == h.length ? 1 : 0;
            a.set(P, h ? h[0 + k] : 0),
            a.set(W, ka() || "-");
            var l = JA.util.Vv("clickid"),
                m = l[j] && l[0];
            return a.set(X, m),
            !0
        },
        ma = function () {
            var a = n.util.Vv("__jdb"),
                b = a[j] > 0 ? a[0][i](".") : null;
            return b && 1 == b.length ? 1 * b[0] : b && 4 == b.length ? 1 * b[1] : 0
        },
        na = function (a) {
            var b = c.location.search,
                d = c.referrer,
                e = a.get(s),
                f = n.util.getParameter(b, "utm_source"),
                h = [],
                m = a.get(R),
                o = a.get(S),
                p = a.get(T),
                q = 0 == n.util.Vv("__jdb")[j],
                r = !1;
            if (f) {
                    var t = n.util.getParameter(b, "utm_campaign"),
                        u = n.util.getParameter(b, "utm_medium"),
                        v = n.util.getParameter(b, "utm_term");
                    h[g](f),
                    h[g](t || "-"),
                    h[g](u || "-"),
                    h[g](v || "not set"),
                    a.set(U, h[3]),
                    r = !0
                } else {
                    var w = d && d[i]("/")[2],
                        x = !1;
                    if (w && w[k](e) < 0) {
                            for (var y = a.get(Q), z = 0; z < y.length; z++) {
                                var A = y[z][i](":");
                                if (w[k](A[0][l]()) > -1 && d[k]((A[1] + "=")[l]()) > -1) {
                                    var B = n.util.getParameter(d, A[1]);
                                    h[g](A[0]),
                                    h[g]("-"),
                                    h[g]("organic"),
                                    h[g](B || "not set"),
                                    a.set(U, h[3]),
                                    x = !0;
                                    break
                                }
                            }
                            x || (w[k]("zol.com.cn") > -1 ? (h[g]("zol.com.cn"), h[g]("-"), h[g]("cpc"), h[g]("not set")) : (h[g](w), h[g]("-"), h[g]("referral"), h[g]("-")))
                        }
                }
            var C = h[j] > 0 && (h[0] !== m || h[1] !== o || h[2] !== p) && "referral" !== h[2];
            return q || !q && C ? (a.set(R, h[0] || a.get(R)), a.set(S, h[1] || a.get(S)), a.set(T, h[2] || a.get(T)), a.set(U, h[3] || a.get(U)), ua(a)) : qa(a),
            C || r
        },
        oa = function (a, b) {
            var c = b.split(".");
            a.set(L, c[2]),
            a.set(M, c[4]),
            a.set(N, ba()),
            a.m(O),
            a.set(P, 1)
        },
        pa = function (a) {
            var b = a.get(t);
            a.set(q, ca()),
            a.set(L, b),
            a.set(M, b),
            a.set(N, b),
            a.set(O, 1),
            a.set(P, 1)
        },
        qa = function (a) {
            a.m(P)
        },
        ra = function (a) {
            return [a.get(r), a.get(q) || "-", a.get(L) || "-", a.get(M) || "-", a.get(N) || "-", a.get(O) || 1][h](".")
        },
        sa = function (a) {
            return [a.get(r), a.get(P) || 1, a.get(q) + "|" + a.get(O) || 1, a.get(N) || a.get(t)][h](".")
        },
        ta = function (a) {
            return [a.get(r), a.get(R) || c.domain, a.get(S) || "(direct)", a.get(T) || "direct", a.get(U) || "-", (new Date).getTime()][h]("|")
        },
        ua = function (a) {
            var b = n.util.Vv("__jda");
            0 == b.length ? pa(a) : oa(a, b[0])
        },
        va = new _,
        wa = function () {
            this.a = {},
            this.add = function (a, b) {
                this.a[a] = b
            },
            this.get = function (a) {
                return this.a[a]
            },
            this.toString = function () {
                return this.a[h]("&")
            }
        },
        xa = function (a, b) {
            b.add("jdac", a.get(p)),
            b.add("jduid", a.get(q)),
            b.add("jdsid", a.get(q) + "|" + a.get(O)),
            b.add("jdje", a.get(J)),
            b.add("jdsc", a.get(H)),
            b.add("jdsr", a.get(G)),
            b.add("jdul", a.get(I)),
            b.add("jdcs", a.get(K)),
            b.add("jddt", a.get(y) || "-"),
            b.add("jdmr", d(a.get(A))),
            b.add("jdhn", a.get(x) || "-"),
            b.add("jdfl", a.get(C)),
            b.add("jdos", a.get(F)),
            b.add("jdbr", a.get(D)),
            b.add("jdbv", a.get(E)),
            b.add("jdwb", a.get(L)),
            b.add("jdxb", a.get(M)),
            b.add("jdyb", a.get(N)),
            b.add("jdzb", a.get(O)),
            b.add("jdcb", a.get(P)),
            b.add("jdusc", a.get(R) || "direct"),
            b.add("jducp", a.get(S) || "-"),
            b.add("jdumd", a.get(T) || "-"),
            b.add("jduct", a.get(U) || "-"),
            b.add("jdlt", "object" != typeof jdpts ? 0 : void 0 == jdpts._st ? 0 : a.get(V) - jdpts._st),
            b.add("jdtad", a.get(W)),
            b.add("jdak", a.get(X)),
            b.add("pinid", a.get(Z))
        },
        ya = function (a, b, c, e) {
            b.add("jdac", a.get(p)),
            b.add("jduid", a.get(q)),
            b.add("jdsid", a.get(q) + "|" + a.get(O)),
            b.add("jdje", "-"),
            b.add("jdsc", "-"),
            b.add("jdsr", "-"),
            b.add("jdul", "-"),
            b.add("jdcs", "-"),
            b.add("jddt", "-"),
            b.add("jdmr", d(a.get(A))),
            b.add("jdhn", "-"),
            b.add("jdfl", "-"),
            b.add("jdos", "-"),
            b.add("jdbr", "-"),
            b.add("jdbv", "-"),
            b.add("jdwb", "-"),
            b.add("jdxb", "-"),
            b.add("jdyb", "-"),
            b.add("jdzb", a.get(O)),
            b.add("jdcb", e ? ma() + e : a.get(P)),
            b.add("jdusc", "-"),
            b.add("jducp", "-"),
            b.add("jdumd", "-"),
            b.add("jduct", "-"),
            b.add("jdlt", 0),
            b.add("jdtad", c),
            b.add("jdak", a.get(X)),
            b.add("pinid", a.get(Z))
        },
        za = function () {
            la(va);
            var a = na(va),
                b = n.util.Vv("__jdv"),
                c = new wa,
                d = va.get(s);
            return xa(va, c),
            n.util.Wv("__jda", ra(va), d, va.get(u)),
            n.util.Wv("__jdb", sa(va), d, va.get(w)),
            n.util.Wv("__jdc", va.get(r), d),
            !a && b.length || n.util.Wv("__jdv", ta(va), d, va.get(v)),
            n.util.Wv("clickid", "0", d, -846e5),
            c.a
        },
        Aa = function () {
            var a = new wa;
            return xa(va, a),
            a.a
        },
        Ba = function (a, b) {
            var c = new wa;
            return ya(va, c, a, b),
            c.a
        },
        Ca = function (a) {
            var b = document.createElement("img");
            return b.width = 1,
            b.height = 1,
            b.src = a,
            b
        },
        Da = function (a) {
            var b = Ca(a);
            b.onload = b.onerror = function () {
                b.onload = null,
                b.onerror = null
            }
        };
    n.util.Nt = Aa,
    n.tracker = {
            sendOld: function (a, b, c, d) {

            },
            sendNew: function (a, b) {
                var e = Aa(),
                    f = ("https:" == document.location.protocol ? "https://mercury" : "http://mercury") + ".jd.com/log.gif?t=" + a + "&m=" + va.get(p) + "&pin=" + d(va.get(Y)) + "&uid=" + e.jduid + "&sid=" + e.jdsid + (e.jdak ? "&cul=" + document.location.href + d("&clickid=" + e.jdak) : "") + "&v=" + d(b) + "&ref=" + d(c.referrer) + "&rm=" + (new Date).getTime();
                Da(f)
            },
            sendToWarriors: function (a, b) {
                var e = Aa(),
                    f = ("https:" == document.location.protocol ? "https://warriors" : "http://warriors") + ".jd.com/log.gif?t=" + a + "&m=" + va.get(p) + "&pin=" + d(va.get(Y)) + "&uid=" + e.jduid + "&sid=" + e.jdsid + (e.jdak ? "&cul=" + document.location.href + d("&clickid=" + e.jdak) : "") + "&v=" + d(b) + "&ref=" + d(c.referrer) + "&rm=" + (new Date).getTime();
                Da(f)
            },
            ngloader: function (a, b) {
                var c = Aa(),
                    d = {
                        je: c.jdje,
                        sc: c.jdsc,
                        sr: c.jdsr,
                        ul: c.jdul,
                        cs: c.jdcs,
                        dt: c.jddt,
                        hn: c.jdhn,
                        fl: c.jdfl,
                        os: c.jdos,
                        br: c.jdbr,
                        bv: c.jdbv,
                        wb: c.jdwb,
                        xb: c.jdxb,
                        yb: c.jdyb,
                        zb: c.jdzb,
                        cb: c.jdcb,
                        usc: c.jdusc,
                        ucp: c.jducp,
                        umd: c.jdumd,
                        uct: c.jduct,
                        ct: (new Date).getTime(),
                        lt: c.jdlt,
                        tad: c.jdtad
                    };
                this.ngaloader(a, d, b)
            },
            ngaloader: function (a, b, c) {
                var d = "";
                for (var e in b) d += e + "=" + b[e] + "$";
                if (c) for (var e in c) d += e + "=" + c[e] + "$";
                d += "pinid=" + va.get(Z) + "$";
                try {
                    d += "jdv=" + (n.util.Vv("__jdv")[0] || "") + "$"
                } catch (a) {}
                d += "dataver=" + m + "$",
                d = d.substring(0, d.length - 1),
                this.sendNew(a, d)
            },
            ngloaderJSON: function (a, b, c) {
                var d = Aa();
                b.pinid = va.get(Z),
                b.je = d.jdje,
                b.sc = d.jdsc,
                b.sr = d.jdsr,
                b.ul = d.jdul,
                b.cs = d.jdcs,
                b.dt = d.jddt,
                b.hn = d.jdhn,
                b.fl = d.jdfl,
                b.os = d.jdos,
                b.br = d.jdbr,
                b.bv = d.jdbv,
                b.wb = d.jdwb,
                b.xb = d.jdxb,
                b.yb = d.jdyb,
                b.zb = d.jdzb,
                b.cb = d.jdcb,
                b.usc = d.jdusc,
                b.ucp = d.jducp,
                b.umd = d.jdumd,
                b.uct = d.jduct,
                b.ct = (new Date).getTime(),
                b.lt = d.jdlt,
                b.tad = d.jdtad;
                try {
                    b.jdv = n.util.Vv("__jdv")[0] || ""
                } catch (a) {}
                b.dataver = m,
                c && "toWarriors" == c ? this.sendToWarriors(a, $.toJSON(b)) : this.sendNew(a, $.toJSON(b))
            },
            bloading: function (a, b, c) {
                var d = za();
                this.loading(a, b, d, c);
                var e = {
                    je: d.jdje,
                    sc: d.jdsc,
                    sr: d.jdsr,
                    ul: d.jdul,
                    cs: d.jdcs,
                    dt: d.jddt,
                    hn: d.jdhn,
                    fl: d.jdfl,
                    os: d.jdos,
                    br: d.jdbr,
                    bv: d.jdbv,
                    wb: d.jdwb,
                    xb: d.jdxb,
                    yb: d.jdyb,
                    zb: d.jdzb,
                    cb: d.jdcb,
                    usc: d.jdusc,
                    ucp: d.jducp,
                    umd: d.jdumd,
                    uct: d.jduct,
                    lt: d.jdlt,
                    ct: c,
                    tad: d.jdtad
                };
                this.ngaloader("www.100000", e),
                d.jduid % 1e3 === 1 && this.ngloader("jsver.000000", {
                    jsfile: "wl",
                    jsver: "20141223"
                })
            },
            loading: function (a, b, c, d) {
                this.sendOld(a, b, c, JA.util.join(d))
            },
            aloading: function (a, b, c) {
                var d = Aa();
                this.loading(a, b, d, c)
            },
            aloadingJSON: function (a, b, c) {
                var d = Aa();
                this.sendOld(a, b, d, $.toJSON(c))
            },
            adshow: function (a) {
                var b = Ba(a);
                this.loading("AD", "IM", b, "")
            },
            adclick: function (a) {
                var b = Ba(a, 1);
                this.loading("AD", "CL", b, "")
            },
            isCanPrey: function () {
                var a = getCookie("__jda");
                if (a) {
                    var b = a.split(".");
                    if (b.length > 1) {
                        var c = b[1],
                            d = b[1].length;
                        return c = c.substr(d - 1, d),
                        "2" == c
                    }
                }
                return !1
            }
        },
    window.JA = n,
    n.tracker.bloading("J", "A", (new Date).getTime());
    var Ea = 5 === $(".w .crumb a").length && /e.jd.com\/products\/(\d*)-(\d*)-(\d*).html[\w\W]*?e.jd.com\/(\d*).html/.exec($(".w .crumb").html());
        (window.pageConfig && window.pageConfig.product && window.pageConfig.product.cat || Ea) && n.tracker.ngloader("item.010001", {
            sku: Ea[4] || window.pageConfig.product.skuid,
            cid1: Ea[1] || window.pageConfig.product.cat[0],
            cid2: Ea[2] || window.pageConfig.product.cat[1],
            cid3: Ea[3] || window.pageConfig.product.cat[2],
            brand: Ea ? "0" : window.pageConfig.product.brand
        }),


    function () {
            if (isChecked()) {
                SucInfoMethod.Init();
                var a = getCookie("_distM");
                if (a && a == SucInfo_OrderId) return !0;
                for (var b = ["p000", "p100", "np000", "np100"], c = 0; c < b.length; c++) {
                    var d = getCookie(b[c]);
                    null != d && "" != d && privateLogWLJS("HomePageOrder", b[c])
                }
                var e = "1:2:3:4:5:1a:1b:BR1:BR2:BR3:BR4:BR5:DDR:GR1:GR2:GR3:GR4:VR1:VR2:VR3:VR4:VR5:NR:CR1:CR2:CR3:SR1:SR2:SR3:SR4:Indiv&Simi:Indiv&OthC:Indiv&AllC:Zd";
                simpleMold(e.split(":"), "R", "reWids", "4");
                var f = "Club,ThirdRec,AttRec,OCRec,SORec,EBRec,BookSpecial,BookTrack,BookHis,Coupon,GlobalTrack,GlobalHis,History,historyreco_s,historyreco_c";
                complexMold(f.split(","), "R", "reWids", "4");
                var g = ["v", "TrackRec", "TrackHis", "CouDan", "CarAcc", "Zd", "Tc", "g", "s", "Book", "BookSpecial", "BookTrack", "BookHis", "GlobalTrack", "GlobalHis", "History", "Hiss", "Hisc", "simi", "GThirdRec", "PtoAccy", "AtoAccy"];
                complexMold(g, "o", "rod", "d", !0),
                RecommendTrans("reHome2012,_rtbook", "N", "4"),
                complexMold(["_rdCube"], "Cube", "", "4"),
                simpleMold(["SEO"], "S", "seWids", "4"),
                setCookieMills("_distM", SucInfo_OrderId, 864e5),
                setCookieMills("_ghis", "", -1),
                privateLogWLJS("7", "2", SucInfo_OrderId, SucInfo_OrderType, SucInfo_OrderDetail);
                var h = Aa();
                JA && JA.tracker.ngloader("order.100000", {
                    orderid: SucInfo_OrderId,
                    ordertype: SucInfo_OrderType,
                    orderdetail: SucInfo_OrderDetail,
                    cb: h.jdcb
                })
            }
        }()
}(),


function () {
    "object" == typeof jdpts && jdpts._cls && privateLogWLJS(jdpts._cls.split(".")[0], jdpts._cls.split(".")[1])
}(),
Clublog();