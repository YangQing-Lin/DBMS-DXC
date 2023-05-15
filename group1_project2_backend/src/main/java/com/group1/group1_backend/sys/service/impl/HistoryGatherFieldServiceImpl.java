package com.group1.group1_backend.sys.service.impl;

import com.group1.group1_backend.sys.entity.HistoryGatherField;
import com.group1.group1_backend.sys.mapper.HistoryGatherFieldMapper;
import com.group1.group1_backend.sys.service.IHistoryGatherFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 历史记录详情，存放每条导出记录分别都导出了哪些表的哪些数据 服务实现类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
@Service
public class HistoryGatherFieldServiceImpl extends ServiceImpl<HistoryGatherFieldMapper, HistoryGatherField> implements IHistoryGatherFieldService {

}
