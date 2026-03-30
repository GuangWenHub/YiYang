package com.yiyang.floorDept.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.floorDept.mapper.SysFloorDeptMapper;
import com.yiyang.floorDept.domain.SysFloorDept;
import com.yiyang.floorDept.service.ISysFloorDeptService;

/**
 * 楼层科室管理Service实现
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
@Service
public class SysFloorDeptServiceImpl implements ISysFloorDeptService
{
    @Autowired
    private SysFloorDeptMapper sysFloorDeptMapper;

    /**
     * 查询楼层科室管理
     * 
     * @param floorDeptId 楼层科室管理主键
     * @return 楼层科室管理
     */
    @Override
    public SysFloorDept selectSysFloorDeptById(Long floorDeptId)
    {
        return sysFloorDeptMapper.selectSysFloorDeptById(floorDeptId);
    }

    /**
     * 查询楼层科室管理列表
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 楼层科室管理集合
     */
    @Override
    public List<SysFloorDept> selectSysFloorDeptList(SysFloorDept sysFloorDept)
    {
        return sysFloorDeptMapper.selectSysFloorDeptList(sysFloorDept);
    }

    /**
     * 新增楼层科室管理
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 结果
     */
    @Override
    public int insertSysFloorDept(SysFloorDept sysFloorDept)
    {
        return sysFloorDeptMapper.insertSysFloorDept(sysFloorDept);
    }

    /**
     * 修改楼层科室管理
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 结果
     */
    @Override
    public int updateSysFloorDept(SysFloorDept sysFloorDept)
    {
        return sysFloorDeptMapper.updateSysFloorDept(sysFloorDept);
    }

    /**
     * 批量删除楼层科室管理
     * 
     * @param floorDeptIds 需要删除的楼层科室管理主键集合
     * @return 结果
     */
    @Override
    public int deleteSysFloorDeptByIds(Long[] floorDeptIds)
    {
        return sysFloorDeptMapper.deleteSysFloorDeptByIds(floorDeptIds);
    }

    /**
     * 删除楼层科室管理信息
     * 
     * @param floorDeptId 楼层科室管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFloorDeptById(Long floorDeptId)
    {
        return sysFloorDeptMapper.deleteSysFloorDeptById(floorDeptId);
    }
}
