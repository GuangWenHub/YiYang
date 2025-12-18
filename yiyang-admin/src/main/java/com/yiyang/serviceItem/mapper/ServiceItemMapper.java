package com.yiyang.serviceItem.mapper;

import java.util.List;
import com.yiyang.serviceItem.domain.ServiceItem;

/**
 * 服务项目库Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface ServiceItemMapper 
{
    /**
     * 查询服务项目库
     * 
     * @param itemId 服务项目库主键
     * @return 服务项目库
     */
    public ServiceItem selectServiceItemByItemId(Long itemId);

    /**
     * 查询服务项目库列表
     * 
     * @param serviceItem 服务项目库
     * @return 服务项目库集合
     */
    public List<ServiceItem> selectServiceItemList(ServiceItem serviceItem);

    /**
     * 新增服务项目库
     * 
     * @param serviceItem 服务项目库
     * @return 结果
     */
    public int insertServiceItem(ServiceItem serviceItem);

    /**
     * 修改服务项目库
     * 
     * @param serviceItem 服务项目库
     * @return 结果
     */
    public int updateServiceItem(ServiceItem serviceItem);

    /**
     * 删除服务项目库
     * 
     * @param itemId 服务项目库主键
     * @return 结果
     */
    public int deleteServiceItemByItemId(Long itemId);

    /**
     * 批量删除服务项目库
     * 
     * @param itemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceItemByItemIds(Long[] itemIds);
}
