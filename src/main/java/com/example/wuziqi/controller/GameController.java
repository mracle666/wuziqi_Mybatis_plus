package com.example.wuziqi.controller;

import com.example.wuziqi.dto.MoveRequest;
import com.example.wuziqi.dto.MoveResponse;
import com.example.wuziqi.entity.GameSave;
import com.example.wuziqi.model.BoardState;
import com.example.wuziqi.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/game")
    public String gamePage(Model model) {
        BoardState boardState = gameService.getBoardState();
        model.addAttribute("boardState", boardState);
        return "wuziqi";
    }

    @PostMapping("/api/move")
    @ResponseBody
    public MoveResponse makeMove(@RequestBody MoveRequest request) {
        return gameService.makeMove(request);
    }

    @PostMapping("/api/restart")
    @ResponseBody
    public BoardState restartGame() {
        gameService.resetGame();
        return gameService.getBoardState();
    }

    @GetMapping("/api/state")
    @ResponseBody
    public BoardState getState() {
        return gameService.getBoardState();
    }

    @PostMapping("/api/save")
    @ResponseBody
    public Map<String, Object> saveGame(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String saveName = request.get("saveName");
            if (saveName == null || saveName.trim().isEmpty()) {
                saveName = "存档_" + System.currentTimeMillis();
            }
            Long saveId = gameService.saveGame(saveName);
            response.put("success", true);
            response.put("message", "游戏保存成功");
            response.put("saveId", saveId);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/saves")
    @ResponseBody
    public List<GameSave> getAllSaves() {
        return gameService.getAllSaves();
    }

    @PostMapping("/api/load/{saveId}")
    @ResponseBody
    public Map<String, Object> loadGame(@PathVariable Long saveId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = gameService.loadGame(saveId);
            if (success) {
                response.put("success", true);
                response.put("message", "游戏加载成功");
                response.put("boardState", gameService.getBoardState());
            } else {
                response.put("success", false);
                response.put("message", "存档不存在");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "加载失败: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/api/save/{saveId}")
    @ResponseBody
    public Map<String, Object> deleteSave(@PathVariable Long saveId) {
        Map<String, Object> response = new HashMap<>();
        boolean success = gameService.deleteSave(saveId);
        response.put("success", success);
        response.put("message", success ? "存档删除成功" : "存档不存在");
        return response;
    }

    @GetMapping("/")
    public String redirectToGame() {
        return "redirect:/game";
    }
}
