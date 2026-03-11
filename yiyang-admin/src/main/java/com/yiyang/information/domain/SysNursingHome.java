package com.yiyang.information.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 养老院基本信息对象 sys_nursing_home
 * 
 * @author Guangwen
 * @date 2026-03-11
 */
public class SysNursingHome extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 养老院名称 */
    @Excel(name = "养老院名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 介绍 */
    @Excel(name = "介绍")
    private String introduction;

    /** 展示图片URL */
    @Excel(name = "展示图片URL")
    private String imageUrl;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 成立日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date establishDate;

    /** 规模 */
    @Excel(name = "规模")
    private String scale;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setIntroduction(String introduction) 
    {
        this.introduction = introduction;
    }

    public String getIntroduction() 
    {
        return introduction;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setContactPerson(String contactPerson) 
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() 
    {
        return contactPerson;
    }

    public void setEstablishDate(Date establishDate) 
    {
        this.establishDate = establishDate;
    }

    public Date getEstablishDate() 
    {
        return establishDate;
    }

    public void setScale(String scale) 
    {
        this.scale = scale;
    }

    public String getScale() 
    {
        return scale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("address", getAddress())
            .append("introduction", getIntroduction())
            .append("imageUrl", getImageUrl())
            .append("contactPhone", getContactPhone())
            .append("contactPerson", getContactPerson())
            .append("establishDate", getEstablishDate())
            .append("scale", getScale())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
