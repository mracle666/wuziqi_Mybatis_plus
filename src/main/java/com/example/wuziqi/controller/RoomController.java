package com.example.wuziqi.controller;

import com.example.wuziqi.entity.GameRoom;
import com.example.wuziqi.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    private String getUsername(HttpServletRequest request) {
        String user = request.getRemoteUser();
        return user != null ? user : "guest";
    }

    @GetMapping("/room/{roomId}")
    public String gameRoom(@PathVariable Long roomId, Model model, HttpServletRequest request) {
        GameRoom room = roomService.getRoom(roomId);
        if (room == null) {
            return "redirect:/?tabId=" + request.getParameter("tabId");
        }

        String username = getUsername(request);
        boolean isPlayer = username.equals(room.getHostPlayer()) || 
                          (room.getGuestPlayer() != null && username.equals(room.getGuestPlayer()));

        if (!isPlayer) {
            roomService.addSpectator(roomId, username);
        }

        model.addAttribute("room", room);
        model.addAttribute("username", username);
        model.addAttribute("isPlayer", isPlayer);
        model.addAttribute("isSpectator", !isPlayer);
        model.addAttribute("spectatorCount", roomService.getSpectatorCount(roomId));
        return "game";
    }

    @PostMapping("/api/room/{roomId}/leave")
    @ResponseBody
    public Map<String, Object> leaveRoom(@PathVariable Long roomId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String username = getUsername(request);
        GameRoom room = roomService.getRoom(roomId);
        if (room != null) {
            roomService.removeSpectator(roomId, username);
            roomService.leaveRoom(roomId, username);
        }
        response.put("success", true);
        return response;
    }
}