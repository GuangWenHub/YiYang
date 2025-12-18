package com.yiyang.elderly.mapper;

import java.util.List;
import com.yiyang.elderly.domain.Elderly;

/**
 * 老人档案Mapper接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface ElderlyMapper 
{
    /**
     * 查询老人档案
     * 
     * @param elderlyId 老人档案主键
     * @return 老人档案
     */
    public Elderly selectElderlyByElderlyId(Long elderlyId);

    /**
     * 查询老人档案列表
     * 
     * @param elderly 老人档案
     * @return 老人档案集合
     */
    public List<Elderly> selectElderlyList(Elderly elderly);

    /**
     * 新增老人档案
     * 
     * @param elderly 老人档案
     * @return 结果
     */
    public int insertElderly(Elderly elderly);

    /**
     * 修改老人档案
     * 
     * @param elderly 老人档案
     * @return 结果
     */
    public int updateElderly(Elderly elderly);

    /**
     * 删除老人档案
     * 
     * @param elderlyId 老人档案主键
     * @return 结果
     */
    public int deleteElderlyByElderlyId(Long elderlyId);

    /**
     * 批量删除老人档案
     * 
     * @param elderlyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElderlyByElderlyIds(Long[] elderlyIds);
}
