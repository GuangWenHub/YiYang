package com.yiyang.handover.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 医生交班记录对象 doctor_handover
 * 
 * @author GuangWen
 * @date 2026-03-28
 */
public class DoctorHandover extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 交接记录ID */
    @Excel(name = "交接记录ID")
    private Long handoverId;

    /** 当前医生ID */
    @Excel(name = "当前医生ID")
    private Long currentDoctorId;

    /** 待交接医生ID */
    @Excel(name = "待交接医生ID")
    private Long nextDoctorId;

    /** 上班时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上班时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 下班时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下班时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 当前情况概述 */
    @Excel(name = "当前情况概述")
    private String overview;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setHandoverId(Long handoverId) 
    {
        this.handoverId = handoverId;
    }

    public Long getHandoverId() 
    {
        return handoverId;
    }

    public void setCurrentDoctorId(Long currentDoctorId) 
    {
        this.currentDoctorId = currentDoctorId;
    }

    public Long getCurrentDoctorId() 
    {
        return currentDoctorId;
    }

    public void setNextDoctorId(Long nextDoctorId) 
    {
        this.nextDoctorId = nextDoctorId;
    }

    public Long getNextDoctorId() 
    {
        return nextDoctorId;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOverview(String overview) 
    {
        this.overview = overview;
    }

    public String getOverview() 
    {
        return overview;
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
            .append("handoverId", getHandoverId())
            .append("currentDoctorId", getCurrentDoctorId())
            .append("nextDoctorId", getNextDoctorId())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("overview", getOverview())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
