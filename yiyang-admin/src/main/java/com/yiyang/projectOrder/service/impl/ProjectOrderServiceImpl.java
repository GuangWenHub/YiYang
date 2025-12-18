package com.yiyang.projectOrder.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.projectOrder.mapper.ProjectOrderMapper;
import com.yiyang.projectOrder.domain.ProjectOrder;
import com.yiyang.projectOrder.service.IProjectOrderService;

/**
 * 项目单主Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class ProjectOrderServiceImpl implements IProjectOrderService 
{
    @Autowired
    private ProjectOrderMapper projectOrderMapper;

    /**
     * 查询项目单主
     * 
     * @param orderId 项目单主主键
     * @return 项目单主
     */
    @Override
    public ProjectOrder selectProjectOrderByOrderId(Long orderId)
    {
        return projectOrderMapper.selectProjectOrderByOrderId(orderId);
    }

    /**
     * 查询项目单主列表
     * 
     * @param projectOrder 项目单主
     * @return 项目单主
     */
    @Override
    public List<ProjectOrder> selectProjectOrderList(ProjectOrder projectOrder)
    {
        return projectOrderMapper.selectProjectOrderList(projectOrder);
    }

    /**
     * 新增项目单主
     * 
     * @param projectOrder 项目单主
     * @return 结果
     */
    @Override
    public int insertProjectOrder(ProjectOrder projectOrder)
    {
        projectOrder.setCreateTime(DateUtils.getNowDate());
        return projectOrderMapper.insertProjectOrder(projectOrder);
    }

    /**
     * 修改项目单主
     * 
     * @param projectOrder 项目单主
     * @return 结果
     */
    @Override
    public int updateProjectOrder(ProjectOrder projectOrder)
    {
        projectOrder.setUpdateTime(DateUtils.getNowDate());
        return projectOrderMapper.updateProjectOrder(projectOrder);
    }

    /**
     * 批量删除项目单主
     * 
     * @param orderIds 需要删除的项目单主主键
     * @return 结果
     */
    @Override
    public int deleteProjectOrderByOrderIds(Long[] orderIds)
    {
        return projectOrderMapper.deleteProjectOrderByOrderIds(orderIds);
    }

    /**
     * 删除项目单主信息
     * 
     * @param orderId 项目单主主键
     * @return 结果
     */
    @Override
    public int deleteProjectOrderByOrderId(Long orderId)
    {
        return projectOrderMapper.deleteProjectOrderByOrderId(orderId);
    }
}
