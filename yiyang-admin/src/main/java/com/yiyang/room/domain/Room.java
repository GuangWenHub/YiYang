package com.yiyang.room.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yiyang.common.annotation.Excel;
import com.yiyang.common.core.domain.BaseEntity;

/**
 * 房间信息对象 room
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public class Room extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long roomId;

    /** 房间号（如101） */
    @Excel(name = "房间号", readConverterExp = "如=101")
    private String roomNumber;

    /** 楼层 */
    @Excel(name = "楼层")
    private Long floor;

    /** 床位容量 */
    @Excel(name = "床位容量")
    private Long capacity;

    /** 房间图片路径 */
    @Excel(name = "房间图片路径")
    private String imageUrl;

    /** 删除标志：0-正常，1-删除 */
    private Long delFlag;

    /** 床位信息信息 */
    private List<Bed> bedList;

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setFloor(Long floor) 
    {
        this.floor = floor;
    }

    public Long getFloor() 
    {
        return floor;
    }

    public void setCapacity(Long capacity) 
    {
        this.capacity = capacity;
    }

    public Long getCapacity() 
    {
        return capacity;
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

    public List<Bed> getBedList()
    {
        return bedList;
    }

    public void setBedList(List<Bed> bedList)
    {
        this.bedList = bedList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomId", getRoomId())
            .append("roomNumber", getRoomNumber())
            .append("floor", getFloor())
            .append("capacity", getCapacity())
            .append("imageUrl", getImageUrl())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("bedList", getBedList())
            .toString();
    }
}
