package com.yiyang.medicationRecord.service;

import java.util.List;
import com.yiyang.medicationRecord.domain.MedicationRecord;

/**
 * 用药跟踪Service接口
 * 
 * @author GuangWen
 * @date 2026-03-19
 */
public interface IMedicationRecordService 
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
     * 批量删除用药跟踪
     * 
     * @param recordIds 需要删除的用药跟踪主键集合
     * @return 结果
     */
    public int deleteMedicationRecordByRecordIds(Long[] recordIds);

    /**
     * 删除用药跟踪信息
     * 
     * @param recordId 用药跟踪主键
     * @return 结果
     */
    public int deleteMedicationRecordByRecordId(Long recordId);
}
