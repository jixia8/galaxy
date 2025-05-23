package com.example.galaxy.service.inter;

import com.example.galaxy.entity.UserVisitRecord;

import java.util.List;
import java.util.Map;

public interface UserVisitRecordService {
    void batchInsert(List<UserVisitRecord> records);
    List<Map<String, Object>> selectWeeklyRank();
}
