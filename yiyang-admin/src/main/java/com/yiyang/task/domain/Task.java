package com.yiyang.task.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 任务执行对象 task
 * 
 * @author GuangWenLI
 * @date 2025-12-18
 */
public class Task extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 来源明细ID */
    @Excel(name = "来源明细ID")
    private Long orderDetailId;

    /** 指派护工用户ID */
    @Excel(name = "指派护工用户ID")
    private Long caregiverId;

    /** 计划执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scheduledTime;

    /** 实际完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualTime;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Long status;

    /** 完成情况备注 */
    @Excel(name = "完成情况备注")
    private String completionNote;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }

    public void setOrderDetailId(Long orderDetailId) 
    {
        this.orderDetailId = orderDetailId;
    }

    public Long getOrderDetailId() 
    {
        return orderDetailId;
    }

    public void setCaregiverId(Long caregiverId) 
    {
        this.caregiverId = caregiverId;
    }

    public Long getCaregiverId() 
    {
        return caregiverId;
    }

    public void setScheduledTime(Date scheduledTime) 
    {
        this.scheduledTime = scheduledTime;
    }

    public Date getScheduledTime() 
    {
        return scheduledTime;
    }

    public void setActualTime(Date actualTime) 
    {
        this.actualTime = actualTime;
    }

    public Date getActualTime() 
    {
        return actualTime;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setCompletionNote(String completionNote) 
    {
        this.completionNote = completionNote;
    }

    public String getCompletionNote() 
    {
        return completionNote;
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
            .append("taskId", getTaskId())
            .append("orderDetailId", getOrderDetailId())
            .append("caregiverId", getCaregiverId())
            .append("scheduledTime", getScheduledTime())
            .append("actualTime", getActualTime())
            .append("status", getStatus())
            .append("completionNote", getCompletionNote())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
