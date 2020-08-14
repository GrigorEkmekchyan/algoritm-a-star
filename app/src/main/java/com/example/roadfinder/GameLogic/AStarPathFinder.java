package com.example.roadfinder.GameLogic;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathFinder {

    private final static int HORIZONTAL_VERTICAL_COST = 10;
    private final static int DIAGONAL_COST = 14;
    private Node START_NODE;
    private Node FINISH_NODE;
    private Node[][] searchArea;
    private PriorityQueue<Node> openList;
    private Set<Node> checkedSet;

    public AStarPathFinder(int x, int y) {

        searchArea = new Node[x][y];
        openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                return Integer.compare(node.getFCost(), t1.getFCost());
            }
        });
        this.checkedSet = new HashSet<>();
    }


    public void setNodes() {
        for (int i = 0; i < searchArea.length; i++) {
            for (int j = 0; j < searchArea[0].length; j++) {
                Node node = new Node(i, j);
                node.calculateHeuristic(FINISH_NODE);
                this.searchArea[i][j] = node;
            }
        }
    }

    public void setBlocks(int[][] blocksArray) {
        if (blocksArray != null)
            for (int i = 0; i < blocksArray.length - 1; i++) {
                for (int j = 0; j < blocksArray[0].length - 1; j++) {
                    if (blocksArray[i][j] == 1) {
                        setBlock(i, j);
                    }
                }
            }
    }

    public List<Node> searchPath() {

        openList.add(START_NODE);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            checkedSet.add(currentNode);
            if (currentNode != null)
                if (checkIsFinalNode(currentNode)) {
                    return getBestPath(currentNode);

                } else checkNeighbor(currentNode);
        }
        return new ArrayList<>();

    }

    private List<Node> getBestPath(Node currentNode) {

        List<Node> path = new ArrayList<>();
        path.add(currentNode);
        Node cameFrom;
        while ((cameFrom = currentNode.getFromNode()) != null) {
            path.add(0, cameFrom);
            currentNode = cameFrom;
        }
        return path;
    }


    private void checkNeighbor(Node currentNode) {
        checkLeftDGNeighbor(currentNode);
        checkRightDGNeighbor(currentNode);
        checkUpDownNeighbor(currentNode);
    }

    private void checkRightDGNeighbor(Node currentNode) {

        int colum = currentNode.getCol();
        int row = currentNode.getRow();
        int lowerRow = colum + 1;
        if (lowerRow < getSearchArea().length ) {
            if (row - 1 >= 0) {
                checkNode(currentNode, row - 1, lowerRow, DIAGONAL_COST);
            }
            if (row + 1 < getSearchArea()[0].length - 1) {
                checkNode(currentNode, row + 1, lowerRow, DIAGONAL_COST);
            }
            checkNode(currentNode, row, lowerRow, HORIZONTAL_VERTICAL_COST);
        }
    }


    private void checkUpDownNeighbor(Node currentNode) {

        int colum = currentNode.getCol();
        int row = currentNode.getRow();
        if (row - 1 >= 0) {
            checkNode(currentNode, row - 1, colum, HORIZONTAL_VERTICAL_COST);
        }
        if (row + 1 < getSearchArea()[0].length - 1) {
            checkNode(currentNode, row + 1, colum, HORIZONTAL_VERTICAL_COST);
        }
    }

    private void checkLeftDGNeighbor(Node currentNode) {

        int col = currentNode.getCol();
        int row = currentNode.getRow();
        int upperRow = col - 1;
        if (upperRow >= 0) {
            if (row - 1 >= 0) {
                checkNode(currentNode, row - 1, upperRow, DIAGONAL_COST);
            }
            if (row + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, row + 1, upperRow, DIAGONAL_COST);
            }
            checkNode(currentNode, row, upperRow, HORIZONTAL_VERTICAL_COST);
        }
    }

    private void checkNode(Node currentNode, int x, int y, int cost) {

        Node neighborNode = this.searchArea[x][y];
        if (!neighborNode.isWalkable() && !checkedSet.contains(neighborNode)) {
            if (!openList.contains(neighborNode)) {
                neighborNode.setNodeData(currentNode, cost);
                openList.add(neighborNode);

            } else {
                boolean changed = neighborNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    openList.remove(neighborNode);
                    openList.add(neighborNode);
                }
            }
        }
    }


    private boolean checkIsFinalNode(Node currentNode) {
        return currentNode.equals(FINISH_NODE);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int row, int col) {
        this.searchArea[row][col].setWalkable(true);
    }


    public Node[][] getSearchArea() {
        return searchArea;
    }

    public void setSTART_NODE(Node START_NODE) {
        this.START_NODE = START_NODE;
    }

    public void setFINISH_NODE(Node FINISH_NODE) {
        this.FINISH_NODE = FINISH_NODE;
    }

}
