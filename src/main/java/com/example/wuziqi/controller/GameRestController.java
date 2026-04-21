package com.example.wuziqi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameRestController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "欢迎使用五子棋游戏!");
        result.put("status", "success");
        return result;
    }

    @GetMapping("/game/info")
    public Map<String, Object> getGameInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "五子棋");
        info.put("version", "1.0.0");
        info.put("boardSize", 15);
        info.put("rules", "五子连珠获胜");
        return info;
    }

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("test", "REST控制器测试成功");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
