/* user-passport/1.0.0 login.countrycode-1130.js Date:2017-05-17 17:26:00 */
define("login/login.countrycode-1130.js",
        [
            "ui/switchable.js",
            "login/login.pubSub.js",
            "ui/jquery.tinyscrollbar.js"
        ],
    function(require, a) {
    require("ui/switchable.js");
    var d = require("login/login.pubSub.js");
    require("ui/jquery.tinyscrollbar.js");
    var f = {
        currentIndex: 0,
        elelist: {
            formlogin: $(".login-form"),
            loginname: $("#loginname")
        },
        countries: [],
        tpl: '<div id="country_code_layer"> <iframe frameborder= "0" scrolling="no" style="background-color:transparent; position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left:0;"></iframe>        <a class="arrow" ></a>        <div class="switchbar">        <a class="ui-switchable-item">\u70ed\u95e8</a>        <a class="ui-switchable-item">\u4e9a\u6d32</a>        <a class="ui-switchable-item">\u7f8e\u6d32</a>        <a class="ui-switchable-item">\u6b27\u6d32</a>        <a class="ui-switchable-item">\u5927\u6d0b\u6d32</a>        <a class="ui-switchable-item">\u975e\u6d32</a>        </div>    <ul class="ui-switchable-panel-main">',
        resetPosition: function() {
            var a = this.elelist.selector.offset();
            var b = a.top;
            var c = a.left;
            this.elelist.layer.offset({
                top: b + 30,
                left: c - 198
            })
        },
        fillData: function() {
            var a = this.tpl;
            var b = '<div id="scrollbar_all" class="scrollbar_all"><div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div><div class="viewport"><div class="overview"><div class="ui-switchable-panel"><ul>';
            var c = "";
            var d = "";
            var e = "";
            var f = "";
            var g = "";
            var h = "";
            var i = "</ul></div></div></div></div>";
            var j = this.countries;
            for (var k = 0; k < j.length; k++) switch (j[k].continent) {
                case 0:
                    c += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>";
                    break;
                case 1:
                    d += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>";
                    break;
                case 2:
                    e += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>";
                    break;
                case 3:
                    f += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>";
                    break;
                case 4:
                    g += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>";
                    break;
                case 5:
                    h += '<li><a href="javascript:void(0)" data-code="' + j[k].id + '">' + j[k].value + "</a></li>"
            }
            return a += b.replace("scrollbar_all", "scrollbar0") + c + i + b.replace("scrollbar_all", "scrollbar1") + d + i + b.replace("scrollbar_all", "scrollbar2") + e + i + b.replace("scrollbar_all", "scrollbar3") + f + i + b.replace("scrollbar_all", "scrollbar4") + g + i + b.replace("scrollbar_all", "scrollbar5") + h + i, a += "</ul></div>"
        },
        getCountryItems: function() {
            var a = this;
            $.ajax({
                type: "GET",
                url: "/uc/countryItems",
                dataType: "json",
                success: function(b) {
                    b && (a.countries = b, !$("#country_code_layer").length > 0 ? a.elelist.formlogin.after(a.fillData()) : $("#country_code_layer").prop("outerHTML", a.fillData()), a.bindEvent())
                }
            })
        },
        bindEvent: function() {
            var a = this;
            a.elelist.layer = $("#country_code_layer"),
            a.elelist.selector = $("#country_code_selector"),
            a.elelist.close = $("#close_button"),
            a.elelist.areacode = $(".ui-switchable-panel a"),
            $(document).bind("click", function(b) {
                $(b.target).closest("#country_code_layer").length < 1 && $(b.target).closest("#select-country").length < 1 && (a.elelist.layer.hide(),
                $.browser.safari && $("#entry").css("visibility", "visible"))
            }),
            $("body").delegate("#country_code_selector", "hover", function() {
                a.elelist.layer.show(), $(".scrollbar_all").hide(),
                $("#scrollbar" + a.currentIndex).show(),
                $("#scrollbar" + a.currentIndex).tinyscrollbar(),
                $.browser.safari && $("#entry").css("visibility", "hidden"),
                $.browser.msie && $("#loginname").blur(), a.resetPosition()
            }),
            a.elelist.close.bind("click", function() {
                a.elelist.layer.hide(),
                $.browser.safari && $("#entry").css("visibility", "visible"),
                d.publish("username.focus"), setTimeout(function() {
                    if ($("#loginname").focus(), $.browser.msie) {
                        var a = $("#loginname")[0].createTextRange();
                        a.moveStart("character", $("#loginname").val().length),
                        a.collapse(!0), a.select()
                    }
                }, 180)
            }),
            a.elelist.areacode.bind("click", function() {
                a.elelist.layer.hide(),
                $.browser.safari && $("#entry").css("visibility", "visible"),
                a.elelist.loginname.val($(this).attr("data-code")),
                d.publish("username.focus"), setTimeout(function() {
                    if ($("#loginname").focus(), $.browser.msie) {
                        var a = $("#loginname")[0].createTextRange();
                        a.moveStart("character", $("#loginname").val().length),
                        a.collapse(!0), a.select()
                    }
                }, 180)
            }),
            a.elelist.layer.switchable({
                type: "tab",
                navItem: "ui-switchable-item",
                navSelectedClass: "ui-switchable-selected",
                contentClass: "ui-switchable-panel-main",
                mainClass: "ui-switchable-panel",
                hasPage: !0,
                speed: "fast",
                seamlessLoop: !0,
                callback: function() {
                    if (!$("#country_code_layer").is(":hidden")) switch (this.current) {
                        case 0:
                            $(".scrollbar_all").hide(), $("#scrollbar0").show(), $("#scrollbar0").tinyscrollbar(), a.currentIndex = 0;
                            break;
                        case 1:
                            $(".scrollbar_all").hide(), $("#scrollbar1").show(), $("#scrollbar1").tinyscrollbar(), a.currentIndex = 1;
                            break;
                        case 2:
                            $(".scrollbar_all").hide(), $("#scrollbar2").show(), $("#scrollbar2").tinyscrollbar(), a.currentIndex = 2;
                            break;
                        case 3:
                            $(".scrollbar_all").hide(), $("#scrollbar3").show(), $("#scrollbar3").tinyscrollbar(), a.currentIndex = 3;
                            break;
                        case 4:
                            $(".scrollbar_all").hide(), $("#scrollbar4").show(), $("#scrollbar4").tinyscrollbar(), a.currentIndex = 4;
                            break;
                        case 5:
                            $(".scrollbar_all").hide(), $("#scrollbar5").show(), $("#scrollbar5").tinyscrollbar(), a.currentIndex = 5;
                            break;
                        case 6:
                            $(".scrollbar_all").hide(), $("#scrollbar6").show(), $("#scrollbar6").tinyscrollbar(), a.currentIndex = 6
                    }
                }
            }), $(window).resize(function() {
                a.resetPosition()
            })
        },
        init: function() {
            var a = this;
            a.getCountryItems()
        }
    };

    function g() {
        return f.init()
    }
    a.init = g
});