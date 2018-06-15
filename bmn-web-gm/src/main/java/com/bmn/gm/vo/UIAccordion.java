package com.bmn.gm.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * jquery easyui accordion attributes
 * Created by Administrator on 2017/7/31.
 */
public class UIAccordion implements Comparable<UIAccordion>, Component{
    private int id;
    private String title;
    private boolean selected;
    private String content;

    private int order;
    private String type;
    private String contentType;     //tree: 树
    private String contentId;
    private List<Component> children;

    public UIAccordion(int id) {
        this.id = id;
        this.type = "default";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Override
    public int compareTo(UIAccordion o) {
        return order - o.getOrder() >= 0 ? 1 : -1;
    }

    @Override
    public List<Component> getChildren() {
        return children;
    }

   @Override
    public void setChildren(List<Component> children) {
        this.children = children;
    }

    @Override
    public Component clone() throws CloneNotSupportedException {
        Component clone = (Component)super.clone();
        List<Component> result = new ArrayList<>(children.size());
        for(Component component : children) {
            result.add(component.clone());
        }
        clone.setChildren(result);
        return clone;
    }

    @Override
    public void filter(ComponentFilter filter) {

    }
}
