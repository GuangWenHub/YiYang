package com.yiyang.projectOrder.service.impl;

import java.util.Date;
import java.util.List;
import com.yiyang.common.utils.DateUtils;
import com.yiyang.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yiyang.projectOrder.mapper.ProjectOrderMapper;
import com.yiyang.projectOrder.domain.ProjectOrder;
import com.yiyang.detail.domain.ProjectOrderDetail;
import com.yiyang.projectOrder.service.IProjectOrderService;
import com.yiyang.detail.service.IProjectOrderDetailService;

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

    @Autowired
    private IProjectOrderDetailService projectOrderDetailService;

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
    @Transactional
    public int deleteProjectOrderByOrderIds(Long[] orderIds)
    {
        for (Long orderId : orderIds) {
            // 删除子表记录
            projectOrderDetailService.deleteProjectOrderDetailByOrderId(orderId);
        }
        // 删除主表记录
        return projectOrderMapper.deleteProjectOrderByOrderIds(orderIds);
    }

    /**
     * 删除项目单主信息
     * 
     * @param orderId 项目单主主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteProjectOrderByOrderId(Long orderId)
    {
        // 删除子表记录
        projectOrderDetailService.deleteProjectOrderDetailByOrderId(orderId);
        // 删除主表记录
        return projectOrderMapper.deleteProjectOrderByOrderId(orderId);
    }

    /**
     * 审核项目单
     * 
     * @param projectOrder 项目单信息
     * @param caregiverId 护工ID
     * @return 结果
     */
    @Override
    @Transactional
    public int auditProjectOrder(ProjectOrder projectOrder, Long caregiverId)
    {
        Long orderId = projectOrder.getOrderId();
        
        projectOrder.setUpdateTime(DateUtils.getNowDate());
        
        if (projectOrder.getStatus() == 2) {
            projectOrder.setApprovedTime(new Date());
        }
        
        projectOrder.setAuditorId(SecurityUtils.getUserId());
        
        int result = projectOrderMapper.updateProjectOrder(projectOrder);
        
        if (caregiverId != null) {
            List<ProjectOrderDetail> details = projectOrderDetailService.selectProjectOrderDetailListByOrderId(orderId);
            
            if (details != null && !details.isEmpty()) {
                for (ProjectOrderDetail detail : details) {
                    detail.setCreatorId(caregiverId);
                    projectOrderDetailService.updateProjectOrderDetail(detail);
                }
            } else {
                ProjectOrderDetail newDetail = new ProjectOrderDetail();
                newDetail.setOrderId(orderId);
                newDetail.setCreatorId(caregiverId);
                newDetail.setStatus(0L);
                projectOrderDetailService.insertProjectOrderDetail(newDetail);
            }
        }
        
        return result;
    }
}
