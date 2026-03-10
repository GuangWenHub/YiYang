package com.yiyang.record.mapper;

import com.yiyang.record.domain.HealthRecord;

import java.util.List;

/**
 * 健康档案记录Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface HealthRecordMapper 
{
    /**
     * 查询健康档案记录
     * 
     * @param recordId 健康档案记录主键
     * @return 健康档案记录
     */
    public HealthRecord selectHealthRecordByRecordId(Long recordId);

    /**
     * 查询健康档案记录列表
     * 
     * @param healthRecord 健康档案记录
     * @return 健康档案记录集合
     */
    public List<HealthRecord> selectHealthRecordList(HealthRecord healthRecord);

    /**
     * 新增健康档案记录
     * 
     * @param healthRecord 健康档案记录
     * @return 结果
     */
    public int insertHealthRecord(HealthRecord healthRecord);

    /**
     * 修改健康档案记录
     * 
     * @param healthRecord 健康档案记录
     * @return 结果
     */
    public int updateHealthRecord(HealthRecord healthRecord);

    /**
     * 删除健康档案记录
     * 
     * @param recordId 健康档案记录主键
     * @return 结果
     */
    public int deleteHealthRecordByRecordId(Long recordId);

    /**
     * 批量删除健康档案记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHealthRecordByRecordIds(Long[] recordIds);
}
