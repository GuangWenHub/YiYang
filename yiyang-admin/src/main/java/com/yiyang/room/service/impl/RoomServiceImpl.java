package com.yiyang.room.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.yiyang.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.yiyang.room.domain.Bed;
import com.yiyang.room.mapper.RoomMapper;
import com.yiyang.room.domain.Room;
import com.yiyang.room.service.IRoomService;

/**
 * 房间信息Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class RoomServiceImpl implements IRoomService 
{
    @Autowired
    private RoomMapper roomMapper;

    /**
     * 查询房间信息
     * 
     * @param roomId 房间信息主键
     * @return 房间信息
     */
    @Override
    public Room selectRoomByRoomId(Long roomId)
    {
        return roomMapper.selectRoomByRoomId(roomId);
    }

    /**
     * 查询房间信息列表
     * 
     * @param room 房间信息
     * @return 房间信息
     */
    @Override
    public List<Room> selectRoomList(Room room)
    {
        return roomMapper.selectRoomList(room);
    }

    /**
     * 新增房间信息
     * 
     * @param room 房间信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRoom(Room room)
    {
        room.setCreateTime(DateUtils.getNowDate());
        int rows = roomMapper.insertRoom(room);
        insertBed(room);
        return rows;
    }

    /**
     * 修改房间信息
     * 
     * @param room 房间信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRoom(Room room)
    {
        room.setUpdateTime(DateUtils.getNowDate());
        roomMapper.deleteBedByRoomId(room.getRoomId());
        insertBed(room);
        return roomMapper.updateRoom(room);
    }

    /**
     * 批量删除房间信息
     * 
     * @param roomIds 需要删除的房间信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRoomByRoomIds(Long[] roomIds)
    {
        roomMapper.deleteBedByRoomIds(roomIds);
        return roomMapper.deleteRoomByRoomIds(roomIds);
    }

    /**
     * 删除房间信息信息
     * 
     * @param roomId 房间信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRoomByRoomId(Long roomId)
    {
        roomMapper.deleteBedByRoomId(roomId);
        return roomMapper.deleteRoomByRoomId(roomId);
    }
    
    /**
     * 根据老人ID查询床位信息
     * 
     * @param elderlyId 老人ID
     * @return 床位信息
     */
    @Override
    public Bed selectBedByElderlyId(Long elderlyId)
    {
        return roomMapper.selectBedByElderlyId(elderlyId);
    }

    /**
     * 新增床位信息信息
     * 
     * @param room 房间信息对象
     */
    public void insertBed(Room room)
    {
        List<Bed> bedList = room.getBedList();
        Long roomId = room.getRoomId();
        if (StringUtils.isNotNull(bedList))
        {
            List<Bed> list = new ArrayList<Bed>();
            for (Bed bed : bedList)
            {
                bed.setRoomId(roomId);
                list.add(bed);
            }
            if (list.size() > 0)
            {
                roomMapper.batchBed(list);
            }
        }
    }
}
