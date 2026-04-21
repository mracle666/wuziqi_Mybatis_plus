package com.example.wuziqi.dto;

import com.example.wuziqi.model.BoardState;

public class MoveResponse {
    private BoardState boardState;
    private boolean success;
    private String message;
    private boolean gameOver;
    private int winner;

    public MoveResponse() {
    }

    public MoveResponse(BoardState boardState, boolean success, String message, boolean gameOver, int winner) {
        this.boardState = boardState;
        this.success = success;
        this.message = message;
        this.gameOver = gameOver;
        this.winner = winner;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
