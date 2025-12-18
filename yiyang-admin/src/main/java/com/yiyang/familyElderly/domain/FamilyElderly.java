package com.yiyang.familyElderly.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 家属-老人关系对象 family_elderly
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class FamilyElderly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    private Long id;

    /** 家属用户ID */
    @Excel(name = "家属用户ID")
    private Long familyUserId;

    /** 老人ID */
    @Excel(name = "老人ID")
    private Long elderlyId;

    /** 亲属关系 */
    @Excel(name = "亲属关系")
    private String relation;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFamilyUserId(Long familyUserId) 
    {
        this.familyUserId = familyUserId;
    }

    public Long getFamilyUserId() 
    {
        return familyUserId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setRelation(String relation) 
    {
        this.relation = relation;
    }

    public String getRelation() 
    {
        return relation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("familyUserId", getFamilyUserId())
            .append("elderlyId", getElderlyId())
            .append("relation", getRelation())
            .toString();
    }
}
