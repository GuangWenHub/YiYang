package com.yiyang.familyElderly.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.familyElderly.mapper.FamilyElderlyMapper;
import com.yiyang.familyElderly.domain.FamilyElderly;
import com.yiyang.familyElderly.service.IFamilyElderlyService;

/**
 * 家属-老人关系Service业务层处理
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@Service
public class FamilyElderlyServiceImpl implements IFamilyElderlyService 
{
    @Autowired
    private FamilyElderlyMapper familyElderlyMapper;

    /**
     * 查询家属-老人关系
     * 
     * @param id 家属-老人关系主键
     * @return 家属-老人关系
     */
    @Override
    public FamilyElderly selectFamilyElderlyById(Long id)
    {
        return familyElderlyMapper.selectFamilyElderlyById(id);
    }

    /**
     * 查询家属-老人关系列表
     * 
     * @param familyElderly 家属-老人关系
     * @return 家属-老人关系
     */
    @Override
    public List<FamilyElderly> selectFamilyElderlyList(FamilyElderly familyElderly)
    {
        return familyElderlyMapper.selectFamilyElderlyList(familyElderly);
    }

    /**
     * 新增家属-老人关系
     * 
     * @param familyElderly 家属-老人关系
     * @return 结果
     */
    @Override
    public int insertFamilyElderly(FamilyElderly familyElderly)
    {
        return familyElderlyMapper.insertFamilyElderly(familyElderly);
    }

    /**
     * 修改家属-老人关系
     * 
     * @param familyElderly 家属-老人关系
     * @return 结果
     */
    @Override
    public int updateFamilyElderly(FamilyElderly familyElderly)
    {
        return familyElderlyMapper.updateFamilyElderly(familyElderly);
    }

    /**
     * 批量删除家属-老人关系
     * 
     * @param ids 需要删除的家属-老人关系主键
     * @return 结果
     */
    @Override
    public int deleteFamilyElderlyByIds(Long[] ids)
    {
        return familyElderlyMapper.deleteFamilyElderlyByIds(ids);
    }

    /**
     * 删除家属-老人关系信息
     * 
     * @param id 家属-老人关系主键
     * @return 结果
     */
    @Override
    public int deleteFamilyElderlyById(Long id)
    {
        return familyElderlyMapper.deleteFamilyElderlyById(id);
    }
}
