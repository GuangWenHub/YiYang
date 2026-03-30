package com.yiyang.floorDept.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 楼层科室信息对象 sys_floor_dept
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
public class SysFloorDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 楼层科室ID */
    private Long floorDeptId;

    /** 楼层号 */
    @Excel(name = "楼层号")
    private Long floor;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String deptName;

    /** 科室简介 */
    @Excel(name = "科室简介")
    private String deptDesc;

    /** 状态（0-正常，1-停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 删除标志（0-正常，2-删除） */
    private String delFlag;

    public void setFloorDeptId(Long floorDeptId)
    {
        this.floorDeptId = floorDeptId;
    }

    public Long getFloorDeptId()
    {
        return floorDeptId;
    }
    public void setFloor(Long floor)
    {
        this.floor = floor;
    }

    public Long getFloor()
    {
        return floor;
    }
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }
    public void setDeptDesc(String deptDesc)
    {
        this.deptDesc = deptDesc;
    }

    public String getDeptDesc()
    {
        return deptDesc;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
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
            .append("floorDeptId", getFloorDeptId())
            .append("floor", getFloor())
            .append("deptName", getDeptName())
            .append("deptDesc", getDeptDesc())
            .append("status", getStatus())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
