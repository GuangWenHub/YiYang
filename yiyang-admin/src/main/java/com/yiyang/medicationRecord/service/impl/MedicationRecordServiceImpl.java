package com.yiyang.medicationRecord.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.medicationRecord.mapper.MedicationRecordMapper;
import com.yiyang.medicationRecord.domain.MedicationRecord;
import com.yiyang.medicationRecord.service.IMedicationRecordService;

/**
 * 用药跟踪Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-19
 */
@Service
public class MedicationRecordServiceImpl implements IMedicationRecordService 
{
    @Autowired
    private MedicationRecordMapper medicationRecordMapper;

    /**
     * 查询用药跟踪
     * 
     * @param recordId 用药跟踪主键
     * @return 用药跟踪
     */
    @Override
    public MedicationRecord selectMedicationRecordByRecordId(Long recordId)
    {
        return medicationRecordMapper.selectMedicationRecordByRecordId(recordId);
    }

    /**
     * 查询用药跟踪列表
     * 
     * @param medicationRecord 用药跟踪
     * @return 用药跟踪
     */
    @Override
    public List<MedicationRecord> selectMedicationRecordList(MedicationRecord medicationRecord)
    {
        return medicationRecordMapper.selectMedicationRecordList(medicationRecord);
    }

    /**
     * 新增用药跟踪
     * 
     * @param medicationRecord 用药跟踪
     * @return 结果
     */
    @Override
    public int insertMedicationRecord(MedicationRecord medicationRecord)
    {
        medicationRecord.setCreateTime(DateUtils.getNowDate());
        return medicationRecordMapper.insertMedicationRecord(medicationRecord);
    }

    /**
     * 修改用药跟踪
     * 
     * @param medicationRecord 用药跟踪
     * @return 结果
     */
    @Override
    public int updateMedicationRecord(MedicationRecord medicationRecord)
    {
        medicationRecord.setUpdateTime(DateUtils.getNowDate());
        return medicationRecordMapper.updateMedicationRecord(medicationRecord);
    }

    /**
     * 批量删除用药跟踪
     * 
     * @param recordIds 需要删除的用药跟踪主键
     * @return 结果
     */
    @Override
    public int deleteMedicationRecordByRecordIds(Long[] recordIds)
    {
        return medicationRecordMapper.deleteMedicationRecordByRecordIds(recordIds);
    }

    /**
     * 删除用药跟踪信息
     * 
     * @param recordId 用药跟踪主键
     * @return 结果
     */
    @Override
    public int deleteMedicationRecordByRecordId(Long recordId)
    {
        return medicationRecordMapper.deleteMedicationRecordByRecordId(recordId);
    }
}
