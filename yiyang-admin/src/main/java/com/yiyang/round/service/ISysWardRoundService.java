package com.yiyang.round.service;

import java.util.List;
import com.yiyang.round.domain.SysWardRound;

/**
 * 查房记录Service接口
 * 
 * @author GuangWen
 * @date 2026-03-11
 */
public interface ISysWardRoundService 
{
    /**
     * 查询查房记录
     * 
     * @param roundId 查房记录主键
     * @return 查房记录
     */
    public SysWardRound selectSysWardRoundByRoundId(Long roundId);

    /**
     * 查询查房记录列表
     * 
     * @param sysWardRound 查房记录
     * @return 查房记录集合
     */
    public List<SysWardRound> selectSysWardRoundList(SysWardRound sysWardRound);

    /**
     * 新增查房记录
     * 
     * @param sysWardRound 查房记录
     * @return 结果
     */
    public int insertSysWardRound(SysWardRound sysWardRound);

    /**
     * 修改查房记录
     * 
     * @param sysWardRound 查房记录
     * @return 结果
     */
    public int updateSysWardRound(SysWardRound sysWardRound);

    /**
     * 批量删除查房记录
     * 
     * @param roundIds 需要删除的查房记录主键集合
     * @return 结果
     */
    public int deleteSysWardRoundByRoundIds(Long[] roundIds);

    /**
     * 删除查房记录信息
     * 
     * @param roundId 查房记录主键
     * @return 结果
     */
    public int deleteSysWardRoundByRoundId(Long roundId);
}
