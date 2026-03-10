package com.yiyang.record.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 健康档案记录对象 health_record
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class HealthRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 健康记录ID */
    private Long recordId;

    /** 老人ID */
    @Excel(name = "老人ID")
    private Long elderlyId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderlyName;

    /** 记录人用户ID */
    private Long recorderId;

    /** 记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordTime;

    /** 血压（格式：120/80） */
    @Excel(name = "血压", readConverterExp = "格=式：120/80")
    private String bloodPressure;

    /** 心率（次/分钟） */
    @Excel(name = "心率", readConverterExp = "次=/分钟")
    private Long heartRate;

    /** 体温（℃） */
    @Excel(name = "体温", readConverterExp = "℃=")
    private BigDecimal temperature;

    /** 体重（kg） */
    private BigDecimal weight;

    /** 血糖（mmol/L） */
    @Excel(name = "血糖", readConverterExp = "m=mol/L")
    private BigDecimal bloodSugar;

    /** 症状描述 */
    @Excel(name = "症状描述")
    private String symptomDesc;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

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

    public void setElderlyName(String elderlyName) 
    {
        this.elderlyName = elderlyName;
    }

    public String getElderlyName() 
    {
        return elderlyName;
    }

    public void setRecorderId(Long recorderId) 
    {
        this.recorderId = recorderId;
    }

    public Long getRecorderId() 
    {
        return recorderId;
    }

    public void setRecordTime(Date recordTime) 
    {
        this.recordTime = recordTime;
    }

    public Date getRecordTime() 
    {
        return recordTime;
    }

    public void setBloodPressure(String bloodPressure) 
    {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodPressure() 
    {
        return bloodPressure;
    }

    public void setHeartRate(Long heartRate) 
    {
        this.heartRate = heartRate;
    }

    public Long getHeartRate() 
    {
        return heartRate;
    }

    public void setTemperature(BigDecimal temperature) 
    {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() 
    {
        return temperature;
    }

    public void setWeight(BigDecimal weight) 
    {
        this.weight = weight;
    }

    public BigDecimal getWeight() 
    {
        return weight;
    }

    public void setBloodSugar(BigDecimal bloodSugar) 
    {
        this.bloodSugar = bloodSugar;
    }

    public BigDecimal getBloodSugar() 
    {
        return bloodSugar;
    }

    public void setSymptomDesc(String symptomDesc) 
    {
        this.symptomDesc = symptomDesc;
    }

    public String getSymptomDesc() 
    {
        return symptomDesc;
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
            .append("recordId", getRecordId())
            .append("elderlyId", getElderlyId())
            .append("elderlyName", getElderlyName())
            .append("recorderId", getRecorderId())
            .append("recordTime", getRecordTime())
            .append("bloodPressure", getBloodPressure())
            .append("heartRate", getHeartRate())
            .append("temperature", getTemperature())
            .append("weight", getWeight())
            .append("bloodSugar", getBloodSugar())
            .append("symptomDesc", getSymptomDesc())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
