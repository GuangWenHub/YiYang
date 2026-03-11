package com.yiyang.information.service;

import java.util.List;
import com.yiyang.information.domain.SysNursingHome;

/**
 * 养老院基本信息Service接口
 * 
 * @author Guangwen
 * @date 2026-03-11
 */
public interface ISysNursingHomeService 
{
    /**
     * 查询养老院基本信息
     * 
     * @param id 养老院基本信息主键
     * @return 养老院基本信息
     */
    public SysNursingHome selectSysNursingHomeById(Long id);

    /**
     * 查询养老院基本信息列表
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 养老院基本信息集合
     */
    public List<SysNursingHome> selectSysNursingHomeList(SysNursingHome sysNursingHome);

    /**
     * 新增养老院基本信息
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 结果
     */
    public int insertSysNursingHome(SysNursingHome sysNursingHome);

    /**
     * 修改养老院基本信息
     * 
     * @param sysNursingHome 养老院基本信息
     * @return 结果
     */
    public int updateSysNursingHome(SysNursingHome sysNursingHome);

    /**
     * 批量删除养老院基本信息
     * 
     * @param ids 需要删除的养老院基本信息主键集合
     * @return 结果
     */
    public int deleteSysNursingHomeByIds(Long[] ids);

    /**
     * 删除养老院基本信息信息
     * 
     * @param id 养老院基本信息主键
     * @return 结果
     */
    public int deleteSysNursingHomeById(Long id);
}
