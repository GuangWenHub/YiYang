package com.yiyang.record.service.impl;

import com.yiyang.common.utils.DateUtils;
import com.yiyang.record.domain.HealthRecord;
import com.yiyang.record.mapper.HealthRecordMapper;
import com.yiyang.record.service.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 健康档案记录Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class HealthRecordServiceImpl implements IHealthRecordService 
{
    @Autowired
    private HealthRecordMapper healthRecordMapper;

    /**
     * 查询健康档案记录
     * 
     * @param recordId 健康档案记录主键
     * @return 健康档案记录
     */
    @Override
    public HealthRecord selectHealthRecordByRecordId(Long recordId)
    {
        return healthRecordMapper.selectHealthRecordByRecordId(recordId);
    }

    /**
     * 查询健康档案记录列表
     * 
     * @param healthRecord 健康档案记录
     * @return 健康档案记录
     */
    @Override
    public List<HealthRecord> selectHealthRecordList(HealthRecord healthRecord)
    {
        return healthRecordMapper.selectHealthRecordList(healthRecord);
    }

    /**
     * 新增健康档案记录
     * 
     * @param healthRecord 健康档案记录
     * @return 结果
     */
    @Override
    public int insertHealthRecord(HealthRecord healthRecord)
    {
        healthRecord.setCreateTime(DateUtils.getNowDate());
        return healthRecordMapper.insertHealthRecord(healthRecord);
    }

    /**
     * 修改健康档案记录
     * 
     * @param healthRecord 健康档案记录
     * @return 结果
     */
    @Override
    public int updateHealthRecord(HealthRecord healthRecord)
    {
        healthRecord.setUpdateTime(DateUtils.getNowDate());
        return healthRecordMapper.updateHealthRecord(healthRecord);
    }

    /**
     * 批量删除健康档案记录
     * 
     * @param recordIds 需要删除的健康档案记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthRecordByRecordIds(Long[] recordIds)
    {
        return healthRecordMapper.deleteHealthRecordByRecordIds(recordIds);
    }

    /**
     * 删除健康档案记录信息
     * 
     * @param recordId 健康档案记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthRecordByRecordId(Long recordId)
    {
        return healthRecordMapper.deleteHealthRecordByRecordId(recordId);
    }
}
