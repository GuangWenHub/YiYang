package com.yiyang.round.controller;

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
import com.yiyang.round.domain.SysWardRound;
import com.yiyang.round.service.ISysWardRoundService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 查房记录Controller
 * 
 * @author GuangWen
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/round/round")
public class SysWardRoundController extends BaseController
{
    @Autowired
    private ISysWardRoundService sysWardRoundService;

    /**
     * 查询查房记录列表
     */
    @PreAuthorize("@ss.hasPermi('round:round:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWardRound sysWardRound)
    {
        startPage();
        List<SysWardRound> list = sysWardRoundService.selectSysWardRoundList(sysWardRound);
        return getDataTable(list);
    }

    /**
     * 导出查房记录列表
     */
    @PreAuthorize("@ss.hasPermi('round:round:export')")
    @Log(title = "查房记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysWardRound sysWardRound)
    {
        List<SysWardRound> list = sysWardRoundService.selectSysWardRoundList(sysWardRound);
        ExcelUtil<SysWardRound> util = new ExcelUtil<SysWardRound>(SysWardRound.class);
        util.exportExcel(response, list, "查房记录数据");
    }

    /**
     * 获取查房记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('round:round:query')")
    @GetMapping(value = "/{roundId}")
    public AjaxResult getInfo(@PathVariable("roundId") Long roundId)
    {
        return success(sysWardRoundService.selectSysWardRoundByRoundId(roundId));
    }

    /**
     * 新增查房记录
     */
    @PreAuthorize("@ss.hasPermi('round:round:add')")
    @Log(title = "查房记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysWardRound sysWardRound)
    {
        return toAjax(sysWardRoundService.insertSysWardRound(sysWardRound));
    }

    /**
     * 修改查房记录
     */
    @PreAuthorize("@ss.hasPermi('round:round:edit')")
    @Log(title = "查房记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysWardRound sysWardRound)
    {
        return toAjax(sysWardRoundService.updateSysWardRound(sysWardRound));
    }

    /**
     * 删除查房记录
     */
    @PreAuthorize("@ss.hasPermi('round:round:remove')")
    @Log(title = "查房记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roundIds}")
    public AjaxResult remove(@PathVariable Long[] roundIds)
    {
        return toAjax(sysWardRoundService.deleteSysWardRoundByRoundIds(roundIds));
    }
}
