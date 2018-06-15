/**
 *
 * Created by Administrator on 2017/8/2.
 */
define(
    "ui/easyui.rightmenu.js",
    [],
    function(require) {
        var menu = {
            "dom": function(id) {
                return $("#" +id);
            },
            "bindContextMenu": function(menuNode, target) {
                $(target).bind('contextmenu',function(e){
                    e.preventDefault();

                    var node = target.tree('getSelected');
                    if(!node) {
                        $.messager.alert('提示','请选择一个添加节点!');
                        return;
                    }

                    menuNode.menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                });
            },
            bindOnClick: function(menuNode, target, callback) {
                menuNode.menu({"onClick" : function(item){
                    callback(target, item);
                }});
            }

        };

        return menu;
    }
);
