package com.snapgames.core.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node {

    private static int index = 0;
    int id = index++;

    public boolean active;
    private String name = "node_" + id;

    private Node parent;

    private List<Node> children = new ArrayList<>();

    protected Node() {
        this.active = true;
    }

    protected Node(String name) {
        this();
        this.name = name;
    }

    public void addChild(Node child) {
        children.add((Node)child);
        child.setParent(this);
    }

    public void setParent(Node node) {
        this.parent = parent;
    }

    public static int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Collection<Node> getChild() {
        return children;
    }
}
