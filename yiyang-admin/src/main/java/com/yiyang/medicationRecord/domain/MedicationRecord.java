package com.yiyang.medicationRecord.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 用药跟踪对象 medication_record
 * 
 * @author GuangWen
 * @date 2026-03-19
 */
public class MedicationRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用药记录ID */
    private Long recordId;

    /** 医嘱ID */
    private Long orderId;

    /** 药品ID  */
    @Excel(name = "药品ID ")
    private Long medicineId;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String medicineName;

    /** 医嘱内容 */
    @Excel(name = "医嘱内容")
    private String orderContent;

    /** 用药建议 */
    @Excel(name = "用药建议")
    private String medicationAdvice;

    /** 计划用药时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划用药时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledTime;

    /** 实际用药时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际用药时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualTime;

    /** 分配护工ID */
    @Excel(name = "分配护工ID")
    private Long nurseId;

    /** 打卡图片URL */
    @Excel(name = "打卡图片URL")
    private String imageUrl;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private String status;

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

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setMedicineId(Long medicineId) 
    {
        this.medicineId = medicineId;
    }

    public Long getMedicineId() 
    {
        return medicineId;
    }

    public void setMedicineName(String medicineName) 
    {
        this.medicineName = medicineName;
    }

    public String getMedicineName() 
    {
        return medicineName;
    }

    public void setOrderContent(String orderContent) 
    {
        this.orderContent = orderContent;
    }

    public String getOrderContent() 
    {
        return orderContent;
    }

    public void setMedicationAdvice(String medicationAdvice) 
    {
        this.medicationAdvice = medicationAdvice;
    }

    public String getMedicationAdvice() 
    {
        return medicationAdvice;
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

    public void setNurseId(Long nurseId) 
    {
        this.nurseId = nurseId;
    }

    public Long getNurseId() 
    {
        return nurseId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("orderId", getOrderId())
            .append("medicineId", getMedicineId())
            .append("medicineName", getMedicineName())
            .append("scheduledTime", getScheduledTime())
            .append("actualTime", getActualTime())
            .append("nurseId", getNurseId())
            .append("imageUrl", getImageUrl())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("orderContent", getOrderContent())
            .append("medicationAdvice", getMedicationAdvice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
