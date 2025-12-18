package com.yiyang.familyElderly.controller;

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
import com.yiyang.familyElderly.domain.FamilyElderly;
import com.yiyang.familyElderly.service.IFamilyElderlyService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 家属-老人关系Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/familyElderly/familyElderly")
public class FamilyElderlyController extends BaseController
{
    @Autowired
    private IFamilyElderlyService familyElderlyService;

    /**
     * 查询家属-老人关系列表
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:list')")
    @GetMapping("/list")
    public TableDataInfo list(FamilyElderly familyElderly)
    {
        startPage();
        List<FamilyElderly> list = familyElderlyService.selectFamilyElderlyList(familyElderly);
        return getDataTable(list);
    }

    /**
     * 导出家属-老人关系列表
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:export')")
    @Log(title = "家属-老人关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FamilyElderly familyElderly)
    {
        List<FamilyElderly> list = familyElderlyService.selectFamilyElderlyList(familyElderly);
        ExcelUtil<FamilyElderly> util = new ExcelUtil<FamilyElderly>(FamilyElderly.class);
        util.exportExcel(response, list, "家属-老人关系数据");
    }

    /**
     * 获取家属-老人关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(familyElderlyService.selectFamilyElderlyById(id));
    }

    /**
     * 新增家属-老人关系
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:add')")
    @Log(title = "家属-老人关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FamilyElderly familyElderly)
    {
        return toAjax(familyElderlyService.insertFamilyElderly(familyElderly));
    }

    /**
     * 修改家属-老人关系
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:edit')")
    @Log(title = "家属-老人关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FamilyElderly familyElderly)
    {
        return toAjax(familyElderlyService.updateFamilyElderly(familyElderly));
    }

    /**
     * 删除家属-老人关系
     */
    @PreAuthorize("@ss.hasPermi('familyElderly:familyElderly:remove')")
    @Log(title = "家属-老人关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(familyElderlyService.deleteFamilyElderlyByIds(ids));
    }
}
