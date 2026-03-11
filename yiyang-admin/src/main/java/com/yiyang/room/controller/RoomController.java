package com.yiyang.room.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yiyang.common.annotation.Log;
import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import com.yiyang.common.enums.BusinessType;
import com.yiyang.room.domain.Room;
import com.yiyang.room.service.IRoomService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 房间信息Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/room/room")
public class RoomController extends BaseController
{
    @Autowired
    private IRoomService roomService;

    /**
     * 查询房间信息列表
     */
    @PreAuthorize("@ss.hasPermi('room:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(Room room)
    {
        startPage();
        List<Room> list = roomService.selectRoomList(room);
        return getDataTable(list);
    }

    /**
     * 导出房间信息列表
     */
    @PreAuthorize("@ss.hasPermi('room:room:export')")
    @Log(title = "房间信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Room room)
    {
        List<Room> list = roomService.selectRoomList(room);
        ExcelUtil<Room> util = new ExcelUtil<Room>(Room.class);
        util.exportExcel(response, list, "房间信息数据");
    }

    /**
     * 获取房间信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('room:room:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId)
    {
        return success(roomService.selectRoomByRoomId(roomId));
    }

    /**
     * 新增房间信息
     */
    @PreAuthorize("@ss.hasPermi('room:room:add')")
    @Log(title = "房间信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Room room)
    {
        return toAjax(roomService.insertRoom(room));
    }

    /**
     * 修改房间信息
     */
    @PreAuthorize("@ss.hasPermi('room:room:edit')")
    @Log(title = "房间信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Room room)
    {
        return toAjax(roomService.updateRoom(room));
    }

    /**
     * 删除房间信息
     */
    @PreAuthorize("@ss.hasPermi('room:room:remove')")
    @Log(title = "房间信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds)
    {
        return toAjax(roomService.deleteRoomByRoomIds(roomIds));
    }
    
    /**
     * 根据老人ID查询床位信息
     */
    @GetMapping("/bed/{elderlyId}")
    public AjaxResult getBedByElderlyId(@PathVariable("elderlyId") Long elderlyId)
    {
        return success(roomService.selectBedByElderlyId(elderlyId));
    }
}
