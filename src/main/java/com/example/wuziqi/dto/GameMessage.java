package com.example.wuziqi.dto;

public class GameMessage {
    
    private String type;
    private String username;
    private Integer row;
    private Integer col;
    private Integer player;
    private String boardData;
    private Boolean gameOver;
    private Integer winner;
    private String message;
    private Integer spectatorCount;

    private Boolean isPlayer;
    private String guestPlayer;
    private Integer roomStatus;

    public GameMessage() {}

    public GameMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }

    public String getBoardData() {
        return boardData;
    }

    public void setBoardData(String boardData) {
        this.boardData = boardData;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSpectatorCount() {
        return spectatorCount;
    }

    public void setSpectatorCount(Integer spectatorCount) {
        this.spectatorCount = spectatorCount;
    }

    public Boolean getIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(Boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public String getGuestPlayer() {
        return guestPlayer;
    }

    public void setGuestPlayer(String guestPlayer) {
        this.guestPlayer = guestPlayer;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }
}
