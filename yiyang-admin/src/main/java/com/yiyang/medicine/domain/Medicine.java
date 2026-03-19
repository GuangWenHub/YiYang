package com.yiyang.medicine.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 药品管理对象 medicine
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
public class Medicine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 药品ID */
    private Long medicineId;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String medicineName;

    /** 药品价格 */
    @Excel(name = "药品价格")
    private BigDecimal price;

    /** 药品说明 */
    @Excel(name = "药品说明")
    private String description;

    /** 药品库存 */
    @Excel(name = "药品库存")
    private Long stock;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
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
            .append("medicineId", getMedicineId())
            .append("medicineName", getMedicineName())
            .append("price", getPrice())
            .append("description", getDescription())
            .append("stock", getStock())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
