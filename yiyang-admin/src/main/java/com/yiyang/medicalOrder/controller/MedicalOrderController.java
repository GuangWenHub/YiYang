package com.yiyang.medicalOrder.controller;

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
import com.yiyang.medicalOrder.domain.MedicalOrder;
import com.yiyang.medicalOrder.service.IMedicalOrderService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 医嘱开立Controller
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
@RestController
@RequestMapping("/medicalOrder/medicalOrder")
public class MedicalOrderController extends BaseController
{
    @Autowired
    private IMedicalOrderService medicalOrderService;

    /**
     * 查询医嘱开立列表
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(MedicalOrder medicalOrder)
    {
        startPage();
        List<MedicalOrder> list = medicalOrderService.selectMedicalOrderList(medicalOrder);
        return getDataTable(list);
    }

    /**
     * 导出医嘱开立列表
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:export')")
    @Log(title = "医嘱开立", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MedicalOrder medicalOrder)
    {
        List<MedicalOrder> list = medicalOrderService.selectMedicalOrderList(medicalOrder);
        ExcelUtil<MedicalOrder> util = new ExcelUtil<MedicalOrder>(MedicalOrder.class);
        util.exportExcel(response, list, "医嘱开立数据");
    }

    /**
     * 获取医嘱开立详细信息
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(medicalOrderService.selectMedicalOrderByOrderId(orderId));
    }

    /**
     * 新增医嘱开立
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:add')")
    @Log(title = "医嘱开立", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MedicalOrder medicalOrder)
    {
        return toAjax(medicalOrderService.insertMedicalOrder(medicalOrder));
    }

    /**
     * 修改医嘱开立
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:edit')")
    @Log(title = "医嘱开立", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MedicalOrder medicalOrder)
    {
        return toAjax(medicalOrderService.updateMedicalOrder(medicalOrder));
    }

    /**
     * 删除医嘱开立
     */
    @PreAuthorize("@ss.hasPermi('medicalOrder:medicalOrder:remove')")
    @Log(title = "医嘱开立", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(medicalOrderService.deleteMedicalOrderByOrderIds(orderIds));
    }
}
