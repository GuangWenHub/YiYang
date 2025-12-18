package com.yiyang.serviceItem.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.serviceItem.mapper.ServiceItemMapper;
import com.yiyang.serviceItem.domain.ServiceItem;
import com.yiyang.serviceItem.service.IServiceItemService;

/**
 * 服务项目库Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class ServiceItemServiceImpl implements IServiceItemService 
{
    @Autowired
    private ServiceItemMapper serviceItemMapper;

    /**
     * 查询服务项目库
     * 
     * @param itemId 服务项目库主键
     * @return 服务项目库
     */
    @Override
    public ServiceItem selectServiceItemByItemId(Long itemId)
    {
        return serviceItemMapper.selectServiceItemByItemId(itemId);
    }

    /**
     * 查询服务项目库列表
     * 
     * @param serviceItem 服务项目库
     * @return 服务项目库
     */
    @Override
    public List<ServiceItem> selectServiceItemList(ServiceItem serviceItem)
    {
        return serviceItemMapper.selectServiceItemList(serviceItem);
    }

    /**
     * 新增服务项目库
     * 
     * @param serviceItem 服务项目库
     * @return 结果
     */
    @Override
    public int insertServiceItem(ServiceItem serviceItem)
    {
        serviceItem.setCreateTime(DateUtils.getNowDate());
        return serviceItemMapper.insertServiceItem(serviceItem);
    }

    /**
     * 修改服务项目库
     * 
     * @param serviceItem 服务项目库
     * @return 结果
     */
    @Override
    public int updateServiceItem(ServiceItem serviceItem)
    {
        serviceItem.setUpdateTime(DateUtils.getNowDate());
        return serviceItemMapper.updateServiceItem(serviceItem);
    }

    /**
     * 批量删除服务项目库
     * 
     * @param itemIds 需要删除的服务项目库主键
     * @return 结果
     */
    @Override
    public int deleteServiceItemByItemIds(Long[] itemIds)
    {
        return serviceItemMapper.deleteServiceItemByItemIds(itemIds);
    }

    /**
     * 删除服务项目库信息
     * 
     * @param itemId 服务项目库主键
     * @return 结果
     */
    @Override
    public int deleteServiceItemByItemId(Long itemId)
    {
        return serviceItemMapper.deleteServiceItemByItemId(itemId);
    }
}
