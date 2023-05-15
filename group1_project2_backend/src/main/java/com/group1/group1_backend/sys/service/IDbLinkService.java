package com.group1.group1_backend.sys.service;

import com.group1.group1_backend.sys.entity.DbLink;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-29
 */
public interface IDbLinkService extends IService<DbLink> {

    String test(DbLink dbLink);
}
