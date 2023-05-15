package com.group1.group1_backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 历史记录，用于存放这个数据库表数据的导出历史记录
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
public class History implements Serializable {
    public History(String linkId, String tableName, String tableComment, Integer fieldCount, Integer dataCount, String gatherState, String gatherType, String exportType, String exportPath, String filterCriteria) {
        this.linkId = Integer.valueOf(linkId);
        this.tableName = tableName;
        this.tableComment = tableComment;
        this.fieldCount = fieldCount;
        this.dataCount = dataCount;
        this.gatherState = Integer.valueOf(gatherState);
        this.gatherType = Integer.valueOf(gatherType);
        this.exportType = exportType;
        this.exportPath = exportPath;
        this.filterCriteria = filterCriteria;
    }

    public History() {
    }

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录id
     */
    @TableId(value = "history_id", type = IdType.AUTO)
    private Integer historyId;

    /**
     * 对应的数据库连接id
     */
    private Integer linkId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 字段总数
     */
    private Integer fieldCount;

    /**
     * 数据量
     */
    private Integer dataCount;

    /**
     * 采集日期
     */
    private LocalDateTime gatherTime;

    /**
     * 采集状态，0：失败，1：成功
     */
    private Integer gatherState;

    /**
     * 采集方式，0：增量，1：全量
     */
    private Integer gatherType;

    /**
     * 导出类型，txt、binary、xlsx、xls
     */
    private String exportType;

    /**
     * 导出路径
     */
    private String exportPath;

    /**
     * 过滤条件
     */
    private String filterCriteria;

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }
    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }
    public Integer getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }
    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }
    public LocalDateTime getGatherTime() {
        return gatherTime;
    }

    public void setGatherTime(LocalDateTime gatherTime) {
        this.gatherTime = gatherTime;
    }
    public Integer getGatherState() {
        return gatherState;
    }

    public void setGatherState(Integer gatherState) {
        this.gatherState = gatherState;
    }
    public Integer getGatherType() {
        return gatherType;
    }

    public void setGatherType(Integer gatherType) {
        this.gatherType = gatherType;
    }
    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
    public String getFilterCriteria() { return filterCriteria; }
    public void setFilterCriteria(String filterCriteria) { this.filterCriteria = filterCriteria; }

    @Override
    public String toString() {
        return "History{" +
            "historyId=" + historyId +
            ", linkId=" + linkId +
            ", tableName=" + tableName +
            ", tableComment=" + tableComment +
            ", fieldCount=" + fieldCount +
            ", dataCount=" + dataCount +
            ", gatherTime=" + gatherTime +
            ", gatherState=" + gatherState +
            ", gatherType=" + gatherType +
            ", exportType=" + exportType +
            ", exportPath=" + exportPath +
            ", filterCriteria=" + filterCriteria +
        "}";
    }
}
