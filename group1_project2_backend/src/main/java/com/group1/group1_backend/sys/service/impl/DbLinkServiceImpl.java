package com.group1.group1_backend.sys.service.impl;

import com.group1.group1_backend.sys.entity.DbLink;
import com.group1.group1_backend.sys.mapper.DbLinkMapper;
import com.group1.group1_backend.sys.service.IDbLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-29
 */
@Service
public class DbLinkServiceImpl extends ServiceImpl<DbLinkMapper, DbLink> implements IDbLinkService {

    @Override
    public String test(@RequestBody DbLink dbLink) {
        System.out.println(dbLink.toString());
        try {
            System.out.println("dbLink = " + dbLink);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:" + dbLink.getLinkDbType() + "://" + dbLink.getLinkDbIp() + ":" + dbLink.getLinkPort() + "/" + dbLink.getLinkDbName();
            System.out.println("url = " + url);
            Connection connection = DriverManager.getConnection(url, dbLink.getLinkUserName(), dbLink.getLinkPassword());
            connection.close();
            return "连接成功";
        } catch (ClassNotFoundException | SQLException e) {
            return "连接失败：" + e.getMessage();
        }
    }
}
