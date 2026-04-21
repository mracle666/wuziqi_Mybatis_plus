package com.example.wuziqi.dto;

public class MoveRequest {
    private int row;
    private int col;
    private int player;

    public MoveRequest() {
    }

    public MoveRequest(int row, int col, int player) {
        this.row = row;
        this.col = col;
        this.player = player;
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

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
