package com.bmn.bean;

public class BranchBean extends TreeBean{

    private int size;

    private LeafBean leaf;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LeafBean getLeaf() {
        return leaf;
    }

    public void setLeaf(LeafBean leaf) {
        this.leaf = leaf;
    }
}
