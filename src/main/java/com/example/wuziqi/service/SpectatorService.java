package com.example.wuziqi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class SpectatorService {

    private static final String SPECTATOR_SET_KEY = "game:spectators";
    private static final String GAME_STATE_KEY = "game:current_state";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addSpectator(String username) {
        redisTemplate.opsForSet().add(SPECTATOR_SET_KEY, username);
    }

    public void removeSpectator(String username) {
        redisTemplate.opsForSet().remove(SPECTATOR_SET_KEY, username);
    }

    public long getSpectatorCount() {
        Long count = redisTemplate.opsForSet().size(SPECTATOR_SET_KEY);
        return count != null ? count : 0;
    }

    public Set<Object> getAllSpectators() {
        return redisTemplate.opsForSet().members(SPECTATOR_SET_KEY);
    }

    public void saveGameState(String boardData) {
        redisTemplate.opsForValue().set(GAME_STATE_KEY, boardData, 24, TimeUnit.HOURS);
    }

    public String getGameState() {
        Object state = redisTemplate.opsForValue().get(GAME_STATE_KEY);
        return state != null ? state.toString() : null;
    }

    public void clearGameState() {
        redisTemplate.delete(GAME_STATE_KEY);
    }
}
