package com.example.wuziqi.model;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
    private static final int BOARD_SIZE = 15;
    private static final int EMPTY = 0;
    private static final int BLACK = 1;
    private static final int WHITE = 2;
    private static final int[][] STAR_POINTS = {
            {3, 3}, {3, 7}, {3, 11},
            {7, 3}, {7, 7}, {7, 11},
            {11, 3}, {11, 7}, {11, 11}
    };

    private List<List<Cell>> board;
    private int currentPlayer;
    private int boardSize;
    private boolean gameOver;
    private int winner;

    public BoardState() {
        this.boardSize = BOARD_SIZE;
        this.currentPlayer = BLACK;
        this.board = new ArrayList<>();
        this.gameOver = false;
        this.winner = EMPTY;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                boolean isStarPoint = isStarPoint(i, j);
                row.add(new Cell(i, j, EMPTY, isStarPoint));
            }
            this.board.add(row);
        }
    }

    private boolean isStarPoint(int row, int col) {
        for (int[] point : STAR_POINTS) {
            if (point[0] == row && point[1] == col) {
                return true;
            }
        }
        return false;
    }

    public boolean makeMove(int row, int col, int player) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        if (player != currentPlayer) {
            return false;
        }
        if (gameOver) {
            return false;
        }
        if (board.get(row).get(col).getState() != EMPTY) {
            return false;
        }

        board.get(row).get(col).setState(player);

        if (checkWin(row, col, player)) {
            gameOver = true;
            winner = player;
        } else {
            currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
        }

        return true;
    }

    private boolean checkWin(int row, int col, int player) {
        int[][][] directions = {
                {{0, 1}, {0, -1}},
                {{1, 0}, {-1, 0}},
                {{1, 1}, {-1, -1}},
                {{1, -1}, {-1, 1}}
        };

        for (int[][] direction : directions) {
            int count = 1;

            count += countInDirection(row, col, direction[0][0], direction[0][1], player);
            count += countInDirection(row, col, direction[1][0], direction[1][1], player);

            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    private int countInDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 0;
        int r = row + dRow;
        int c = col + dCol;

        while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board.get(r).get(c).getState() == player) {
            count++;
            r += dRow;
            c += dCol;
        }

        return count;
    }

    public List<List<Cell>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Cell>> board) {
        this.board = board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
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
