package com.yiyang.elderly.controller;

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
import com.yiyang.elderly.domain.Elderly;
import com.yiyang.elderly.service.IElderlyService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 老人档案Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/elderly/elderly")
public class ElderlyController extends BaseController
{
    @Autowired
    private IElderlyService elderlyService;

    /**
     * 查询老人档案列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:list')")
    @GetMapping("/list")
    public TableDataInfo list(Elderly elderly)
    {
        startPage();
        List<Elderly> list = elderlyService.selectElderlyList(elderly);
        return getDataTable(list);
    }

    /**
     * 导出老人档案列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:export')")
    @Log(title = "老人档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Elderly elderly)
    {
        List<Elderly> list = elderlyService.selectElderlyList(elderly);
        ExcelUtil<Elderly> util = new ExcelUtil<Elderly>(Elderly.class);
        util.exportExcel(response, list, "老人档案数据");
    }

    /**
     * 获取老人档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:query')")
    @GetMapping(value = "/{elderlyId}")
    public AjaxResult getInfo(@PathVariable("elderlyId") Long elderlyId)
    {
        return success(elderlyService.selectElderlyByElderlyId(elderlyId));
    }

    /**
     * 新增老人档案
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:add')")
    @Log(title = "老人档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Elderly elderly)
    {
        return toAjax(elderlyService.insertElderly(elderly));
    }

    /**
     * 修改老人档案
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:edit')")
    @Log(title = "老人档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Elderly elderly)
    {
        return toAjax(elderlyService.updateElderly(elderly));
    }

    /**
     * 删除老人档案
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:remove')")
    @Log(title = "老人档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{elderlyIds}")
    public AjaxResult remove(@PathVariable Long[] elderlyIds)
    {
        return toAjax(elderlyService.deleteElderlyByElderlyIds(elderlyIds));
    }
}
