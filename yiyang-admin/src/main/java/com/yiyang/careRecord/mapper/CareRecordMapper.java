package com.yiyang.careRecord.mapper;

import java.util.List;
import com.yiyang.careRecord.domain.CareRecord;

/**
 * 生活照料记录Mapper接口
 * 
 * @author Guangwen
 * @date 2026-03-24
 */
public interface CareRecordMapper 
{
    /**
     * 查询生活照料记录
     * 
     * @param recordId 生活照料记录主键
     * @return 生活照料记录
     */
    public CareRecord selectCareRecordByRecordId(Long recordId);

    /**
     * 查询生活照料记录列表
     * 
     * @param careRecord 生活照料记录
     * @return 生活照料记录集合
     */
    public List<CareRecord> selectCareRecordList(CareRecord careRecord);

    /**
     * 新增生活照料记录
     * 
     * @param careRecord 生活照料记录
     * @return 结果
     */
    public int insertCareRecord(CareRecord careRecord);

    /**
     * 修改生活照料记录
     * 
     * @param careRecord 生活照料记录
     * @return 结果
     */
    public int updateCareRecord(CareRecord careRecord);

    /**
     * 删除生活照料记录
     * 
     * @param recordId 生活照料记录主键
     * @return 结果
     */
    public int deleteCareRecordByRecordId(Long recordId);

    /**
     * 批量删除生活照料记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCareRecordByRecordIds(Long[] recordIds);
}
