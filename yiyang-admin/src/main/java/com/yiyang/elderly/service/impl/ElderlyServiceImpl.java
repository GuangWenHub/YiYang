package com.yiyang.elderly.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.elderly.mapper.ElderlyMapper;
import com.yiyang.elderly.domain.Elderly;
import com.yiyang.elderly.service.IElderlyService;

/**
 * 老人档案Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class ElderlyServiceImpl implements IElderlyService 
{
    @Autowired
    private ElderlyMapper elderlyMapper;

    /**
     * 查询老人档案
     * 
     * @param elderlyId 老人档案主键
     * @return 老人档案
     */
    @Override
    public Elderly selectElderlyByElderlyId(Long elderlyId)
    {
        return elderlyMapper.selectElderlyByElderlyId(elderlyId);
    }

    /**
     * 查询老人档案列表
     * 
     * @param elderly 老人档案
     * @return 老人档案
     */
    @Override
    public List<Elderly> selectElderlyList(Elderly elderly)
    {
        return elderlyMapper.selectElderlyList(elderly);
    }

    /**
     * 新增老人档案
     * 
     * @param elderly 老人档案
     * @return 结果
     */
    @Override
    public int insertElderly(Elderly elderly)
    {
        elderly.setCreateTime(DateUtils.getNowDate());
        return elderlyMapper.insertElderly(elderly);
    }

    /**
     * 修改老人档案
     * 
     * @param elderly 老人档案
     * @return 结果
     */
    @Override
    public int updateElderly(Elderly elderly)
    {
        elderly.setUpdateTime(DateUtils.getNowDate());
        return elderlyMapper.updateElderly(elderly);
    }

    /**
     * 批量删除老人档案
     * 
     * @param elderlyIds 需要删除的老人档案主键
     * @return 结果
     */
    @Override
    public int deleteElderlyByElderlyIds(Long[] elderlyIds)
    {
        return elderlyMapper.deleteElderlyByElderlyIds(elderlyIds);
    }

    /**
     * 删除老人档案信息
     * 
     * @param elderlyId 老人档案主键
     * @return 结果
     */
    @Override
    public int deleteElderlyByElderlyId(Long elderlyId)
    {
        return elderlyMapper.deleteElderlyByElderlyId(elderlyId);
    }
}
