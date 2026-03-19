package com.yiyang.medicationRecord.mapper;

import java.util.List;
import com.yiyang.medicationRecord.domain.MedicationRecord;

/**
 * 用药跟踪Mapper接口
 * 
 * @author GuangWen
 * @date 2026-03-19
 */
public interface MedicationRecordMapper 
{
    /**
     * 查询用药跟踪
     * 
     * @param recordId 用药跟踪主键
     * @return 用药跟踪
     */
    public MedicationRecord selectMedicationRecordByRecordId(Long recordId);

    /**
     * 查询用药跟踪列表
     * 
     * @param medicationRecord 用药跟踪
     * @return 用药跟踪集合
     */
    public List<MedicationRecord> selectMedicationRecordList(MedicationRecord medicationRecord);

    /**
     * 新增用药跟踪
     * 
     * @param medicationRecord 用药跟踪
     * @return 结果
     */
    public int insertMedicationRecord(MedicationRecord medicationRecord);

    /**
     * 修改用药跟踪
     * 
     * @param medicationRecord 用药跟踪
     * @return 结果
     */
    public int updateMedicationRecord(MedicationRecord medicationRecord);

    /**
     * 删除用药跟踪
     * 
     * @param recordId 用药跟踪主键
     * @return 结果
     */
    public int deleteMedicationRecordByRecordId(Long recordId);

    /**
     * 批量删除用药跟踪
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMedicationRecordByRecordIds(Long[] recordIds);
    
    /**
     * 根据医嘱ID删除用药跟踪
     * 
     * @param orderId 医嘱ID
     * @return 结果
     */
    public int deleteMedicationRecordByOrderId(Long orderId);
}
