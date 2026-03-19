package com.yiyang.medicalOrder.mapper;

import java.util.List;
import com.yiyang.medicalOrder.domain.MedicalOrder;

/**
 * 医嘱开立Mapper接口
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
public interface MedicalOrderMapper 
{
    /**
     * 查询医嘱开立
     * 
     * @param orderId 医嘱开立主键
     * @return 医嘱开立
     */
    public MedicalOrder selectMedicalOrderByOrderId(Long orderId);

    /**
     * 查询医嘱开立列表
     * 
     * @param medicalOrder 医嘱开立
     * @return 医嘱开立集合
     */
    public List<MedicalOrder> selectMedicalOrderList(MedicalOrder medicalOrder);

    /**
     * 新增医嘱开立
     * 
     * @param medicalOrder 医嘱开立
     * @return 结果
     */
    public int insertMedicalOrder(MedicalOrder medicalOrder);

    /**
     * 修改医嘱开立
     * 
     * @param medicalOrder 医嘱开立
     * @return 结果
     */
    public int updateMedicalOrder(MedicalOrder medicalOrder);

    /**
     * 删除医嘱开立
     * 
     * @param orderId 医嘱开立主键
     * @return 结果
     */
    public int deleteMedicalOrderByOrderId(Long orderId);

    /**
     * 批量删除医嘱开立
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMedicalOrderByOrderIds(Long[] orderIds);
}
