package com.yiyang.medicine.controller;

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
import com.yiyang.medicine.domain.Medicine;
import com.yiyang.medicine.service.IMedicineService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 药品管理Controller
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
@RestController
@RequestMapping("/medicine/medicine")
public class MedicineController extends BaseController
{
    @Autowired
    private IMedicineService medicineService;

    /**
     * 查询药品管理列表
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:list')")
    @GetMapping("/list")
    public TableDataInfo list(Medicine medicine)
    {
        startPage();
        List<Medicine> list = medicineService.selectMedicineList(medicine);
        return getDataTable(list);
    }

    /**
     * 导出药品管理列表
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:export')")
    @Log(title = "药品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Medicine medicine)
    {
        List<Medicine> list = medicineService.selectMedicineList(medicine);
        ExcelUtil<Medicine> util = new ExcelUtil<Medicine>(Medicine.class);
        util.exportExcel(response, list, "药品管理数据");
    }

    /**
     * 获取药品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:query')")
    @GetMapping(value = "/{medicineId}")
    public AjaxResult getInfo(@PathVariable("medicineId") Long medicineId)
    {
        return success(medicineService.selectMedicineByMedicineId(medicineId));
    }

    /**
     * 新增药品管理
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:add')")
    @Log(title = "药品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Medicine medicine)
    {
        return toAjax(medicineService.insertMedicine(medicine));
    }

    /**
     * 修改药品管理
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:edit')")
    @Log(title = "药品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Medicine medicine)
    {
        return toAjax(medicineService.updateMedicine(medicine));
    }

    /**
     * 删除药品管理
     */
    @PreAuthorize("@ss.hasPermi('medicine:medicine:remove')")
    @Log(title = "药品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{medicineIds}")
    public AjaxResult remove(@PathVariable Long[] medicineIds)
    {
        return toAjax(medicineService.deleteMedicineByMedicineIds(medicineIds));
    }
}
