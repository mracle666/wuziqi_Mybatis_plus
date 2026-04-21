package com.example.wuziqi.model;

public class Cell {
    private int row;
    private int col;
    private int state;
    private boolean starPoint;

    public Cell(int row, int col, int state, boolean starPoint) {
        this.row = row;
        this.col = col;
        this.state = state;
        this.starPoint = starPoint;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isStarPoint() {
        return starPoint;
    }

    public void setStarPoint(boolean starPoint) {
        this.starPoint = starPoint;
    }
}
