package it.ing.pajc.data.movements;

import java.util.*;

/**
 * Tree of positions.
 *
 * @param <Position> Type of generic tree.
 */
public class GenericTree<Position> {
    private GenericTreeNode<Position> root;

    public GenericTreeNode<Position> getRoot() {
        return this.root;
    }

    /**
     * Set the new given root.
     *
     * @param root the given root.
     */
    public void setRoot(GenericTreeNode<Position> root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        int numberOfNodes = 0;
        if (root != null) {
            numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; //1 for the root!
        }
        return numberOfNodes;
    }

    /**
     * Returns the auxiliary number of nodes.
     *
     * @param node The given node.
     * @return the number of nodes.
     */
    private int auxiliaryGetNumberOfNodes(GenericTreeNode<Position> node) {
        int numberOfNodes = node.getNumberOfChildren();
        for (GenericTreeNode<Position> child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }
        return numberOfNodes;
    }

    public boolean exists(Position dataToFind) {
        return (find(dataToFind) != null);
    }

    private GenericTreeNode<Position> find(Position dataToFind) {
        GenericTreeNode<Position> returnNode = null;

        if (root != null) {
            returnNode = auxiliaryFind(root, dataToFind);
        }
        return returnNode;
    }

    private GenericTreeNode<Position> auxiliaryFind(GenericTreeNode<Position> currentNode, Position dataToFind) {
        GenericTreeNode<Position> returnNode = null;
        int i;
        if (currentNode.getData().equals(dataToFind)) {
            returnNode = currentNode;
        } else if (currentNode.hasChildren()) {
            i = 0;
            while (returnNode == null && i < currentNode.getNumberOfChildren()) {
                returnNode = auxiliaryFind(currentNode.getChildAt(i), dataToFind);
                i++;
            }
        }
        return returnNode;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * Build the tree list.
     *
     * @param traversalOrder order of the build.
     * @return the builted tree node list.
     */
    public List<GenericTreeNode<Position>> build(GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<Position>> returnList = null;
        if (root != null) {
            returnList = build(root, traversalOrder);
        }
        return returnList;
    }

    /**
     * Given a node builds and returns the list of tree nodes of positions.
     *
     * @param node           Given node.
     * @param traversalOrder Order used to build.
     * @return list of generic tree node.
     */
    private List<GenericTreeNode<Position>> build(GenericTreeNode<Position> node, GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<Position>> traversalResult = new ArrayList<>();

        if (traversalOrder == GenericTreeTraversalOrderEnum.PRE_ORDER) {
            buildPreOrder(node, traversalResult);
        } else if (traversalOrder == GenericTreeTraversalOrderEnum.POST_ORDER) {
            buildPostOrder(node, traversalResult);
        }

        return traversalResult;
    }

    /**
     * Build with pre order.
     *
     * @param node            Given node.
     * @param traversalResult Given list of generic tree node of position.
     */
    private void buildPreOrder(GenericTreeNode<Position> node, List<GenericTreeNode<Position>> traversalResult) {
        traversalResult.add(node);

        for (GenericTreeNode<Position> child : node.getChildren()) {
            buildPreOrder(child, traversalResult);
        }
    }

    /**
     * Build with post order.
     *
     * @param node            Given node.
     * @param traversalResult Given list of generic tree node of position.
     */
    private void buildPostOrder(GenericTreeNode<Position> node, List<GenericTreeNode<Position>> traversalResult) {
        for (GenericTreeNode<Position> child : node.getChildren()) {
            buildPostOrder(child, traversalResult);
        }

        traversalResult.add(node);
    }

    @Override
    public String toString() {
        /*
        We're going to assume a pre-order traversal by default
         */

        String stringRepresentation = "";

        if (root != null) {
            stringRepresentation = build(GenericTreeTraversalOrderEnum.PRE_ORDER).toString();

        }

        return stringRepresentation;
    }
}
