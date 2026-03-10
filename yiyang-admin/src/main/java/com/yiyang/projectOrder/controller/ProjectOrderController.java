package com.yiyang.projectOrder.controller;

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
import com.yiyang.projectOrder.domain.ProjectOrder;
import com.yiyang.projectOrder.service.IProjectOrderService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 项目单主Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/projectOrder/projectOrder")
public class ProjectOrderController extends BaseController
{
    @Autowired
    private IProjectOrderService projectOrderService;

    /**
     * 查询项目单主列表
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProjectOrder projectOrder)
    {
        startPage();
        List<ProjectOrder> list = projectOrderService.selectProjectOrderList(projectOrder);
        return getDataTable(list);
    }

    /**
     * 导出项目单主列表
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:export')")
    @Log(title = "项目单主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProjectOrder projectOrder)
    {
        List<ProjectOrder> list = projectOrderService.selectProjectOrderList(projectOrder);
        ExcelUtil<ProjectOrder> util = new ExcelUtil<ProjectOrder>(ProjectOrder.class);
        util.exportExcel(response, list, "项目单主数据");
    }

    /**
     * 获取项目单主详细信息
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(projectOrderService.selectProjectOrderByOrderId(orderId));
    }

    /**
     * 新增项目单主
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:add')")
    @Log(title = "项目单主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProjectOrder projectOrder)
    {
        return toAjax(projectOrderService.insertProjectOrder(projectOrder));
    }

    /**
     * 修改项目单主
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:edit')")
    @Log(title = "项目单主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProjectOrder projectOrder)
    {
        return toAjax(projectOrderService.updateProjectOrder(projectOrder));
    }

    /**
     * 删除项目单主
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:remove')")
    @Log(title = "项目单主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(projectOrderService.deleteProjectOrderByOrderIds(orderIds));
    }

    /**
     * 审核项目单
     */
    @PreAuthorize("@ss.hasPermi('projectOrder:projectOrder:audit')")
    @Log(title = "项目单主", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody ProjectOrder projectOrder)
    {
        Long caregiverId = null;
        if (projectOrder.getOrderDetails() != null && !projectOrder.getOrderDetails().isEmpty()) {
            caregiverId = projectOrder.getOrderDetails().get(0).getCreatorId();
        }
        return toAjax(projectOrderService.auditProjectOrder(projectOrder, caregiverId));
    }
}
