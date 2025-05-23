package com.example.galaxy.mapper;
import com.example.galaxy.entity.UserVisitRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserVisitRecordMapper {
    void batchInsert(List<UserVisitRecord> records);
    List<Map<String, Object>> selectWeeklyRank();
}