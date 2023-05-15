package com.group1.group1_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.group1.group1_backend.comm.vo.Result;
import com.group1.group1_backend.sys.entity.History;
import com.group1.group1_backend.sys.entity.HistoryGatherField;
import com.group1.group1_backend.sys.service.IHistoryGatherFieldService;
import com.group1.group1_backend.sys.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 历史记录详情，存放每条导出记录分别都导出了哪些表的哪些数据 前端控制器
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/history_info")
public class HistoryGatherFieldController {
    @Autowired
    private IHistoryGatherFieldService historyGatherFieldService;

    @GetMapping("/all")
    public Result<List<HistoryGatherField>> getAllHistoryGatherField() {
        List<HistoryGatherField> list = historyGatherFieldService.list();
        list.forEach(System.out::println);
        return Result.success("获取所有历史记录详细信息成功", list);
    }

    /**
     * 获取一个历史记录的详细信息
     * @param historyId
     * @return
     */
    @GetMapping("/get")
    public Result<List<HistoryGatherField>> getHistoryGatherFieldInfo(String historyId) {
        System.out.println("history id: " + historyId);
        LambdaQueryWrapper<HistoryGatherField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HistoryGatherField::getHistoryId, Integer.valueOf(historyId));

        // 所有符合的历史记录
        List<HistoryGatherField> list = historyGatherFieldService.list(wrapper);
        System.out.println("list: " + list);
        if (list.size() == 0) {
            return Result.fail("查询失败");
        }
        list.forEach(System.out::println);
        return Result.success("获取历史记录详细信息成功", list);
    }
}
