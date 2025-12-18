package com.yiyang.projectOrder.service;

import java.util.List;
import com.yiyang.projectOrder.domain.ProjectOrder;

/**
 * 项目单主Service接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface IProjectOrderService 
{
    /**
     * 查询项目单主
     * 
     * @param orderId 项目单主主键
     * @return 项目单主
     */
    public ProjectOrder selectProjectOrderByOrderId(Long orderId);

    /**
     * 查询项目单主列表
     * 
     * @param projectOrder 项目单主
     * @return 项目单主集合
     */
    public List<ProjectOrder> selectProjectOrderList(ProjectOrder projectOrder);

    /**
     * 新增项目单主
     * 
     * @param projectOrder 项目单主
     * @return 结果
     */
    public int insertProjectOrder(ProjectOrder projectOrder);

    /**
     * 修改项目单主
     * 
     * @param projectOrder 项目单主
     * @return 结果
     */
    public int updateProjectOrder(ProjectOrder projectOrder);

    /**
     * 批量删除项目单主
     * 
     * @param orderIds 需要删除的项目单主主键集合
     * @return 结果
     */
    public int deleteProjectOrderByOrderIds(Long[] orderIds);

    /**
     * 删除项目单主信息
     * 
     * @param orderId 项目单主主键
     * @return 结果
     */
    public int deleteProjectOrderByOrderId(Long orderId);
}
