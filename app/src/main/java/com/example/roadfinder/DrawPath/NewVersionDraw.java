package com.example.roadfinder.DrawPath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.roadfinder.GameLogic.AStarPathFinder;
import com.example.roadfinder.GameLogic.Node;

import java.util.List;

public class NewVersionDraw extends View {
    private int row;
    private int colum;
    private int cellWidth;
    private Paint areaPaint = new Paint();
    private Paint pathPaint = new Paint();
    private Node[][] searchArea;
    private int[][] blokesArray;
    private List<Node> bestPathList;
    private AStarPathFinder aStarPathFinder;
    private boolean isSelectedStart =false;
    private boolean isSelectedFinish =false;

    public NewVersionDraw(Context context) {
        super(context, null);
    }

    public NewVersionDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        areaPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setBoardParams(int row, int colums) {
        this.row = row;
        this.colum = colums;
        aStarPathFinder = new AStarPathFinder(row, colums);
        searchArea = new Node[row][colums];
        blokesArray = new int[row][colums];
        calculateBoardSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateBoardSize();
    }

    private void calculateBoardSize() {
        if (row < 1 || colum < 1) {
            return;
        }
        cellWidth = getWidth() / colum;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (row == 0 || colum == 0) {
            return;
        }
        int wigth = getWidth();
        int height = getWidth();

        for (int i = 0; i < colum; i++) {
            for (int j = 0; j < row; j++) {
                if (searchArea[i][j] != null) {

                    canvas.drawRect(i * cellWidth, j * cellWidth,
                            (i + 1) * cellWidth, (j + 1) * cellWidth, pathPaint);
                }
            }
        }

        for (int i = 1; i < colum; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, areaPaint);

        }
        for (int i = 1; i < row; i++) {
            canvas.drawLine(0, i * cellWidth, wigth, i * cellWidth, areaPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int row = (int) (event.getX() / cellWidth);
        int colum = (int) (event.getY() / cellWidth);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (isSelectedStart) {
                Node node = new Node(row, colum);
                searchArea[row][colum] = node;
                aStarPathFinder.setSTART_NODE(node);
                isSelectedStart=false;
                invalidate();

            }

            if (isSelectedFinish) {
                Node node = new Node(row, colum);
                searchArea[row][colum] = node;
                aStarPathFinder.setFINISH_NODE(node);
                aStarPathFinder.setNodes();
                aStarPathFinder.setBlocks(blokesArray);
                bestPathList = aStarPathFinder.searchPath();
                PathFinderThread pathFinderThread = new PathFinderThread();
                Thread thread = new Thread(pathFinderThread);
                thread.start();
                isSelectedFinish=false;
                invalidate();
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (event.getY() > 0 && event.getX() > 0 && event.getX() < getHeight() - 1 && event.getY() < getHeight() && event.getX() < getWidth() - 1 && event.getY() < getWidth()) {
                if (searchArea[row][colum] == null) {
                    Node node = new Node(row, colum);
                    searchArea[row][colum] = node;
                    blokesArray[row][colum] = 1;
                    invalidate();
                }
            }
        }
        invalidate();
        return true;
    }
    public void setSelectedStart(boolean selectedStart) {
        isSelectedStart = selectedStart;
    }

    public void setSelectedFinish(boolean selectedFinish) {
        isSelectedFinish = selectedFinish;
    }

    class PathFinderThread implements Runnable {

        @Override
        public void run() {
            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (Node nodes : bestPathList) {

                        invalidate();
                        int rows = nodes.getRow();
                        int colums = nodes.getCol();
                        searchArea[rows][colums] = nodes;

                    }
                }
            });
        }
    }
}

