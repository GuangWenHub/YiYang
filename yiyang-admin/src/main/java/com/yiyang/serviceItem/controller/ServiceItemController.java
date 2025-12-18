package com.yiyang.serviceItem.controller;

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
import com.yiyang.serviceItem.domain.ServiceItem;
import com.yiyang.serviceItem.service.IServiceItemService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 服务项目库Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/serviceItem/serviceItem")
public class ServiceItemController extends BaseController
{
    @Autowired
    private IServiceItemService serviceItemService;

    /**
     * 查询服务项目库列表
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceItem serviceItem)
    {
        startPage();
        List<ServiceItem> list = serviceItemService.selectServiceItemList(serviceItem);
        return getDataTable(list);
    }

    /**
     * 导出服务项目库列表
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:export')")
    @Log(title = "服务项目库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceItem serviceItem)
    {
        List<ServiceItem> list = serviceItemService.selectServiceItemList(serviceItem);
        ExcelUtil<ServiceItem> util = new ExcelUtil<ServiceItem>(ServiceItem.class);
        util.exportExcel(response, list, "服务项目库数据");
    }

    /**
     * 获取服务项目库详细信息
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:query')")
    @GetMapping(value = "/{itemId}")
    public AjaxResult getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(serviceItemService.selectServiceItemByItemId(itemId));
    }

    /**
     * 新增服务项目库
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:add')")
    @Log(title = "服务项目库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceItem serviceItem)
    {
        return toAjax(serviceItemService.insertServiceItem(serviceItem));
    }

    /**
     * 修改服务项目库
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:edit')")
    @Log(title = "服务项目库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceItem serviceItem)
    {
        return toAjax(serviceItemService.updateServiceItem(serviceItem));
    }

    /**
     * 删除服务项目库
     */
    @PreAuthorize("@ss.hasPermi('serviceItem:serviceItem:remove')")
    @Log(title = "服务项目库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{itemIds}")
    public AjaxResult remove(@PathVariable Long[] itemIds)
    {
        return toAjax(serviceItemService.deleteServiceItemByItemIds(itemIds));
    }
}
