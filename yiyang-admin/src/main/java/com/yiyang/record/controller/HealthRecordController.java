package com.yiyang.record.controller;

import com.yiyang.common.annotation.Log;
import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import com.yiyang.common.core.page.TableDataInfo;
import com.yiyang.common.enums.BusinessType;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.record.domain.HealthRecord;
import com.yiyang.record.service.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 健康档案记录Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/record/record")
public class HealthRecordController extends BaseController
{
    @Autowired
    private IHealthRecordService healthRecordService;

    /**
     * 查询健康档案记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(HealthRecord healthRecord)
    {
        startPage();
        List<HealthRecord> list = healthRecordService.selectHealthRecordList(healthRecord);
        return getDataTable(list);
    }

    /**
     * 导出健康档案记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:record:export')")
    @Log(title = "健康档案记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HealthRecord healthRecord)
    {
        List<HealthRecord> list = healthRecordService.selectHealthRecordList(healthRecord);
        ExcelUtil<HealthRecord> util = new ExcelUtil<HealthRecord>(HealthRecord.class);
        util.exportExcel(response, list, "健康档案记录数据");
    }

    /**
     * 获取健康档案记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('record:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(healthRecordService.selectHealthRecordByRecordId(recordId));
    }

    /**
     * 新增健康档案记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:add')")
    @Log(title = "健康档案记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HealthRecord healthRecord)
    {
        return toAjax(healthRecordService.insertHealthRecord(healthRecord));
    }

    /**
     * 修改健康档案记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:edit')")
    @Log(title = "健康档案记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HealthRecord healthRecord)
    {
        return toAjax(healthRecordService.updateHealthRecord(healthRecord));
    }

    /**
     * 删除健康档案记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:remove')")
    @Log(title = "健康档案记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(healthRecordService.deleteHealthRecordByRecordIds(recordIds));
    }
}
