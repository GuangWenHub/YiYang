package com.yiyang.information.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.information.mapper.SysNursingHomeMapper;
import com.yiyang.information.domain.SysNursingHome;
import com.yiyang.information.service.ISysNursingHomeService;

/**
 * 养老院基本信息Service业务层处理
 * 
 * @author Guangwen
 * @date 2026-03-11
 */
@Service
public class SysNursingHomeServiceImpl implements ISysNursingHomeService 
{
    @Autowired
    private SysNursingHomeMapper sysNursingHomeMapper;

    /**
     * 查询养老院基本信息
     * 
     * @param id 养老院基本信息主键
     * @return 养老院基本信息
     */
    @Override
    public SysNursingHome selectSysNursingHomeById(Long id)
    {
        return sysNursingHomeMapper.selectSysNursingHomeById(id);
    }

    /**
     * 查询养老院基本信息列表
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 养老院基本信息
     */
    @Override
    public List<SysNursingHome> selectSysNursingHomeList(SysNursingHome sysNursingHome)
    {
        return sysNursingHomeMapper.selectSysNursingHomeList(sysNursingHome);
    }

    /**
     * 新增养老院基本信息
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 结果
     */
    @Override
    public int insertSysNursingHome(SysNursingHome sysNursingHome)
    {
        sysNursingHome.setCreateTime(DateUtils.getNowDate());
        return sysNursingHomeMapper.insertSysNursingHome(sysNursingHome);
    }

    /**
     * 修改养老院基本信息
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 结果
     */
    @Override
    public int updateSysNursingHome(SysNursingHome sysNursingHome)
    {
        sysNursingHome.setUpdateTime(DateUtils.getNowDate());
        return sysNursingHomeMapper.updateSysNursingHome(sysNursingHome);
    }

    /**
     * 批量删除养老院基本信息
     * 
     * @param ids 需要删除的养老院基本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysNursingHomeByIds(Long[] ids)
    {
        return sysNursingHomeMapper.deleteSysNursingHomeByIds(ids);
    }

    /**
     * 删除养老院基本信息信息
     * 
     * @param id 养老院基本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysNursingHomeById(Long id)
    {
        return sysNursingHomeMapper.deleteSysNursingHomeById(id);
    }
}
