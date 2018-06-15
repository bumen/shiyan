/**
 *
 * Created by Administrator on 2017/8/2.
 */
define(
    "ui/easyui.accordion.js",
    [],
    function(require) {

        var accordion = {
            "addAccordion": function(target, accordionInfo) {
                var accordion = {
                    'title' : accordionInfo.title,
                    'iconCls' : 'icon-ok',
                    'selected':accordionInfo.selected,
                    'content': accordionInfo.content
                };

                target.accordion('add', accordion);
            },
            getPanel: function(target, title) {
                return target.accordion('getPanel', title);
            }

        };
        return accordion;
    }
);
