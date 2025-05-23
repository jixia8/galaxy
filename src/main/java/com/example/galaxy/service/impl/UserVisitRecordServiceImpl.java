package com.example.galaxy.service.impl;

import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.entity.UserVisitRecord;
import com.example.galaxy.mapper.UserVisitRecordMapper;
import com.example.galaxy.service.inter.UserVisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserVisitRecordServiceImpl implements UserVisitRecordService {
    private final UserVisitRecordMapper userVisitRecordMapper;
    @Autowired
    public UserVisitRecordServiceImpl(UserVisitRecordMapper userVisitRecordMapper) {
        this.userVisitRecordMapper = userVisitRecordMapper;
    }
    @Override
    public void batchInsert(List<UserVisitRecord> records) {
        userVisitRecordMapper.batchInsert(records);
    }
    @Override
    public List<Map<String, Object>> selectWeeklyRank(){
        return userVisitRecordMapper.selectWeeklyRank();
    }
}
