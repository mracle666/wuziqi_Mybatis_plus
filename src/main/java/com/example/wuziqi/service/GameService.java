package com.example.wuziqi.service;

import com.example.wuziqi.dto.MoveRequest;
import com.example.wuziqi.dto.MoveResponse;
import com.example.wuziqi.entity.GameSave;
import com.example.wuziqi.mapper.GameSaveMapper;
import com.example.wuziqi.model.BoardState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GameService {

    private BoardState boardState;

    @Autowired
    private GameSaveMapper gameSaveMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public GameService() {
        this.boardState = new BoardState();
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public MoveResponse makeMove(MoveRequest request) {
        MoveResponse response = new MoveResponse();

        boolean success = boardState.makeMove(request.getRow(), request.getCol(), request.getPlayer());

        if (success) {
            response.setSuccess(true);
            response.setMessage("落子成功");
        } else {
            response.setSuccess(false);
            response.setMessage("落子失败，请检查位置或游戏状态");
        }

        response.setBoardState(boardState);
        response.setGameOver(boardState.isGameOver());
        response.setWinner(boardState.getWinner());

        return response;
    }

    public void resetGame() {
        this.boardState = new BoardState();
    }

    public Long saveGame(String saveName) {
        try {
            String boardData = convertBoardToJson(boardState);
            GameSave gameSave = new GameSave(
                    saveName,
                    boardData,
                    boardState.getCurrentPlayer(),
                    boardState.isGameOver(),
                    boardState.getWinner()
            );
            gameSave.setCreateTime(LocalDateTime.now());
            gameSaveMapper.insert(gameSave);
            return gameSave.getId();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("保存游戏失败", e);
        }
    }

    public boolean loadGame(Long saveId) {
        try {
            GameSave gameSave = gameSaveMapper.selectById(saveId);
            if (gameSave == null) {
                return false;
            }

            this.boardState = convertJsonToBoard(gameSave.getBoardData());
            this.boardState.setCurrentPlayer(gameSave.getCurrentPlayer());
            this.boardState.setGameOver(gameSave.isGameOver());
            this.boardState.setWinner(gameSave.getWinner());
            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("加载游戏失败", e);
        }
    }

    public List<GameSave> getAllSaves() {
        return gameSaveMapper.findAllOrderByCreateTimeDesc();
    }

    public boolean deleteSave(Long saveId) {
        if (gameSaveMapper.selectById(saveId) != null) {
            gameSaveMapper.deleteById(saveId);
            return true;
        }
        return false;
    }

    private String convertBoardToJson(BoardState boardState) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("boardSize", boardState.getBoardSize());

        int[][] boardArray = new int[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                boardArray[i][j] = boardState.getBoard().get(i).get(j).getState();
            }
        }
        data.put("board", boardArray);

        return objectMapper.writeValueAsString(data);
    }

    private BoardState convertJsonToBoard(String json) throws JsonProcessingException {
        Map<String, Object> data = objectMapper.readValue(json, Map.class);
        BoardState boardState = new BoardState();

        List<List<Integer>> boardData = (List<List<Integer>>) data.get("board");
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int state = boardData.get(i).get(j);
                boardState.getBoard().get(i).get(j).setState(state);
            }
        }

        return boardState;
    }
}
