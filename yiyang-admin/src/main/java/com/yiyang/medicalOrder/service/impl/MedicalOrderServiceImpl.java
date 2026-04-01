package com.yiyang.medicalOrder.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yiyang.medicalOrder.mapper.MedicalOrderMapper;
import com.yiyang.medicalOrder.domain.MedicalOrder;
import com.yiyang.medicalOrder.service.IMedicalOrderService;
import com.yiyang.medicationRecord.mapper.MedicationRecordMapper;
import com.yiyang.medicationRecord.domain.MedicationRecord;
import com.yiyang.medicine.service.IMedicineService;
import com.yiyang.medicine.domain.Medicine;

/**
 * 医嘱开立Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
@Service
public class MedicalOrderServiceImpl implements IMedicalOrderService 
{
    @Autowired
    private MedicalOrderMapper medicalOrderMapper;
    
    @Autowired
    private MedicationRecordMapper medicationRecordMapper;

    @Autowired
    private IMedicineService medicineService;

    /**
     * 查询医嘱开立
     * 
     * @param orderId 医嘱开立主键
     * @return 医嘱开立
     */
    @Override
    public MedicalOrder selectMedicalOrderByOrderId(Long orderId)
    {
        return medicalOrderMapper.selectMedicalOrderByOrderId(orderId);
    }

    /**
     * 查询医嘱开立列表
     * 
     * @param medicalOrder 医嘱开立
     * @return 医嘱开立
     */
    @Override
    public List<MedicalOrder> selectMedicalOrderList(MedicalOrder medicalOrder)
    {
        return medicalOrderMapper.selectMedicalOrderList(medicalOrder);
    }

    /**
     * 新增医嘱开立
     * 
     * @param medicalOrder 医嘱开立
     * @return 结果
     */
    @Override
    @Transactional
    public int insertMedicalOrder(MedicalOrder medicalOrder)
    {
        medicalOrder.setCreateTime(DateUtils.getNowDate());
        int result = medicalOrderMapper.insertMedicalOrder(medicalOrder);
        
        // 保存用药记录并减少药品库存
        if (medicalOrder.getMedicationRecords() != null && !medicalOrder.getMedicationRecords().isEmpty()) {
            for (MedicationRecord record : medicalOrder.getMedicationRecords()) {
                record.setOrderId(medicalOrder.getOrderId());
                record.setCreateTime(DateUtils.getNowDate());
                medicationRecordMapper.insertMedicationRecord(record);
                
                // 减少药品库存
                if (record.getMedicineId() != null) {
                    Medicine medicine = medicineService.selectMedicineByMedicineId(record.getMedicineId());
                    if (medicine != null && medicine.getStock() > 0) {
                        medicine.setStock(medicine.getStock() - 1);
                        medicineService.updateMedicine(medicine);
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 修改医嘱开立
     * 
     * @param medicalOrder 医嘱开立
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMedicalOrder(MedicalOrder medicalOrder)
    {
        medicalOrder.setUpdateTime(DateUtils.getNowDate());
        int result = medicalOrderMapper.updateMedicalOrder(medicalOrder);
        
        // 删除旧的用药记录
        medicationRecordMapper.deleteMedicationRecordByOrderId(medicalOrder.getOrderId());
        
        // 保存新的用药记录并减少药品库存
        if (medicalOrder.getMedicationRecords() != null && !medicalOrder.getMedicationRecords().isEmpty()) {
            for (MedicationRecord record : medicalOrder.getMedicationRecords()) {
                record.setOrderId(medicalOrder.getOrderId());
                record.setCreateTime(DateUtils.getNowDate());
                medicationRecordMapper.insertMedicationRecord(record);
                
                // 减少药品库存
                if (record.getMedicineId() != null) {
                    Medicine medicine = medicineService.selectMedicineByMedicineId(record.getMedicineId());
                    if (medicine != null && medicine.getStock() > 0) {
                        medicine.setStock(medicine.getStock() - 1);
                        medicineService.updateMedicine(medicine);
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 批量删除医嘱开立
     * 
     * @param orderIds 需要删除的医嘱开立主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMedicalOrderByOrderIds(Long[] orderIds)
    {
        int result = 0;
        for (Long orderId : orderIds) {
            result += deleteMedicalOrderByOrderId(orderId);
        }
        return result;
    }

    /**
     * 删除医嘱开立信息
     * 
     * @param orderId 医嘱开立主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMedicalOrderByOrderId(Long orderId)
    {
        // 先查询该医嘱下的用药记录
        List<MedicationRecord> records = medicationRecordMapper.selectMedicationRecordByOrderId(orderId);
        
        // 恢复药品库存
        for (MedicationRecord record : records) {
            if (record.getMedicineId() != null) {
                Medicine medicine = medicineService.selectMedicineByMedicineId(record.getMedicineId());
                if (medicine != null) {
                    medicine.setStock(medicine.getStock() + 1);
                    medicineService.updateMedicine(medicine);
                }
            }
        }
        
        // 删除用药记录
        medicationRecordMapper.deleteMedicationRecordByOrderId(orderId);
        
        // 删除医嘱
        return medicalOrderMapper.deleteMedicalOrderByOrderId(orderId);
    }
}
