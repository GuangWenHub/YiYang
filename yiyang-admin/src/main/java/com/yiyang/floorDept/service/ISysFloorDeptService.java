package com.yiyang.floorDept.service;

import java.util.List;
import com.yiyang.floorDept.domain.SysFloorDept;

/**
 * 楼层科室管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-30
 */
public interface ISysFloorDeptService
{
    /**
     * 查询楼层科室管理
     * 
     * @param floorDeptId 楼层科室管理主键
     * @return 楼层科室管理
     */
    public SysFloorDept selectSysFloorDeptById(Long floorDeptId);

    /**
     * 查询楼层科室管理列表
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 楼层科室管理集合
     */
    public List<SysFloorDept> selectSysFloorDeptList(SysFloorDept sysFloorDept);

    /**
     * 新增楼层科室管理
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 结果
     */
    public int insertSysFloorDept(SysFloorDept sysFloorDept);

    /**
     * 修改楼层科室管理
     * 
     * @param sysFloorDept 楼层科室管理
     * @return 结果
     */
    public int updateSysFloorDept(SysFloorDept sysFloorDept);

    /**
     * 批量删除楼层科室管理
     * 
     * @param floorDeptIds 需要删除的楼层科室管理主键集合
     * @return 结果
     */
    public int deleteSysFloorDeptByIds(Long[] floorDeptIds);

    /**
     * 删除楼层科室管理信息
     * 
     * @param floorDeptId 楼层科室管理主键
     * @return 结果
     */
    public int deleteSysFloorDeptById(Long floorDeptId);
}
