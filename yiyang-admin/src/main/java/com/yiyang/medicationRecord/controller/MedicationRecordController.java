package com.yiyang.medicationRecord.controller;

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
import com.yiyang.medicationRecord.domain.MedicationRecord;
import com.yiyang.medicationRecord.service.IMedicationRecordService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 用药跟踪Controller
 * 
 * @author GuangWen
 * @date 2026-03-19
 */
@RestController
@RequestMapping("/medicationRecord/medicationRecord")
public class MedicationRecordController extends BaseController
{
    @Autowired
    private IMedicationRecordService medicationRecordService;

    /**
     * 查询用药跟踪列表
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(MedicationRecord medicationRecord)
    {
        startPage();
        List<MedicationRecord> list = medicationRecordService.selectMedicationRecordList(medicationRecord);
        return getDataTable(list);
    }

    /**
     * 导出用药跟踪列表
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:export')")
    @Log(title = "用药跟踪", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MedicationRecord medicationRecord)
    {
        List<MedicationRecord> list = medicationRecordService.selectMedicationRecordList(medicationRecord);
        ExcelUtil<MedicationRecord> util = new ExcelUtil<MedicationRecord>(MedicationRecord.class);
        util.exportExcel(response, list, "用药跟踪数据");
    }

    /**
     * 获取用药跟踪详细信息
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(medicationRecordService.selectMedicationRecordByRecordId(recordId));
    }

    /**
     * 新增用药跟踪
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:add')")
    @Log(title = "用药跟踪", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MedicationRecord medicationRecord)
    {
        return toAjax(medicationRecordService.insertMedicationRecord(medicationRecord));
    }

    /**
     * 修改用药跟踪
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:edit')")
    @Log(title = "用药跟踪", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MedicationRecord medicationRecord)
    {
        return toAjax(medicationRecordService.updateMedicationRecord(medicationRecord));
    }

    /**
     * 删除用药跟踪
     */
    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:remove')")
    @Log(title = "用药跟踪", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(medicationRecordService.deleteMedicationRecordByRecordIds(recordIds));
    }
}
