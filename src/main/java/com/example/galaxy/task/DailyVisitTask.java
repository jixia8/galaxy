package com.example.galaxy.task;
import com.example.galaxy.common.Constants;
import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.entity.UserVisitRecord;
import com.example.galaxy.service.inter.UserVisitRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DailyVisitTask {
    private final UserVisitRecordService userVisitRecordService;
    private final RedisUtils redisUtils;
    @Autowired
    public DailyVisitTask(UserVisitRecordService userVisitRecordService, RedisUtils redisUtils)
    {
        this.userVisitRecordService = userVisitRecordService;
        this.redisUtils = redisUtils;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void SaveDailyVisitToDB(){
        try {
            Map<String, Object> dailyVisit=redisUtils.getHash(Constants.USER_DAILY_KEY);
            if (dailyVisit!=null && dailyVisit.isEmpty()){
                List<UserVisitRecord> records= new ArrayList<>();
                Date today = new Date();
                for (Map.Entry<String, Object> entry : dailyVisit.entrySet()) {
                    UserVisitRecord record = new UserVisitRecord();
                    record.setUserId(Long.parseLong(entry.getKey()));
                    record.setVisitCount(Integer.parseInt(entry.getValue().toString()));
                    record.setRecordDate(today);
                    records.add(record);
                }
                userVisitRecordService.batchInsert(records);
                redisUtils.delete(Constants.USER_DAILY_KEY);
                log.info("每日访问数据已保存至数据库，并清空 Redis");
            }else{
                log.info("Redis 中没有每日访问数据");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
