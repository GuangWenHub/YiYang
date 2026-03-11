package com.yiyang.round.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.round.mapper.SysWardRoundMapper;
import com.yiyang.round.domain.SysWardRound;
import com.yiyang.round.service.ISysWardRoundService;

/**
 * 查房记录Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-11
 */
@Service
public class SysWardRoundServiceImpl implements ISysWardRoundService 
{
    @Autowired
    private SysWardRoundMapper sysWardRoundMapper;

    /**
     * 查询查房记录
     * 
     * @param roundId 查房记录主键
     * @return 查房记录
     */
    @Override
    public SysWardRound selectSysWardRoundByRoundId(Long roundId)
    {
        return sysWardRoundMapper.selectSysWardRoundByRoundId(roundId);
    }

    /**
     * 查询查房记录列表
     * 
     * @param sysWardRound 查房记录
     * @return 查房记录
     */
    @Override
    public List<SysWardRound> selectSysWardRoundList(SysWardRound sysWardRound)
    {
        return sysWardRoundMapper.selectSysWardRoundList(sysWardRound);
    }

    /**
     * 新增查房记录
     * 
     * @param sysWardRound 查房记录
     * @return 结果
     */
    @Override
    public int insertSysWardRound(SysWardRound sysWardRound)
    {
        sysWardRound.setCreateTime(DateUtils.getNowDate());
        return sysWardRoundMapper.insertSysWardRound(sysWardRound);
    }

    /**
     * 修改查房记录
     * 
     * @param sysWardRound 查房记录
     * @return 结果
     */
    @Override
    public int updateSysWardRound(SysWardRound sysWardRound)
    {
        sysWardRound.setUpdateTime(DateUtils.getNowDate());
        return sysWardRoundMapper.updateSysWardRound(sysWardRound);
    }

    /**
     * 批量删除查房记录
     * 
     * @param roundIds 需要删除的查房记录主键
     * @return 结果
     */
    @Override
    public int deleteSysWardRoundByRoundIds(Long[] roundIds)
    {
        return sysWardRoundMapper.deleteSysWardRoundByRoundIds(roundIds);
    }

    /**
     * 删除查房记录信息
     * 
     * @param roundId 查房记录主键
     * @return 结果
     */
    @Override
    public int deleteSysWardRoundByRoundId(Long roundId)
    {
        return sysWardRoundMapper.deleteSysWardRoundByRoundId(roundId);
    }
}
