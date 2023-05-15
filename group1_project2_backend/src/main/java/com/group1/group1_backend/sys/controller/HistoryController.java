package com.group1.group1_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.group1.group1_backend.comm.vo.Result;
import com.group1.group1_backend.sys.entity.DbLink;
import com.group1.group1_backend.sys.entity.History;
import com.group1.group1_backend.sys.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 历史记录，用于存放这个数据库表数据的导出历史记录 前端控制器
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private IHistoryService historyService;

    @GetMapping("/all")
    public Result<List<History>> getAllHistory() {
        List<History> list = historyService.list();
        list.forEach(System.out::println);
        return Result.success("获取所有历史记录成功", list);
    }

    /**
     * 获取一个数据库连接的所有历史记录
     * @param linkId
     * @return
     */
    @GetMapping("/get")
    public Result<?> getHistory(String linkId, String tableName) {
        System.out.println("info = " + linkId + " " + tableName);
        LambdaQueryWrapper<History> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(History::getLinkId, linkId).eq(History::getTableName, tableName);
        Map<String, Object> data = new HashMap<>();

        // 所有符合的历史记录
        List<History> list = historyService.list(wrapper);
        if (list.size() == 0) {
            data.put("latest", 0);
            data.put("total", 0);
            data.put("maxFieldCount", 0);
            data.put("historyList", list);
            return Result.success("暂无数据", data);
        }
        list.forEach(System.out::println);

        // 最近采集日期
        LocalDateTime latest = list.get(0).getGatherTime();
        for (History history : list) {
            if (history.getGatherTime().isAfter(latest)) {
                latest = history.getGatherTime();
            }
        }

        // 采集数据量总数
        int total = 0;
        for (History history : list) {
            total += Integer.valueOf(history.getDataCount());
        }

        // 最大采集字段个数
        int maxFieldCount = 0;
        for (History history : list) {
            maxFieldCount = Math.max(maxFieldCount, Integer.valueOf(history.getFieldCount()));
        }

        data.put("latest", latest);
        data.put("total", total);
        data.put("maxFieldCount", maxFieldCount);
        data.put("historyList", list);
        return Result.success("查询成功", data);
    }


}
