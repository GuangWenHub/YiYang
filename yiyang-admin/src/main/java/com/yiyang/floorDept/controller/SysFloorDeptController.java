package com.yiyang.floorDept.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.yiyang.floorDept.domain.SysFloorDept;
import com.yiyang.floorDept.service.ISysFloorDeptService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 楼层科室管理Controller
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
@RestController
@RequestMapping("/floorDept/floorDept")
public class SysFloorDeptController extends BaseController
{
    @Autowired
    private ISysFloorDeptService sysFloorDeptService;

    /**
     * 查询楼层科室管理列表
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFloorDept sysFloorDept)
    {
        startPage();
        List<SysFloorDept> list = sysFloorDeptService.selectSysFloorDeptList(sysFloorDept);
        return getDataTable(list);
    }

    /**
     * 导出楼层科室管理列表
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:export')")
    @Log(title = "楼层科室管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysFloorDept sysFloorDept)
    {
        List<SysFloorDept> list = sysFloorDeptService.selectSysFloorDeptList(sysFloorDept);
        ExcelUtil<SysFloorDept> util = new ExcelUtil<SysFloorDept>(SysFloorDept.class);
        return util.exportExcel(list, "楼层科室管理数据");
    }

    /**
     * 获取楼层科室管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:query')")
    @GetMapping(value = "/{floorDeptId}")
    public AjaxResult getInfo(@PathVariable("floorDeptId") Long floorDeptId)
    {
        return success(sysFloorDeptService.selectSysFloorDeptById(floorDeptId));
    }

    /**
     * 新增楼层科室管理
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:add')")
    @Log(title = "楼层科室管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFloorDept sysFloorDept)
    {
        return toAjax(sysFloorDeptService.insertSysFloorDept(sysFloorDept));
    }

    /**
     * 修改楼层科室管理
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:edit')")
    @Log(title = "楼层科室管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFloorDept sysFloorDept)
    {
        return toAjax(sysFloorDeptService.updateSysFloorDept(sysFloorDept));
    }

    /**
     * 删除楼层科室管理
     */
    @PreAuthorize("@ss.hasPermi('floorDept:floorDept:remove')")
    @Log(title = "楼层科室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{floorDeptIds}")
    public AjaxResult remove(@PathVariable Long[] floorDeptIds)
    {
        return toAjax(sysFloorDeptService.deleteSysFloorDeptByIds(floorDeptIds));
    }
}
