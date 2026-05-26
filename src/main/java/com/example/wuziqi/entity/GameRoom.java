package com.example.wuziqi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("game_room")
public class GameRoom {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String roomName;
    private String hostPlayer;
    private String guestPlayer;
    private Integer status;
    private String boardData;
    private Integer currentPlayer;
    private Boolean gameOver;
    private Integer winner;
    private LocalDateTime createTime;

    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_FINISHED = 2;

    public GameRoom() {}

    public GameRoom(String roomName, String hostPlayer) {
        this.roomName = roomName;
        this.hostPlayer = hostPlayer;
        this.status = STATUS_WAITING;
        this.currentPlayer = 1;
        this.gameOver = false;
        this.winner = 0;
        this.createTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHostPlayer() {
        return hostPlayer;
    }

    public void setHostPlayer(String hostPlayer) {
        this.hostPlayer = hostPlayer;
    }

    public String getGuestPlayer() {
        return guestPlayer;
    }

    public void setGuestPlayer(String guestPlayer) {
        this.guestPlayer = guestPlayer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBoardData() {
        return boardData;
    }

    public void setBoardData(String boardData) {
        this.boardData = boardData;
    }

    public Integer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Integer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
