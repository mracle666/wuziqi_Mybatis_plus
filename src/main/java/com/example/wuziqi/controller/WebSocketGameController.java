package com.example.wuziqi.controller;

import com.example.wuziqi.dto.GameMessage;
import com.example.wuziqi.entity.GameRoom;
import com.example.wuziqi.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketGameController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketGameController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int BLACK = 1;
    private static final int WHITE = 2;

    @MessageMapping("/room.join")
    public void handleRoomJoin(Map<String, Object> payload) {
        Long roomId = Long.valueOf(payload.get("roomId").toString());
        String username = payload.get("username").toString();
        boolean isPlayer = Boolean.parseBoolean(payload.get("isPlayer").toString());

        GameRoom room = roomService.getRoom(roomId);
        if (room == null) return;

        if (!isPlayer) {
            roomService.addSpectator(roomId, username);
        } else if (room.getGuestPlayer() != null && room.getStatus() == GameRoom.STATUS_PLAYING) {
            // 对战玩家已满两人，广播START消息
            GameMessage startMsg = new GameMessage();
            startMsg.setType("START");
            startMsg.setMessage("游戏开始！双方已就位");
            startMsg.setGuestPlayer(room.getGuestPlayer());
            startMsg.setSpectatorCount((int) roomService.getSpectatorCount(roomId));
            if (room.getBoardData() != null) {
                startMsg.setBoardData(room.getBoardData());
            }
            messagingTemplate.convertAndSend("/topic/room/" + roomId, startMsg);
        }

        GameMessage message = new GameMessage();
        message.setType("JOIN");
        message.setUsername(username);
        message.setIsPlayer(isPlayer);
        message.setMessage(username + (isPlayer ? " 加入对战" : " 加入观战"));
        message.setSpectatorCount((int) roomService.getSpectatorCount(roomId));

        if (room.getBoardData() != null) {
            message.setBoardData(room.getBoardData());
        }

        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);

        GameMessage countMessage = new GameMessage();
        countMessage.setType("SPECTATOR_COUNT");
        countMessage.setSpectatorCount((int) roomService.getSpectatorCount(roomId));
        messagingTemplate.convertAndSend("/topic/room/" + roomId, countMessage);
    }

    @MessageMapping("/room.leave")
    public void handleRoomLeave(Map<String, Object> payload) {
        Long roomId = Long.valueOf(payload.get("roomId").toString());
        String username = payload.get("username").toString();
        boolean isPlayer = Boolean.parseBoolean(payload.get("isPlayer").toString());

        if (!isPlayer) {
            roomService.removeSpectator(roomId, username);
        } else {
            GameRoom roomBefore = roomService.getRoom(roomId);
            roomService.leaveRoom(roomId, username);
            GameRoom roomAfter = roomService.getRoom(roomId);

            if (roomAfter == null) {
                // 房间已被删除（房主退出或两人都退出）
                GameMessage closedMsg = new GameMessage();
                closedMsg.setType("ROOM_CLOSED");
                closedMsg.setMessage("房间已解散");
                messagingTemplate.convertAndSend("/topic/room/" + roomId, closedMsg);
                return;
            }

            if (roomBefore.getGuestPlayer() != null && roomAfter.getGuestPlayer() == null) {
                // 对手退出，房间回到等待状态
                GameMessage statusMsg = new GameMessage();
                statusMsg.setType("WAITING");
                statusMsg.setMessage("对手已离开，等待新玩家加入...");
                statusMsg.setRoomStatus(GameRoom.STATUS_WAITING);
                messagingTemplate.convertAndSend("/topic/room/" + roomId, statusMsg);
            }
        }

        GameMessage message = new GameMessage();
        message.setType("LEAVE");
        message.setUsername(username);
        message.setMessage(username + " 离开了房间");
        message.setSpectatorCount((int) roomService.getSpectatorCount(roomId));

        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    @MessageMapping("/room.move")
    public void handleRoomMove(Map<String, Object> payload) {
        Long roomId = Long.valueOf(payload.get("roomId").toString());
        String username = payload.get("username").toString();
        Integer row = Integer.valueOf(payload.get("row").toString());
        Integer col = Integer.valueOf(payload.get("col").toString());
        Integer player = Integer.valueOf(payload.get("player").toString());

        GameRoom room = roomService.getRoom(roomId);
        if (room == null || room.getStatus() != GameRoom.STATUS_PLAYING) {
            return;
        }

        try {
            String boardData = room.getBoardData();
            int[][] board;
            if (boardData == null || boardData.isEmpty()) {
                board = new int[15][15];
            } else {
                Map<String, Object> data = objectMapper.readValue(boardData, Map.class);
                board = convertToBoard((java.util.List<java.util.List<Integer>>) data.get("board"));
            }

            board[row][col] = player;

            boolean gameOver = checkWin(board, row, col, player);
            int winner = gameOver ? player : 0;
            int nextPlayer = player == BLACK ? WHITE : BLACK;

            String newBoardData = convertBoardToJson(board);
            roomService.updateRoomState(roomId, newBoardData, nextPlayer, gameOver, winner);

            GameMessage message = new GameMessage();
            message.setType("MOVE");
            message.setUsername(username);
            message.setRow(row);
            message.setCol(col);
            message.setPlayer(player);
            message.setBoardData(newBoardData);
            message.setGameOver(gameOver);
            message.setWinner(winner);
            message.setSpectatorCount((int) roomService.getSpectatorCount(roomId));

            messagingTemplate.convertAndSend("/topic/room/" + roomId, message);

        } catch (Exception e) {
            logger.error("处理落子消息失败", e);
        }
    }

    @MessageMapping("/room.reset")
    public void handleRoomReset(Map<String, Object> payload) {
        Long roomId = Long.valueOf(payload.get("roomId").toString());
        String username = payload.get("username").toString();

        try {
            String emptyBoard = roomService.getEmptyBoardJson();
            roomService.updateRoomState(roomId, emptyBoard, BLACK, false, 0);

            GameMessage message = new GameMessage();
            message.setType("RESET");
            message.setUsername(username);
            message.setMessage("游戏已重置");
            message.setSpectatorCount((int) roomService.getSpectatorCount(roomId));

            messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
        } catch (Exception e) {
            logger.error("重置游戏失败", e);
        }
    }

    private int[][] convertToBoard(java.util.List<java.util.List<Integer>> boardList) {
        int[][] board = new int[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = boardList.get(i).get(j);
            }
        }
        return board;
    }

    private String convertBoardToJson(int[][] board) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("boardSize", 15);
        data.put("board", board);
        return objectMapper.writeValueAsString(data);
    }

    private boolean checkWin(int[][] board, int row, int col, int player) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        
        for (int[] dir : directions) {
            int count = 1;
            count += countInDirection(board, row, col, dir[0], dir[1], player);
            count += countInDirection(board, row, col, -dir[0], -dir[1], player);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    private int countInDirection(int[][] board, int row, int col, int dRow, int dCol, int player) {
        int count = 0;
        int r = row + dRow;
        int c = col + dCol;
        
        while (r >= 0 && r < 15 && c >= 0 && c < 15 && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }
}
