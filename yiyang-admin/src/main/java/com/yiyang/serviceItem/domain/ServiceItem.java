package com.yiyang.serviceItem.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 服务项目库对象 service_item
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class ServiceItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 服务项目ID */
    private Long itemId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String name;

    /** 项目分类 */
    @Excel(name = "项目分类")
    private String category;

    /** 预计耗时（分钟） */
    private Long durationMinutes;

    /** 单价（元） */
    @Excel(name = "单价", readConverterExp = "元=")
    private BigDecimal price;

    /** 项目详细描述 */
    @Excel(name = "项目详细描述")
    private String description;

    /** 项目图片路径 */
    @Excel(name = "项目图片路径")
    private String imageUrl;

    /** 是否启用 */
    private Long isActive;

    /** 删除标志 */
    private Long delFlag;

    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setDurationMinutes(Long durationMinutes) 
    {
        this.durationMinutes = durationMinutes;
    }

    public Long getDurationMinutes() 
    {
        return durationMinutes;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setIsActive(Long isActive) 
    {
        this.isActive = isActive;
    }

    public Long getIsActive() 
    {
        return isActive;
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
            .append("itemId", getItemId())
            .append("name", getName())
            .append("category", getCategory())
            .append("durationMinutes", getDurationMinutes())
            .append("price", getPrice())
            .append("description", getDescription())
            .append("imageUrl", getImageUrl())
            .append("isActive", getIsActive())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
