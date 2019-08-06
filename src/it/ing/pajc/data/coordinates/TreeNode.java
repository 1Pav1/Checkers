package it.ing.pajc.data.coordinates;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T>{
    private T data = null;
    private List<TreeNode> children = new ArrayList<>();
    private TreeNode parent = null;

    public TreeNode(T data) {
        this.data = data;
    }

   /* public void addChild(TreeNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }*/

    /**
     * Called from addNewChild to set parent and add to list of children.
     * @param child
     */
    private void addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Create a new TreeNode child and calls addChild.
     * @param data
     */
    public void addNewChild(T data) {
        TreeNode<T> newChild = new TreeNode<>(data);
        this.addChild(newChild);
    }

    /**
     * Adds children in the ArrayList of TreeNode
     * @param children
     */
    public void addChildren(List<TreeNode> children) {
        for(TreeNode t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }
}
