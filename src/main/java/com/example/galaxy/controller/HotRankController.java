package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.Constants;
import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.service.inter.UserVisitRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.AbstractMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hot-rank")
@Slf4j
public class HotRankController {
    private final UserVisitRecordService userVisitRecordService;
    private final RedisUtils redisUtils;

    @Autowired
    public HotRankController(UserVisitRecordService userVisitRecordService, RedisUtils redisUtils) {
        this.userVisitRecordService = userVisitRecordService;
        this.redisUtils = redisUtils;
    }

    // 当日热度排行
    @GetMapping("/today")
    public ResultVO<List<Map<String, Object>>> getTodayHotRank() {
        try {
            Map<String, Object> visits = redisUtils.getHash(Constants.USER_DAILY_KEY);

            if (visits == null || visits.isEmpty()) {
                return ResultVOUtils.success(Collections.emptyList());
            }

            List<Map<String, Object>> result = visits.entrySet()
                    .stream()
                    .map(entry -> {
                        Object value = entry.getValue();
                        int count = value == null ? 0 : Integer.parseInt(value.toString());
                        return new AbstractMap.SimpleEntry<>(entry.getKey(), count);
                    })
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(10)
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("username", entry.getKey());
                        item.put("totalVisits", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return ResultVOUtils.success(result);

        } catch (Exception e) {
            log.error("获取今日热度排行失败", e);
            return ResultVOUtils.failed("获取热度排行失败");
        }
    }

    // 周热度排行
    @GetMapping("/weekly")
    public ResultVO<List<Map<String, Object>>> getWeeklyHotRank() {
        try {
            List<Map<String, Object>> records = userVisitRecordService.selectWeeklyRank();
            return ResultVOUtils.success(records);
        } catch (Exception e) {
            log.error("获取周热度排行失败", e);
            return ResultVOUtils.failed("获取热度排行失败");
        }
    }
}