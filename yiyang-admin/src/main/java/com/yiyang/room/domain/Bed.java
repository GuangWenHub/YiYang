package com.yiyang.room.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 床位信息对象 bed
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class Bed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 床位ID */
    private Long bedId;

    /** 所属房间ID */
    @Excel(name = "所属房间ID")
    private Long roomId;

    /** 床位编号（如A01） */
    @Excel(name = "床位编号", readConverterExp = "如=A01")
    private String bedNumber;

    /** 床位状态：0-空闲，1-占用，2-维修 */
    @Excel(name = "床位状态：0-空闲，1-占用，2-维修")
    private Long status;

    /** 当前分配的老人ID（为空表示未分配） */
    @Excel(name = "当前分配的老人ID", readConverterExp = "为=空表示未分配")
    private Long elderlyId;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    public void setBedId(Long bedId) 
    {
        this.bedId = bedId;
    }

    public Long getBedId() 
    {
        return bedId;
    }
    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }
    public void setBedNumber(String bedNumber) 
    {
        this.bedNumber = bedNumber;
    }

    public String getBedNumber() 
    {
        return bedNumber;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
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
            .append("bedId", getBedId())
            .append("roomId", getRoomId())
            .append("bedNumber", getBedNumber())
            .append("status", getStatus())
            .append("elderlyId", getElderlyId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
