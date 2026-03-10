package com.yiyang.detail.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.detail.mapper.ProjectOrderDetailMapper;
import com.yiyang.detail.domain.ProjectOrderDetail;
import com.yiyang.detail.service.IProjectOrderDetailService;

/**
 * 项目单明细Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-10
 */
@Service("detailProjectOrderDetailServiceImpl")
public class ProjectOrderDetailServiceImpl implements IProjectOrderDetailService 
{
    @Autowired
    private ProjectOrderDetailMapper projectOrderDetailMapper;

    /**
     * 查询项目单明细
     * 
     * @param detailId 项目单明细主键
     * @return 项目单明细
     */
    @Override
    public ProjectOrderDetail selectProjectOrderDetailByDetailId(Long detailId)
    {
        return projectOrderDetailMapper.selectProjectOrderDetailByDetailId(detailId);
    }

    /**
     * 查询项目单明细列表
     * 
     * @param projectOrderDetail 项目单明细
     * @return 项目单明细
     */
    @Override
    public List<ProjectOrderDetail> selectProjectOrderDetailList(ProjectOrderDetail projectOrderDetail)
    {
        return projectOrderDetailMapper.selectProjectOrderDetailList(projectOrderDetail);
    }

    /**
     * 根据订单ID查询项目单明细列表
     * 
     * @param orderId 订单ID
     * @return 项目单明细集合
     */
    @Override
    public List<ProjectOrderDetail> selectProjectOrderDetailListByOrderId(Long orderId)
    {
        return projectOrderDetailMapper.selectProjectOrderDetailListByOrderId(orderId);
    }

    /**
     * 新增项目单明细
     * 
     * @param projectOrderDetail 项目单明细
     * @return 结果
     */
    @Override
    public int insertProjectOrderDetail(ProjectOrderDetail projectOrderDetail)
    {
        projectOrderDetail.setCreateTime(DateUtils.getNowDate());
        return projectOrderDetailMapper.insertProjectOrderDetail(projectOrderDetail);
    }

    /**
     * 修改项目单明细
     * 
     * @param projectOrderDetail 项目单明细
     * @return 结果
     */
    @Override
    public int updateProjectOrderDetail(ProjectOrderDetail projectOrderDetail)
    {
        projectOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return projectOrderDetailMapper.updateProjectOrderDetail(projectOrderDetail);
    }

    /**
     * 批量删除项目单明细
     * 
     * @param detailIds 需要删除的项目单明细主键
     * @return 结果
     */
    @Override
    public int deleteProjectOrderDetailByDetailIds(Long[] detailIds)
    {
        return projectOrderDetailMapper.deleteProjectOrderDetailByDetailIds(detailIds);
    }

    /**
     * 删除项目单明细信息
     * 
     * @param detailId 项目单明细主键
     * @return 结果
     */
    @Override
    public int deleteProjectOrderDetailByDetailId(Long detailId)
    {
        return projectOrderDetailMapper.deleteProjectOrderDetailByDetailId(detailId);
    }

    /**
     * 根据订单ID删除项目单明细
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int deleteProjectOrderDetailByOrderId(Long orderId)
    {
        return projectOrderDetailMapper.deleteProjectOrderDetailByOrderId(orderId);
    }
}
