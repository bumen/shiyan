/**
 * Created by Administrator on 2017/8/2.
 */

define(
    "ui/easyui.dialog.js",
    [],
    function(require) {
        var dialog = {
            "dom" : function(id) {
                return $("#"+ id);
            },
            "create": function(target, info) {
                var opt = {
                    "width": 100,
                    "height": 100,
                    "title": "dialog",
                    cache: false,
                    minimizable: false,
                    maximizable: false,
                    collapsible: true,
                    closed: true,   //初始化时，关闭
                    modal: true,
                    buttons:[{
                        text:'提交',
                        iconCls:'icon-ok',
                        handler:function(){ }
                    },{
                        text:'关闭',
                        iconCls:'icon-cancel',
                        handler:function(){ }
                    }],
                    OnClose: function() { }
                };

                $.extend(opt, info);

                target.dialog(opt);
            },
            "open": function(target) {
                target.dialog('open', true);
            },
            "close" : function(target) {
                target.dialog('close', true);
            }
        };
        return dialog;

    }
);