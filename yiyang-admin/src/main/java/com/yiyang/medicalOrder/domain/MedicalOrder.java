package com.yiyang.medicalOrder.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;
import com.yiyang.medicationRecord.domain.MedicationRecord;

/**
 * 医嘱开立对象 medical_order
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
public class MedicalOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 医嘱ID */
    private Long orderId;

    /** 老人ID (逻辑关联，无物理外键) */
    @Excel(name = "老人ID ()")
    private Long elderlyId;

    /** 医嘱内容 */
    @Excel(name = "医嘱内容")
    private String orderContent;

    /** 用药建议 */
    @Excel(name = "用药建议")
    private String medicationAdvice;

    /** 医生ID () */
    private Long doctorId;

    /** 医嘱日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "医嘱日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 用药表ID (建议业务层忽略此字段，关系由子表维护) */
    private Long medicationId;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用药记录列表 */
    private List<MedicationRecord> medicationRecords;

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

    public void setDoctorId(Long doctorId) 
    {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() 
    {
        return doctorId;
    }

    public void setOrderDate(Date orderDate) 
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() 
    {
        return orderDate;
    }

    public void setMedicationId(Long medicationId) 
    {
        this.medicationId = medicationId;
    }

    public Long getMedicationId() 
    {
        return medicationId;
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

    public void setMedicationRecords(List<MedicationRecord> medicationRecords) 
    {
        this.medicationRecords = medicationRecords;
    }

    public List<MedicationRecord> getMedicationRecords() 
    {
        return medicationRecords;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("elderlyId", getElderlyId())
            .append("orderContent", getOrderContent())
            .append("medicationAdvice", getMedicationAdvice())
            .append("doctorId", getDoctorId())
            .append("orderDate", getOrderDate())
            .append("medicationId", getMedicationId())
            .append("status", getStatus())
            .append("medicationRecords", getMedicationRecords())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
