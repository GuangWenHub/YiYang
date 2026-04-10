package com.yiyang.careRecord.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import com.yiyang.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.careRecord.mapper.CareRecordMapper;
import com.yiyang.careRecord.domain.CareRecord;
import com.yiyang.careRecord.service.ICareRecordService;

/**
 * 生活照料记录Service业务层处理
 * 
 * @author Guangwen
 * @date 2026-03-24
 */
@Service
public class CareRecordServiceImpl implements ICareRecordService 
{
    @Autowired
    private CareRecordMapper careRecordMapper;

    /**
     * 查询生活照料记录
     * 
     * @param recordId 生活照料记录主键
     * @return 生活照料记录
     */
    @Override
    public CareRecord selectCareRecordByRecordId(Long recordId)
    {
        return careRecordMapper.selectCareRecordByRecordId(recordId);
    }

    /**
     * 查询生活照料记录列表
     * 
     * @param careRecord 生活照料记录
     * @return 生活照料记录
     */
    @Override
    public List<CareRecord> selectCareRecordList(CareRecord careRecord)
    {
        // 检查用户是否是护工，如果是，只返回与当前用户id对应的记录
        // 管理员和超级管理员可以看到所有记录
        try {
            if (SecurityUtils.hasRole("caregiver") && 
                !SecurityUtils.hasRole("admin") && 
                !SecurityUtils.hasRole("superadmin")) {
                careRecord.setCreatorId(SecurityUtils.getUserId());
            }
        } catch (Exception e) {
            // 如果权限检查失败，不添加过滤条件，显示所有记录
        }
        return careRecordMapper.selectCareRecordList(careRecord);
    }

    /**
     * 新增生活照料记录
     * 
     * @param careRecord 生活照料记录
     * @return 结果
     */
    @Override
    public int insertCareRecord(CareRecord careRecord)
    {
        careRecord.setCreateTime(DateUtils.getNowDate());
        return careRecordMapper.insertCareRecord(careRecord);
    }

    /**
     * 修改生活照料记录
     * 
     * @param careRecord 生活照料记录
     * @return 结果
     */
    @Override
    public int updateCareRecord(CareRecord careRecord)
    {
        careRecord.setUpdateTime(DateUtils.getNowDate());
        return careRecordMapper.updateCareRecord(careRecord);
    }

    /**
     * 批量删除生活照料记录
     * 
     * @param recordIds 需要删除的生活照料记录主键
     * @return 结果
     */
    @Override
    public int deleteCareRecordByRecordIds(Long[] recordIds)
    {
        return careRecordMapper.deleteCareRecordByRecordIds(recordIds);
    }

    /**
     * 删除生活照料记录信息
     * 
     * @param recordId 生活照料记录主键
     * @return 结果
     */
    @Override
    public int deleteCareRecordByRecordId(Long recordId)
    {
        return careRecordMapper.deleteCareRecordByRecordId(recordId);
    }
}
