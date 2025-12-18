package com.yiyang.familyElderly.service;

import java.util.List;
import com.yiyang.familyElderly.domain.FamilyElderly;

/**
 * 家属-老人关系Service接口
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
public interface IFamilyElderlyService 
{
    /**
     * 查询家属-老人关系
     * 
     * @param id 家属-老人关系主键
     * @return 家属-老人关系
     */
    public FamilyElderly selectFamilyElderlyById(Long id);

    /**
     * 查询家属-老人关系列表
     * 
     * @param familyElderly 家属-老人关系
     * @return 家属-老人关系集合
     */
    public List<FamilyElderly> selectFamilyElderlyList(FamilyElderly familyElderly);

    /**
     * 新增家属-老人关系
     * 
     * @param familyElderly 家属-老人关系
     * @return 结果
     */
    public int insertFamilyElderly(FamilyElderly familyElderly);

    /**
     * 修改家属-老人关系
     * 
     * @param familyElderly 家属-老人关系
     * @return 结果
     */
    public int updateFamilyElderly(FamilyElderly familyElderly);

    /**
     * 批量删除家属-老人关系
     * 
     * @param ids 需要删除的家属-老人关系主键集合
     * @return 结果
     */
    public int deleteFamilyElderlyByIds(Long[] ids);

    /**
     * 删除家属-老人关系信息
     * 
     * @param id 家属-老人关系主键
     * @return 结果
     */
    public int deleteFamilyElderlyById(Long id);
}
