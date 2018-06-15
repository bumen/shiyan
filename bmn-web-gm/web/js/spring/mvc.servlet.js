/**
 * Created by Administrator on 2017/10/30.
 */
define(
    "spring/mvc.servlet.js",
    [

    ],
    function() {

        var servlet = {
            ajaxBtn: $("#ajaxBtn"),
            init: function() {
                this.ajaxBtn.bind("click", function() {
                    /*$.ajax({
                        type : "POST",
                        url  : "argumentResolver/requestBody",
                        contentType : "application/json",
                        data : '{"id":3}',
                        success : function() {
                            console.log("success");
                        }
                    });*/


                });
            }
        };

        return servlet;
    }
);