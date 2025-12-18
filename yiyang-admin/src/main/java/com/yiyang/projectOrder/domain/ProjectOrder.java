package com.yiyang.projectOrder.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 项目单主对象 project_order
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class ProjectOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目单ID */
    private Long orderId;

    /** 关联的老人ID */
    @Excel(name = "关联的老人ID")
    private Long elderlyId;

    /** 开单人用户ID */
    @Excel(name = "开单人用户ID")
    private Long creatorId;

    /** 开单人角色：1-管理员,2-医生 */
    @Excel(name = "开单人角色：1-管理员,2-医生")
    private Long creatorRole;

    /** 审核人用户ID */
    @Excel(name = "审核人用户ID")
    private Long auditorId;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 开单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 提交审核时间 */
    private Date submittedTime;

    /** 审核通过时间 */
    private Date approvedTime;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
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

    public void setCreatorRole(Long creatorRole) 
    {
        this.creatorRole = creatorRole;
    }

    public Long getCreatorRole() 
    {
        return creatorRole;
    }

    public void setAuditorId(Long auditorId) 
    {
        this.auditorId = auditorId;
    }

    public Long getAuditorId() 
    {
        return auditorId;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setSubmittedTime(Date submittedTime) 
    {
        this.submittedTime = submittedTime;
    }

    public Date getSubmittedTime() 
    {
        return submittedTime;
    }

    public void setApprovedTime(Date approvedTime) 
    {
        this.approvedTime = approvedTime;
    }

    public Date getApprovedTime() 
    {
        return approvedTime;
    }

    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("elderlyId", getElderlyId())
            .append("creatorId", getCreatorId())
            .append("creatorRole", getCreatorRole())
            .append("auditorId", getAuditorId())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("submittedTime", getSubmittedTime())
            .append("approvedTime", getApprovedTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
