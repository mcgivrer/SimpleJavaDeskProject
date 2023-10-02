package com.snapgames.core.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node {

    private static int index = 0;
    int id = index++;

    public boolean active;
    private String name = "node_" + id;

    private Node parent;

    private List<Node> children = new CopyOnWriteArrayList<>();

    protected Node() {
        this.active = true;
    }

    protected Node(String name) {
        this();
        this.name = name;
    }

    public static int getIndex() {
        return index;
    }

    public void addChild(Node child) {
        children.add((Node) child);
        child.setParent(this);
    }


    public void setParent(Node node) {
        this.parent = parent;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public <T extends Node> Collection<T> getChild() {
        return (Collection<T>) children;
    }


    public <T extends Node> T getChildNode(String name) {
        return (T) getChild().stream().filter(e -> e.getName().equals(name)).findFirst().get();
    }


    public String treeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName() + "\n");
        for (Node n : getChild()) {
            sb.append("\t|_" + n.treeToString() + "\n");
        }
        return sb.toString();
    }


    public int getId() {
        return id;
    }
}
