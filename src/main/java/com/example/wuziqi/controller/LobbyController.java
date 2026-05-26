package com.example.wuziqi.controller;

import com.example.wuziqi.entity.GameRoom;
import com.example.wuziqi.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LobbyController {

    @Autowired
    private RoomService roomService;

    private String getUsername(HttpServletRequest request) {
        String user = request.getRemoteUser();
        return user != null ? user : "guest";
    }

    @GetMapping("/")
    public String lobby(Model model, HttpServletRequest request) {
        String username = getUsername(request);
        roomService.addOnlineUser(username);
        
        List<GameRoom> rooms = roomService.getAllRooms();
        long onlineCount = roomService.getOnlineUserCount();
        
        model.addAttribute("username", username);
        model.addAttribute("rooms", rooms);
        model.addAttribute("onlineCount", onlineCount);
        return "lobby";
    }

    @PostMapping("/api/room/create")
    @ResponseBody
    public Map<String, Object> createRoom(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String roomName = body.get("roomName");
            if (roomName == null || roomName.trim().isEmpty()) {
                roomName = getUsername(request) + "的房间";
            }
            
            GameRoom room = roomService.createRoom(roomName, getUsername(request));
            response.put("success", true);
            response.put("roomId", room.getId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建失败");
        }
        return response;
    }

    @PostMapping("/api/room/join/{roomId}")
    @ResponseBody
    public Map<String, Object> joinRoom(@PathVariable Long roomId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean success = roomService.joinRoom(roomId, getUsername(request));
        response.put("success", success);
        response.put("message", success ? "加入成功" : "房间已满或不存在");
        return response;
    }

    @GetMapping("/api/room/list")
    @ResponseBody
    public List<GameRoom> getRoomList() {
        return roomService.getAllRooms();
    }
}