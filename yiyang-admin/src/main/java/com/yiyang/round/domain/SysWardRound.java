package com.yiyang.round.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 查房记录对象 sys_ward_round
 * 
 * @author GuangWen
 * @date 2026-03-11
 */
public class SysWardRound extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 查房记录ID */
    private Long roundId;

    /** 房间ID */
    @Excel(name = "房间ID")
    private Long roomId;

    /** 床位ID */
    @Excel(name = "床位ID")
    private Long bedId;

    /** 老人ID */
    @Excel(name = "老人ID")
    private Long elderlyId;

    /** 查房时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "查房时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date roundTime;

    /** 查房人ID */
    @Excel(name = "查房人ID")
    private Long roundUserId;

    /** 查房状态：0-正常，1-异常 */
    @Excel(name = "查房状态：0-正常，1-异常")
    private Long status;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String abnormalDesc;

    /** 处理措施 */
    @Excel(name = "处理措施")
    private String handleMeasure;

    /** 查房图片URL */
    @Excel(name = "查房图片URL")
    private String imageUrl;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    public void setRoundId(Long roundId) 
    {
        this.roundId = roundId;
    }

    public Long getRoundId() 
    {
        return roundId;
    }

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setBedId(Long bedId) 
    {
        this.bedId = bedId;
    }

    public Long getBedId() 
    {
        return bedId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setRoundTime(Date roundTime) 
    {
        this.roundTime = roundTime;
    }

    public Date getRoundTime() 
    {
        return roundTime;
    }

    public void setRoundUserId(Long roundUserId) 
    {
        this.roundUserId = roundUserId;
    }

    public Long getRoundUserId() 
    {
        return roundUserId;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setAbnormalDesc(String abnormalDesc) 
    {
        this.abnormalDesc = abnormalDesc;
    }

    public String getAbnormalDesc() 
    {
        return abnormalDesc;
    }

    public void setHandleMeasure(String handleMeasure) 
    {
        this.handleMeasure = handleMeasure;
    }

    public String getHandleMeasure() 
    {
        return handleMeasure;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
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
            .append("roundId", getRoundId())
            .append("roomId", getRoomId())
            .append("bedId", getBedId())
            .append("elderlyId", getElderlyId())
            .append("roundTime", getRoundTime())
            .append("roundUserId", getRoundUserId())
            .append("status", getStatus())
            .append("abnormalDesc", getAbnormalDesc())
            .append("handleMeasure", getHandleMeasure())
            .append("imageUrl", getImageUrl())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
