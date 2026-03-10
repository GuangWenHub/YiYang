package com.yiyang.detail.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 项目单明细对象 project_order_detail
 * 
 * @author GuangWen
 * @date 2026-03-10
 */
public class ProjectOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long detailId;

    /** 所属项目单ID */
    @Excel(name = "所属项目单ID")
    private Long orderId;

    /** 分配护工ID */
    @Excel(name = "分配护工ID")
    private Long creatorId;

    /** 服务开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "服务开始日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /** 服务结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "服务结束日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /** 服务状态 */
    @Excel(name = "服务状态")
    private Long status;

    /** 打卡图片URL */
    @Excel(name = "打卡图片URL")
    private String imageUrl;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    public void setDetailId(Long detailId) 
    {
        this.detailId = detailId;
    }

    public Long getDetailId() 
    {
        return detailId;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setCreatorId(Long creatorId) 
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() 
    {
        return creatorId;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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
            .append("detailId", getDetailId())
            .append("orderId", getOrderId())
            .append("creatorId", getCreatorId())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("imageUrl", getImageUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
