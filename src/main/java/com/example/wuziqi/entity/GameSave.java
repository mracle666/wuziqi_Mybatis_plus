package com.example.wuziqi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("game_save")
public class GameSave {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String saveName;
    private String boardData;
    private int currentPlayer;
    private boolean gameOver;
    private int winner;
    private LocalDateTime createTime;

    public GameSave() {
    }

    public GameSave(String saveName, String boardData, int currentPlayer, boolean gameOver, int winner) {
        this.saveName = saveName;
        this.boardData = boardData;
        this.currentPlayer = currentPlayer;
        this.gameOver = gameOver;
        this.winner = winner;
        this.createTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getBoardData() {
        return boardData;
    }

    public void setBoardData(String boardData) {
        this.boardData = boardData;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
