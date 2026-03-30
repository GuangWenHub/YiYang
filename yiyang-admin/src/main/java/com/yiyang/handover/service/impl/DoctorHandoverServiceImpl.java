package com.yiyang.handover.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.handover.mapper.DoctorHandoverMapper;
import com.yiyang.handover.domain.DoctorHandover;
import com.yiyang.handover.service.IDoctorHandoverService;

/**
 * 医生交班记录Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-28
 */
@Service
public class DoctorHandoverServiceImpl implements IDoctorHandoverService 
{
    @Autowired
    private DoctorHandoverMapper doctorHandoverMapper;

    /**
     * 查询医生交班记录
     * 
     * @param handoverId 医生交班记录主键
     * @return 医生交班记录
     */
    @Override
    public DoctorHandover selectDoctorHandoverByHandoverId(Long handoverId)
    {
        return doctorHandoverMapper.selectDoctorHandoverByHandoverId(handoverId);
    }

    /**
     * 查询医生交班记录列表
     * 
     * @param doctorHandover 医生交班记录
     * @return 医生交班记录
     */
    @Override
    public List<DoctorHandover> selectDoctorHandoverList(DoctorHandover doctorHandover)
    {
        return doctorHandoverMapper.selectDoctorHandoverList(doctorHandover);
    }

    /**
     * 新增医生交班记录
     * 
     * @param doctorHandover 医生交班记录
     * @return 结果
     */
    @Override
    public int insertDoctorHandover(DoctorHandover doctorHandover)
    {
        doctorHandover.setCreateTime(DateUtils.getNowDate());
        return doctorHandoverMapper.insertDoctorHandover(doctorHandover);
    }

    /**
     * 修改医生交班记录
     * 
     * @param doctorHandover 医生交班记录
     * @return 结果
     */
    @Override
    public int updateDoctorHandover(DoctorHandover doctorHandover)
    {
        doctorHandover.setUpdateTime(DateUtils.getNowDate());
        return doctorHandoverMapper.updateDoctorHandover(doctorHandover);
    }

    /**
     * 批量删除医生交班记录
     * 
     * @param handoverIds 需要删除的医生交班记录主键
     * @return 结果
     */
    @Override
    public int deleteDoctorHandoverByHandoverIds(Long[] handoverIds)
    {
        return doctorHandoverMapper.deleteDoctorHandoverByHandoverIds(handoverIds);
    }

    /**
     * 删除医生交班记录信息
     * 
     * @param handoverId 医生交班记录主键
     * @return 结果
     */
    @Override
    public int deleteDoctorHandoverByHandoverId(Long handoverId)
    {
        return doctorHandoverMapper.deleteDoctorHandoverByHandoverId(handoverId);
    }
}
