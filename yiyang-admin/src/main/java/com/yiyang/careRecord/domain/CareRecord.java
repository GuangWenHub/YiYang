package com.yiyang.careRecord.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 生活照料记录对象 care_record
 * 
 * @author Guangwen
 * @date 2026-03-24
 */
public class CareRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 老人ID */
    @Excel(name = "老人ID")
    private Long elderlyId;

    /** 护工ID */
    @Excel(name = "护工ID")
    private Long creatorId;

    /** 照料类型 */
    @Excel(name = "照料类型")
    private String careType;

    /** 照料内容 */
    @Excel(name = "照料内容")
    private String careContent;

    /** 预计照料时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计照料时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date careTime;

    /** 照料开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "照料开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 照料结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "照料结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvedTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 打卡图片URL */
    @Excel(name = "打卡图片")
    private String imageUrl;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setCreatorId(Long creatorId) 
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() 
    {
        return creatorId;
    }

    public void setCareType(String careType) 
    {
        this.careType = careType;
    }

    public String getCareType() 
    {
        return careType;
    }

    public void setCareContent(String careContent) 
    {
        this.careContent = careContent;
    }

    public String getCareContent() 
    {
        return careContent;
    }

    public void setCareTime(Date careTime) 
    {
        this.careTime = careTime;
    }

    public Date getCareTime() 
    {
        return careTime;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setApprovedTime(Date approvedTime) 
    {
        this.approvedTime = approvedTime;
    }

    public Date getApprovedTime() 
    {
        return approvedTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("elderlyId", getElderlyId())
            .append("creatorId", getCreatorId())
            .append("careType", getCareType())
            .append("careContent", getCareContent())
            .append("careTime", getCareTime())
            .append("startTime", getStartTime())
            .append("approvedTime", getApprovedTime())
            .append("status", getStatus())
            .append("imageUrl", getImageUrl())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
