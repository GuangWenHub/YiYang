package com.yiyang.careRecord.controller;

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
import com.yiyang.careRecord.domain.CareRecord;
import com.yiyang.careRecord.service.ICareRecordService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 生活照料记录Controller
 * 
 * @author Guangwen
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/careRecord/careRecord")
public class CareRecordController extends BaseController
{
    @Autowired
    private ICareRecordService careRecordService;

    /**
     * 查询生活照料记录列表
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(CareRecord careRecord)
    {
        startPage();
        List<CareRecord> list = careRecordService.selectCareRecordList(careRecord);
        return getDataTable(list);
    }

    /**
     * 导出生活照料记录列表
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:export')")
    @Log(title = "生活照料记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CareRecord careRecord)
    {
        List<CareRecord> list = careRecordService.selectCareRecordList(careRecord);
        ExcelUtil<CareRecord> util = new ExcelUtil<CareRecord>(CareRecord.class);
        util.exportExcel(response, list, "生活照料记录数据");
    }

    /**
     * 获取生活照料记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(careRecordService.selectCareRecordByRecordId(recordId));
    }

    /**
     * 新增生活照料记录
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:add')")
    @Log(title = "生活照料记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CareRecord careRecord)
    {
        return toAjax(careRecordService.insertCareRecord(careRecord));
    }

    /**
     * 修改生活照料记录
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:edit')")
    @Log(title = "生活照料记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CareRecord careRecord)
    {
        return toAjax(careRecordService.updateCareRecord(careRecord));
    }

    /**
     * 删除生活照料记录
     */
    @PreAuthorize("@ss.hasPermi('careRecord:careRecord:remove')")
    @Log(title = "生活照料记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(careRecordService.deleteCareRecordByRecordIds(recordIds));
    }
}
