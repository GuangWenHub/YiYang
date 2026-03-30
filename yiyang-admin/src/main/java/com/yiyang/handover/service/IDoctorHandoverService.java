package com.yiyang.handover.service;

import java.util.List;
import com.yiyang.handover.domain.DoctorHandover;

/**
 * 医生交班记录Service接口
 * 
 * @author GuangWen
 * @date 2026-03-28
 */
public interface IDoctorHandoverService 
{
    /**
     * 查询医生交班记录
     * 
     * @param handoverId 医生交班记录主键
     * @return 医生交班记录
     */
    public DoctorHandover selectDoctorHandoverByHandoverId(Long handoverId);

    /**
     * 查询医生交班记录列表
     * 
     * @param doctorHandover 医生交班记录
     * @return 医生交班记录集合
     */
    public List<DoctorHandover> selectDoctorHandoverList(DoctorHandover doctorHandover);

    /**
     * 新增医生交班记录
     * 
     * @param doctorHandover 医生交班记录
     * @return 结果
     */
    public int insertDoctorHandover(DoctorHandover doctorHandover);

    /**
     * 修改医生交班记录
     * 
     * @param doctorHandover 医生交班记录
     * @return 结果
     */
    public int updateDoctorHandover(DoctorHandover doctorHandover);

    /**
     * 批量删除医生交班记录
     * 
     * @param handoverIds 需要删除的医生交班记录主键集合
     * @return 结果
     */
    public int deleteDoctorHandoverByHandoverIds(Long[] handoverIds);

    /**
     * 删除医生交班记录信息
     * 
     * @param handoverId 医生交班记录主键
     * @return 结果
     */
    public int deleteDoctorHandoverByHandoverId(Long handoverId);
}
