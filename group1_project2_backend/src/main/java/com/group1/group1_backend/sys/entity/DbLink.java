package com.group1.group1_backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-29
 */
@TableName("db_link")
public class DbLink implements Serializable {
    public DbLink() {
    }

    public DbLink(Map<String, Object> data) {
        this.linkDbType = (String) data.get("linkDbType");
        if (!"oracle".equals(this.linkDbType)) {
            this.linkDbName = (String) data.get("linkDbName");
        }
        this.linkDbIp = (String) data.get("linkDbIp");
        this.linkUserName = (String) data.get("linkUserName");
        this.linkPassword = (String) data.get("linkPassword");
        this.linkPort = Integer.valueOf((String) data.get("linkPort"));
    }

    public DbLink(String linkDbType, String linkDbIp, String linkUserName, String linkPassword, Integer linkPort) {
        this.linkDbType = linkDbType;
        this.linkDbIp = linkDbIp;
        this.linkUserName = linkUserName;
        this.linkPassword = linkPassword;
        this.linkPort = linkPort;
    }

    public DbLink(String linkDbType, String linkDbIp, String linkDbName, String linkUserName, String linkPassword, Integer linkPort) {
        this.linkDbType = linkDbType;
        this.linkDbIp = linkDbIp;
        this.linkDbName = linkDbName;
        this.linkUserName = linkUserName;
        this.linkPassword = linkPassword;
        this.linkPort = linkPort;
    }

    private static final long serialVersionUID = 1L;

    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    /**
     * 这个连接对应的用户id，外键
     */
    private Integer userId;

    private String linkSystemName;

    private String linkDbType;

    private String linkDbIp;

    private String linkDbName;

    private String linkUserName;

    private String linkPassword;

    private Integer linkPort;

    /**
     * 数据库数据源连接管理表
     */
    private String linkComment;

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getLinkSystemName() {
        return linkSystemName;
    }

    public void setLinkSystemName(String linkSystemName) {
        this.linkSystemName = linkSystemName;
    }
    public String getLinkDbType() {
        return linkDbType;
    }

    public void setLinkDbType(String linkDbType) {
        this.linkDbType = linkDbType;
    }
    public String getLinkDbIp() {
        return linkDbIp;
    }

    public void setLinkDbIp(String linkDbIp) {
        this.linkDbIp = linkDbIp;
    }
    public String getLinkDbName() {
        return linkDbName;
    }

    public void setLinkDbName(String linkDbName) {
        this.linkDbName = linkDbName;
    }
    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }
    public String getLinkPassword() {
        return linkPassword;
    }

    public void setLinkPassword(String linkPassword) {
        this.linkPassword = linkPassword;
    }
    public Integer getLinkPort() {
        return linkPort;
    }

    public void setLinkPort(Integer linkPort) {
        this.linkPort = linkPort;
    }
    public String getLinkComment() {
        return linkComment;
    }

    public void setLinkComment(String linkComment) {
        this.linkComment = linkComment;
    }

    @Override
    public String toString() {
        return "DbLink{" +
            "linkId=" + linkId +
            ", userId=" + userId +
            ", linkSystemName=" + linkSystemName +
            ", linkDbType=" + linkDbType +
            ", linkDbIp=" + linkDbIp +
            ", linkDbName=" + linkDbName +
            ", linkUserName=" + linkUserName +
            ", linkPassword=" + linkPassword +
            ", linkPort=" + linkPort +
            ", linkComment=" + linkComment +
        "}";
    }
}
