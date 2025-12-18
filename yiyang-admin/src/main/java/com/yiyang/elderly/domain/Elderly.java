package com.yiyang.elderly.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 老人档案对象 elderly
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class Elderly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 老人ID */
    private Long elderlyId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 身份证号 */
    private String idCard;

    /** 性别：M/F */
    @Excel(name = "性别：M/F")
    private String gender;

    /** 出生日期 */
    private Date birthDate;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 家庭住址 */
    private String address;

    /** 紧急联系人姓名 */
    private String emergencyContact;

    /** 紧急联系电话 */
    private String emergencyPhone;

    /** 健康状况摘要 */
    @Excel(name = "健康状况摘要")
    private String healthStatus;

    /** 入住时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date admissionDate;

    /** 居住状态 */
    @Excel(name = "居住状态")
    private Long status;

    /** 删除标志 */
    private Long delFlag;

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setEmergencyContact(String emergencyContact) 
    {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContact() 
    {
        return emergencyContact;
    }

    public void setEmergencyPhone(String emergencyPhone) 
    {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmergencyPhone() 
    {
        return emergencyPhone;
    }

    public void setHealthStatus(String healthStatus) 
    {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() 
    {
        return healthStatus;
    }

    public void setAdmissionDate(Date admissionDate) 
    {
        this.admissionDate = admissionDate;
    }

    public Date getAdmissionDate() 
    {
        return admissionDate;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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
            .append("elderlyId", getElderlyId())
            .append("name", getName())
            .append("idCard", getIdCard())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("emergencyContact", getEmergencyContact())
            .append("emergencyPhone", getEmergencyPhone())
            .append("healthStatus", getHealthStatus())
            .append("admissionDate", getAdmissionDate())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
