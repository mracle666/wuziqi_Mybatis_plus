package com.example.wuziqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wuziqi.entity.GameSave;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameSaveMapper extends BaseMapper<GameSave> {

    List<GameSave> findAllOrderByCreateTimeDesc();
}
