package com.group1.group1_backend.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 历史记录详情，存放每条导出记录分别都导出了哪些表的哪些数据
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-30
 */
@TableName("history_gather_field")
public class HistoryGatherField implements Serializable {
    public HistoryGatherField(Integer historyId, String fieldComment, String fieldName, String fieldType, Integer fieldLength) {
        this.historyId = historyId;
        this.fieldComment = fieldComment;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldLength = fieldLength;
    }

    public HistoryGatherField() {
    }

    private static final long serialVersionUID = 1L;

    private Integer historyId;

    private String fieldComment;

    private String fieldName;

    private String fieldType;

    private Integer fieldLength;

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }
    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    @Override
    public String toString() {
        return "HistoryGatherField{" +
            "historyId=" + historyId +
            ", fieldComment=" + fieldComment +
            ", fieldName=" + fieldName +
            ", fieldType=" + fieldType +
            ", fieldLength=" + fieldLength +
        "}";
    }
}
