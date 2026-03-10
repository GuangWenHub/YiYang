package com.yiyang.detail.controller;

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
import com.yiyang.detail.domain.ProjectOrderDetail;
import com.yiyang.detail.service.IProjectOrderDetailService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 项目单明细Controller
 * 
 * @author GuangWen
 * @date 2026-03-10
 */
@RestController
@RequestMapping("/detail/detail")
public class ProjectOrderDetailController extends BaseController
{
    @Autowired
    private IProjectOrderDetailService projectOrderDetailService;

    /**
     * 查询项目单明细列表
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProjectOrderDetail projectOrderDetail)
    {
        startPage();
        List<ProjectOrderDetail> list = projectOrderDetailService.selectProjectOrderDetailList(projectOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出项目单明细列表
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:export')")
    @Log(title = "项目单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProjectOrderDetail projectOrderDetail)
    {
        List<ProjectOrderDetail> list = projectOrderDetailService.selectProjectOrderDetailList(projectOrderDetail);
        ExcelUtil<ProjectOrderDetail> util = new ExcelUtil<ProjectOrderDetail>(ProjectOrderDetail.class);
        util.exportExcel(response, list, "项目单明细数据");
    }

    /**
     * 获取项目单明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:query')")
    @GetMapping(value = "/{detailId}")
    public AjaxResult getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(projectOrderDetailService.selectProjectOrderDetailByDetailId(detailId));
    }

    /**
     * 新增项目单明细
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:add')")
    @Log(title = "项目单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProjectOrderDetail projectOrderDetail)
    {
        return toAjax(projectOrderDetailService.insertProjectOrderDetail(projectOrderDetail));
    }

    /**
     * 修改项目单明细
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:edit')")
    @Log(title = "项目单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProjectOrderDetail projectOrderDetail)
    {
        return toAjax(projectOrderDetailService.updateProjectOrderDetail(projectOrderDetail));
    }

    /**
     * 删除项目单明细
     */
    @PreAuthorize("@ss.hasPermi('detail:detail:remove')")
    @Log(title = "项目单明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailIds}")
    public AjaxResult remove(@PathVariable Long[] detailIds)
    {
        return toAjax(projectOrderDetailService.deleteProjectOrderDetailByDetailIds(detailIds));
    }
}
