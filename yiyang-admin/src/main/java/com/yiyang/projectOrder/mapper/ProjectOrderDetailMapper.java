package com.yiyang.projectOrder.mapper;

import java.util.List;
import com.yiyang.projectOrder.domain.ProjectOrderDetail;

/**
 * 项目单明细Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface ProjectOrderDetailMapper 
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
     * 删除项目单明细
     * 
     * @param detailId 项目单明细主键
     * @return 结果
     */
    public int deleteProjectOrderDetailByDetailId(Long detailId);

    /**
     * 批量删除项目单明细
     * 
     * @param detailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectOrderDetailByDetailIds(Long[] detailIds);

    /**
     * 根据订单ID删除项目单明细
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    public int deleteProjectOrderDetailByOrderId(Long orderId);
}
