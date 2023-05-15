package com.group1.group1_backend.sys.service.impl;

import com.group1.group1_backend.sys.entity.History;
import com.group1.group1_backend.sys.mapper.HistoryMapper;
import com.group1.group1_backend.sys.service.IHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 历史记录，用于存放这个数据库表数据的导出历史记录 服务实现类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements IHistoryService {

}
