package com.yiyang.room.mapper;

import java.util.List;
import com.yiyang.room.domain.Room;
import com.yiyang.room.domain.Bed;

/**
 * 房间信息Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface RoomMapper 
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
     * 删除房间信息
     * 
     * @param roomId 房间信息主键
     * @return 结果
     */
    public int deleteRoomByRoomId(Long roomId);

    /**
     * 批量删除房间信息
     * 
     * @param roomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRoomByRoomIds(Long[] roomIds);

    /**
     * 批量删除床位信息
     * 
     * @param roomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBedByRoomIds(Long[] roomIds);
    
    /**
     * 批量新增床位信息
     * 
     * @param bedList 床位信息列表
     * @return 结果
     */
    public int batchBed(List<Bed> bedList);
    

    /**
     * 通过房间信息主键删除床位信息信息
     * 
     * @param roomId 房间信息ID
     * @return 结果
     */
    public int deleteBedByRoomId(Long roomId);
    
    /**
     * 根据老人ID查询床位信息
     * 
     * @param elderlyId 老人ID
     * @return 床位信息
     */
    public Bed selectBedByElderlyId(Long elderlyId);
}
