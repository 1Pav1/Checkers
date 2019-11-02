package it.ing.pajc.data.movements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tree of node.
 *
 * @param <Position>
 */
public class GenericTreeNode<Position> {
    private Position data;
    private List<GenericTreeNode<Position>> children;
    private GenericTreeNode<Position> parent;

    /**
     * Constructor that creates children.
     */
    public GenericTreeNode() {
        children = new ArrayList<>();
    }

    /**
     * Constructor sets data to a given position.
     *
     * @param data position.
     */
    public GenericTreeNode(Position data) {
        this();
        setData(data);
    }

    public GenericTreeNode<Position> getParent() {
        return this.parent;
    }

    /**
     * Getter of children.
     *
     * @return children of position.
     */
    List<GenericTreeNode<Position>> getChildren() {
        return this.children;
    }

    /**
     * Getter number of children.
     *
     * @return Children tree size.
     */
    public int getNumberOfChildren() {
        return getChildren().size();
    }

    /**
     * Checks if it has children.
     *
     * @return True if it has children.
     */
    boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }

    public void setChildren(List<GenericTreeNode<Position>> children) {
        for (GenericTreeNode<Position> child : children) {
            child.parent = this;
        }
        this.children = children;
    }

    /**
     * Adds a child.
     *
     * @param child To add.
     */
    public void addChild(GenericTreeNode<Position> child) {
        child.parent = this;
        children.add(child);
    }

    public void addChildAt(int index, GenericTreeNode<Position> child) throws IndexOutOfBoundsException {
        child.parent = this;
        children.add(index, child);
    }

    /**
     * Removes children.
     */
    public void removeChildren() {
        this.children = new ArrayList<>();
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    /**
     * Getter of child at a given position of the node.
     *
     * @param index Position in the node.
     * @return the requested child.
     * @throws IndexOutOfBoundsException If it exceeds the bounds.
     */
    public GenericTreeNode<Position> getChildAt(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }

    //added
    public Set<GenericTreeNode<Position>> getAllLeafNodes() {
        Set<GenericTreeNode<Position>> leafNodes = new HashSet<>();
        if (this.children.isEmpty()) {
            leafNodes.add(this);
        } else {
            for (GenericTreeNode<Position> child : this.children) {
                leafNodes.addAll(child.getAllLeafNodes());
            }
        }
        return leafNodes;
    }

    /**
     * Getter of data.
     *
     * @return The requested position.
     */
    public Position getData() {
        return this.data;
    }

    /**
     * Setter of data.
     *
     * @param data that you want to set.
     */
    public void setData(Position data) {
        this.data = data;
    }

    /**
     * Converts the data to a string form.
     *
     * @return the requested string.
     */
    public String toString() {
        return getData().toString();
    }

    /**
     * Checks if the given object is equal to another one.
     *
     * @param obj the object used to check.
     * @return true if it is equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) return false;
        if (getClass() != obj.getClass()) {
            return false;
        }
        GenericTreeNode<?> other = (GenericTreeNode<?>) obj;
        if (data == null) {
            return other.data == null;
        } else return data.equals(other.data);
    }

    /**
     * Transforms the data in to hash code.
     *
     * @return the calculated result.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }
}