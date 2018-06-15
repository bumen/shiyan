/**
 * Created by Administrator on 2017/8/2.
 */

define(
    "ui/easyui.tree.js",
    [],
    function(require) {
        var tree = {

            "dom" : function(id) {
                return $("#" + id);
            },
            "create": function(tid, info) {
                var _info = {
                    data: info.children,
                    onClick: function(node) {
                        info.onClick(this.id, node);
                    }
                };
                this.dom(tid).tree(_info);
            },
            "isLeaf" : function(tid, branch) {
                return this.dom(tid).tree('isLeaf', branch);
            },
            "getSelected": function(target) {
                return target.tree("getSelected");
            },
            "appendSelected" : function(target, info) {
                var selected = this.getSelected(target);
                if(!selected) {
                    return;
                }
                target.tree('append', {
                    parent: selected.target,
                    data: [{
                        text: info.text,
                        attributes: info.attributes
                    }]
                });
            },
            "removeSelected" : function(target, callback) {
                var selected = this.getSelected(target);
                if(!selected) {
                    return;
                }

                if(callback(selected.attributes.type, selected.text)) {
                    target.tree('remove', selected.target);
                }
            },
            "isSelectedFolder": function(target) {
                var selected = this.getSelected(target);
                if(!selected) {
                    return 0;
                }

                return selected.attributes.type != "url";
            },
            "isUrl" : function(branch) {
                if(!branch.attributes.type) {
                    return 0;
                }

                if(branch.attributes.type == "url") {
                    return 1;
                }
                return 0;
            }

        };
        return tree;
    }
);