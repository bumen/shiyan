package com.bmn.gm.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jquery esayui tree attributes
 * Created by Administrator on 2017/7/31.
 */
public class UIBranch implements Component{
    private int id;
    private String text;	//显示节点文本
    private List<Component> children;	//一个节点数组声明了若干节点
    private String state;	//'open' 或 'closed'
    private boolean checked;	//表示该节点是否被选中
    private Map<String, Object> attributes;	//被添加到节点的自定义属性

    public UIBranch() {
        this.state = "open";
        this.checked = false;
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public List<Component> getChildren() {
        return children;
    }


    @Override
    public void setChildren(List<Component> children) {
        this.children = children;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setAttributeUrl(String url) {
        this.attributes.put("url", url);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        Object o = attributes.get("type");
        if(o == null) {
            return "";
        }
        return (String)o;
    }

    public void setAttributeType(String type) {
        attributes.put("type", type);
    }

    @Override
    public Component clone() throws CloneNotSupportedException {
        Component clone = (Component)super.clone();
        List<Component> result = new ArrayList<>(children.size());
        for (Component child : children) {
            result.add(child.clone());
        }
        clone.setChildren(result);
        return clone;
    }

    @Override
    public void filter(ComponentFilter filter) {
        filter.doFilter(children);
    }
}
