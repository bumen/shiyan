/**
 * Created by Administrator on 2017/8/2.
 */
define(
    "hello/hello.explorer.js",
    [
        "ui/easyui.accordion.js",
        "ui/easyui.tree.js",
        "ui/easyui.rightmenu.js",
        "ui/easyui.tab.js",
        "ui/easyui.dialog.js",
        "ui/easyui.form.js",
        "util/util.reg.js"
    ],
    function(require) {
        var accordion = require("ui/easyui.accordion.js");
        var tree = require("ui/easyui.tree.js");
        var rightMenu = require("ui/easyui.rightmenu.js");
        var jobTab = require("ui/easyui.tab.js");
        var dialog = require("ui/easyui.dialog.js");
        var form = require("ui/easyui.form.js");
        var reg = require('util/util.reg.js');

        var explorer = {
            accordionEle: $('#ac'),
            mmEle: $("#mm"),
            jobTabEle: $("#job_tabs"),
            addDialog: $("#addDialog"),
            addDialogForm: $('#addDialogForm'),
            explorerEle: $("#explorer"),
            addDialogTarget: undefined,
            addDialogFormName: $("#form_name"),
            addDialogFormUrl : $("#form_url"),
            "init": function() {
                var accordionsStr = this.explorerEle.val();
                if(accordionsStr) {
                    var accordions = JSON.parse(accordionsStr);
                    for(var i in accordions) {
                        var accordionInfo = accordions[i];
                        if(accordionInfo.id) {
                            accordion.addAccordion(this.accordionEle, accordionInfo);

                            if(accordionInfo.contentType == "tree") {
                                var info = {
                                    "children": accordionInfo.children,
                                    "onClick" : this.treeBranchOnClickEvent
                                };
                                tree.create(accordionInfo.contentId, info);
                            }

                            if(accordionInfo.type == "link") {
                                var panel = accordion.getPanel(this.accordionEle, accordionInfo.title);
                                if(panel) {
                                    var rightMenuTarget = $("#" + accordionInfo.contentId);
                                    rightMenu.bindContextMenu(this.mmEle, rightMenuTarget);
                                    rightMenu.bindOnClick(this.mmEle, rightMenuTarget, this.rightMenuOnClick);
                                }
                            }
                        }
                    }

                    dialog.create(this.addDialog, {
                        width:400,
                        height:190,
                        title:"链接",
                        buttons:[{
                            text:'提交',
                            iconCls:'icon-ok',
                            handler:function(){
                                explorer.addDialogSubmitEvent();
                            }
                        },{
                            text:'关闭',
                            iconCls:'icon-cancel',
                            handler:function(){
                                dialog.close(explorer.addDialog);
                            }
                        }],
                        onClose:function(){
                            explorer.addDialogTarget = undefined;
                            form.clear(explorer.addDialogForm);
                        }
                    });
                } else {
                    console.warn("没有任何权限，找管理员吧");
                }
            },
            "treeBranchOnClickEvent" : function(tid, node) {
                var isUrl = tree.isUrl(node);
                if(isUrl) {
                    var title = node.text;
                    var exists = jobTab.existsTab(explorer.jobTabEle, title);
                    if(exists) {
                        jobTab.selectTab(explorer.jobTabEle, title);
                    } else {
                        var url = node.attributes.url;
                        var info = {
                            "title": title,
                            "url": url
                        };
                        jobTab.addTab(explorer.jobTabEle, info);
                    }
                }
            },
            rightMenuOnClick: function(target, item) {
                if(item.name == "new"){
                    var isFolder = tree.isSelectedFolder(target);
                    if(!isFolder) {
                        $.messager.alert('提示','该节点不是文件夹!');
                        return;
                    }

                    explorer.addDialogTarget = target;
                    dialog.open(explorer.addDialog);
                } else if(item.name == "del") {

                   tree.removeSelected(target, function(type, title){
                        if(type && type == "invalid") {
                               return 0;
                        }
                        jobTab.close(explorer.jobTabEle, title);
                        return 1;
                    });
                }
            },
            addDialogSubmitEvent: function() {
                var name = explorer.addDialogFormName.textbox('getValue');
                if(reg.isEmpty(name)) {
                    return;
                }
                var url = explorer.addDialogFormUrl.textbox('getValue');
                var type = "url";
                if(reg.isEmpty(url)) {
                    type = "folder";
                } else if(!reg.isUrl(url)){
                    return;
                }

                tree.appendSelected(explorer.addDialogTarget, {
                    text: name,
                    attributes :{
                        type: type,
                        url : url
                    }
                });

                dialog.close(explorer.addDialog);
            }
        };

        return explorer;
    }
);