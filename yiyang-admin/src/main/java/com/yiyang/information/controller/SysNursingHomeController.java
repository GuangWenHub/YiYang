package com.yiyang.information.controller;

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
import com.yiyang.information.domain.SysNursingHome;
import com.yiyang.information.service.ISysNursingHomeService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 养老院基本信息Controller
 * 
 * @author Guangwen
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/information/information")
public class SysNursingHomeController extends BaseController
{
    @Autowired
    private ISysNursingHomeService sysNursingHomeService;

    /**
     * 查询养老院基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('information:information:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNursingHome sysNursingHome)
    {
        startPage();
        List<SysNursingHome> list = sysNursingHomeService.selectSysNursingHomeList(sysNursingHome);
        return getDataTable(list);
    }

    /**
     * 导出养老院基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('information:information:export')")
    @Log(title = "养老院基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNursingHome sysNursingHome)
    {
        List<SysNursingHome> list = sysNursingHomeService.selectSysNursingHomeList(sysNursingHome);
        ExcelUtil<SysNursingHome> util = new ExcelUtil<SysNursingHome>(SysNursingHome.class);
        util.exportExcel(response, list, "养老院基本信息数据");
    }

    /**
     * 获取养老院基本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('information:information:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysNursingHomeService.selectSysNursingHomeById(id));
    }

    /**
     * 新增养老院基本信息
     */
    @PreAuthorize("@ss.hasPermi('information:information:add')")
    @Log(title = "养老院基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNursingHome sysNursingHome)
    {
        return toAjax(sysNursingHomeService.insertSysNursingHome(sysNursingHome));
    }

    /**
     * 修改养老院基本信息
     */
    @PreAuthorize("@ss.hasPermi('information:information:edit')")
    @Log(title = "养老院基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNursingHome sysNursingHome)
    {
        return toAjax(sysNursingHomeService.updateSysNursingHome(sysNursingHome));
    }

    /**
     * 删除养老院基本信息
     */
    @PreAuthorize("@ss.hasPermi('information:information:remove')")
    @Log(title = "养老院基本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysNursingHomeService.deleteSysNursingHomeByIds(ids));
    }
}
