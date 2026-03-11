package com.yiyang.room.service;

import java.util.List;
import com.yiyang.room.domain.Room;

/**
 * 房间信息Service接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface IRoomService 
{
    /**
     * 查询房间信息
     * 
     * @param roomId 房间信息主键
     * @return 房间信息
     */
    public Room selectRoomByRoomId(Long roomId);

    /**
     * 查询房间信息列表
     * 
     * @param room 房间信息
     * @return 房间信息集合
     */
    public List<Room> selectRoomList(Room room);

    /**
     * 新增房间信息
     * 
     * @param room 房间信息
     * @return 结果
     */
    public int insertRoom(Room room);

    /**
     * 修改房间信息
     * 
     * @param room 房间信息
     * @return 结果
     */
    public int updateRoom(Room room);

    /**
     * 批量删除房间信息
     * 
     * @param roomIds 需要删除的房间信息主键集合
     * @return 结果
     */
    public int deleteRoomByRoomIds(Long[] roomIds);

    /**
     * 删除房间信息信息
     * 
     * @param roomId 房间信息主键
     * @return 结果
     */
    public int deleteRoomByRoomId(Long roomId);
    
    /**
     * 根据老人ID查询床位信息
     * 
     * @param elderlyId 老人ID
     * @return 床位信息
     */
    public com.yiyang.room.domain.Bed selectBedByElderlyId(Long elderlyId);
}
