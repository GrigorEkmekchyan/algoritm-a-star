package com.example.roadfinder.GameLogic;

import android.graphics.Color;

import androidx.annotation.Nullable;

public class Node {
    private int row;
    private int col;
    private int gCost;
    private int hCost;
    private int fCost;
    private boolean isWalkable;
    private Node fromNode;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    public void calculateHeuristic(Node finalNode) {
        this.hCost = Math.abs(finalNode.getRow() - getRow() + Math.abs(finalNode.getCol() - getCol()));
    }

    public void setNodeData(Node currentNode, int cost) {

        int gCost = currentNode.getGCost() + cost;
        setFromNode(currentNode);
        setgCost(gCost);
        calculateFinalCost();

    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getGCost() + cost;
        if (gCost < getGCost()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;

    }

    private void calculateFinalCost() {
        fCost = gCost + hCost;
    }

    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Node other = (Node) obj;

        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    public int getGCost() {
        return gCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }


}

