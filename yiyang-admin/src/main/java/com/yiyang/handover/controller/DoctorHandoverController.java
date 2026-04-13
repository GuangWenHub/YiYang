package com.yiyang.handover.controller;

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
import com.yiyang.handover.domain.DoctorHandover;
import com.yiyang.handover.service.IDoctorHandoverService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 医生交班记录Controller
 * 
 * @author GuangWen
 * @date 2026-03-28
 */
@RestController
@RequestMapping("/handover/handover")
public class DoctorHandoverController extends BaseController
{
    @Autowired
    private IDoctorHandoverService doctorHandoverService;

    /**
     * 查询医生交班记录列表
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:list')")
    @GetMapping("/list")
    public TableDataInfo list(DoctorHandover doctorHandover)
    {
        startPage();
        List<DoctorHandover> list = doctorHandoverService.selectDoctorHandoverList(doctorHandover);
        return getDataTable(list);
    }

    /**
     * 导出医生交班记录列表
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:export')")
    @Log(title = "医生交班记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DoctorHandover doctorHandover)
    {
        List<DoctorHandover> list = doctorHandoverService.selectDoctorHandoverList(doctorHandover);
        ExcelUtil<DoctorHandover> util = new ExcelUtil<DoctorHandover>(DoctorHandover.class);
        util.exportExcel(response, list, "医生交班记录数据");
    }

    /**
     * 获取医生交班记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:query')")
    @GetMapping(value = "/{handoverId}")
    public AjaxResult getInfo(@PathVariable("handoverId") Long handoverId)
    {
        return success(doctorHandoverService.selectDoctorHandoverByHandoverId(handoverId));
    }

    /**
     * 新增医生交班记录
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:add')")
    @Log(title = "医生交班记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DoctorHandover doctorHandover)
    {
        return toAjax(doctorHandoverService.insertDoctorHandover(doctorHandover));
    }

    /**
     * 修改医生交班记录
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:edit')")
    @Log(title = "医生交班记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DoctorHandover doctorHandover)
    {
        return toAjax(doctorHandoverService.updateDoctorHandover(doctorHandover));
    }

    /**
     * 医生交班
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:handover')")
    @Log(title = "医生交班记录", businessType = BusinessType.UPDATE)
    @PutMapping("/handover")
    public AjaxResult handover(@RequestBody DoctorHandover doctorHandover)
    {
        // 强制获取当前登录用户 ID 并设置到 nextDoctorId（双重保障）
        Long currentUserId = getUserId();
        System.out.println("当前登录用户 ID: " + currentUserId);
        System.out.println("前端传递的 nextDoctorId: " + doctorHandover.getNextDoctorId());
        
        // 覆盖前端传递的 nextDoctorId，确保使用当前登录用户的 ID
        doctorHandover.setNextDoctorId(currentUserId);
        
        // 设置状态为已交接
        doctorHandover.setStatus("1");
        
        System.out.println("最终设置的 nextDoctorId: " + doctorHandover.getNextDoctorId());
        return toAjax(doctorHandoverService.updateDoctorHandover(doctorHandover));
    }

    /**
     * 删除医生交班记录
     */
    @PreAuthorize("@ss.hasPermi('handover:handover:remove')")
    @Log(title = "医生交班记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{handoverIds}")
    public AjaxResult remove(@PathVariable Long[] handoverIds)
    {
        return toAjax(doctorHandoverService.deleteDoctorHandoverByHandoverIds(handoverIds));
    }
}
