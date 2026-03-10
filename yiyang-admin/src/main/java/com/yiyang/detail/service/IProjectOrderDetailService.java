package com.yiyang.detail.service;

import java.util.List;
import com.yiyang.detail.domain.ProjectOrderDetail;

/**
 * 项目单明细Service接口
 * 
 * @author GuangWen
 * @date 2026-03-10
 */
public interface IProjectOrderDetailService 
{
    /**
     * 查询项目单明细
     * 
     * @param detailId 项目单明细主键
     * @return 项目单明细
     */
    public ProjectOrderDetail selectProjectOrderDetailByDetailId(Long detailId);

    /**
     * 查询项目单明细列表
     * 
     * @param projectOrderDetail 项目单明细
     * @return 项目单明细集合
     */
    public List<ProjectOrderDetail> selectProjectOrderDetailList(ProjectOrderDetail projectOrderDetail);

    /**
     * 根据订单ID查询项目单明细列表
     * 
     * @param orderId 订单ID
     * @return 项目单明细集合
     */
    public List<ProjectOrderDetail> selectProjectOrderDetailListByOrderId(Long orderId);

    /**
     * 新增项目单明细
     * 
     * @param projectOrderDetail 项目单明细
     * @return 结果
     */
    public int insertProjectOrderDetail(ProjectOrderDetail projectOrderDetail);

    /**
     * 修改项目单明细
     * 
     * @param projectOrderDetail 项目单明细
     * @return 结果
     */
    public int updateProjectOrderDetail(ProjectOrderDetail projectOrderDetail);

    /**
     * 批量删除项目单明细
     * 
     * @param detailIds 需要删除的项目单明细主键集合
     * @return 结果
     */
    public int deleteProjectOrderDetailByDetailIds(Long[] detailIds);

    /**
     * 删除项目单明细信息
     * 
     * @param detailId 项目单明细主键
     * @return 结果
     */
    public int deleteProjectOrderDetailByDetailId(Long detailId);

    /**
     * 根据订单ID删除项目单明细
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    public int deleteProjectOrderDetailByOrderId(Long orderId);
}
