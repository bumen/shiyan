/**
 * Created by Administrator on 2017/8/2.
 */
define(
    "ui/easyui.tab.js",
    [],
    function(require) {
        var tab = {
            "addTab" : function(node, info) {
                var _info = {
                    "title": info.title,
                    content:'<iframe style="width:100%;height:100%"  src="'+ info.url +'" frameborder="0"  scrolling="yes"></iframe>',
                    closable: true
                };
                node.tabs('add',_info);
            },
            "selectTab" : function(node, title) {
                node.tabs("select", title);
            },
            "existsTab": function(node, title) {
               return node.tabs("exists", title);
            },
            "close" : function(target, title) {
                target.tabs('close', title);
            }
        };

        return tab;
    }
);