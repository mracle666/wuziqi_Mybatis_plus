package com.example.wuziqi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.wuziqi.entity.GameRoom;
import com.example.wuziqi.mapper.GameRoomMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoomService {

    @Autowired
    private GameRoomMapper roomMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ONLINE_USERS_KEY = "game:online_users";
    private static final String ROOM_SPECTATORS_KEY = "game:room_spectators:";

    public GameRoom createRoom(String roomName, String hostPlayer) {
        GameRoom room = new GameRoom(roomName, hostPlayer);
        roomMapper.insert(room);
        return room;
    }

    public GameRoom getRoom(Long roomId) {
        return roomMapper.selectById(roomId);
    }

    public List<GameRoom> getAllRooms() {
        return roomMapper.selectList(new QueryWrapper<GameRoom>().orderByDesc("create_time"));
    }

    public List<GameRoom> getWaitingRooms() {
        return roomMapper.selectList(
            new QueryWrapper<GameRoom>().eq("status", GameRoom.STATUS_WAITING).orderByDesc("create_time")
        );
    }

    public boolean joinRoom(Long roomId, String guestPlayer) {
        GameRoom room = roomMapper.selectById(roomId);
        if (room == null || room.getStatus() != GameRoom.STATUS_WAITING) {
            return false;
        }
        if (room.getGuestPlayer() != null) {
            return false;
        }
        if (guestPlayer.equals(room.getHostPlayer())) {
            return false;
        }
        
        room.setGuestPlayer(guestPlayer);
        room.setStatus(GameRoom.STATUS_PLAYING);
        roomMapper.updateById(room);
        return true;
    }

    public void leaveRoom(Long roomId, String username) {
        GameRoom room = roomMapper.selectById(roomId);
        if (room == null) return;

        if (username.equals(room.getHostPlayer())) {
            roomMapper.deleteById(roomId);
            redisTemplate.delete(ROOM_SPECTATORS_KEY + roomId);
        } else if (username.equals(room.getGuestPlayer())) {
            room.setGuestPlayer(null);
            room.setStatus(GameRoom.STATUS_WAITING);
            roomMapper.updateById(room);
        }
    }

    public void updateRoomState(Long roomId, String boardData, Integer currentPlayer, Boolean gameOver, Integer winner) {
        UpdateWrapper<GameRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", roomId);
        updateWrapper.set("board_data", boardData);
        updateWrapper.set("current_player", currentPlayer);
        updateWrapper.set("game_over", gameOver);
        updateWrapper.set("winner", winner);
        if (gameOver) {
            updateWrapper.set("status", GameRoom.STATUS_FINISHED);
        }
        roomMapper.update(null, updateWrapper);
    }

    public void addOnlineUser(String username) {
        redisTemplate.opsForSet().add(ONLINE_USERS_KEY, username);
    }

    public void removeOnlineUser(String username) {
        redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, username);
    }

    public Set<Object> getOnlineUsers() {
        return redisTemplate.opsForSet().members(ONLINE_USERS_KEY);
    }

    public long getOnlineUserCount() {
        Long count = redisTemplate.opsForSet().size(ONLINE_USERS_KEY);
        return count != null ? count : 0;
    }

    public void addSpectator(Long roomId, String username) {
        redisTemplate.opsForSet().add(ROOM_SPECTATORS_KEY + roomId, username);
    }

    public void removeSpectator(Long roomId, String username) {
        redisTemplate.opsForSet().remove(ROOM_SPECTATORS_KEY + roomId, username);
    }

    public long getSpectatorCount(Long roomId) {
        Long count = redisTemplate.opsForSet().size(ROOM_SPECTATORS_KEY + roomId);
        return count != null ? count : 0;
    }

    public Set<Object> getSpectators(Long roomId) {
        return redisTemplate.opsForSet().members(ROOM_SPECTATORS_KEY + roomId);
    }

    public String getEmptyBoardJson() throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("boardSize", 15);
        int[][] boardArray = new int[15][15];
        data.put("board", boardArray);
        return objectMapper.writeValueAsString(data);
    }
}
