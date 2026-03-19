package com.yiyang.medicine.service.impl;

import java.util.List;
import com.yiyang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyang.medicine.mapper.MedicineMapper;
import com.yiyang.medicine.domain.Medicine;
import com.yiyang.medicine.service.IMedicineService;

/**
 * 药品管理Service业务层处理
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
@Service
public class MedicineServiceImpl implements IMedicineService 
{
    @Autowired
    private MedicineMapper medicineMapper;

    /**
     * 查询药品管理
     * 
     * @param medicineId 药品管理主键
     * @return 药品管理
     */
    @Override
    public Medicine selectMedicineByMedicineId(Long medicineId)
    {
        return medicineMapper.selectMedicineByMedicineId(medicineId);
    }

    /**
     * 查询药品管理列表
     * 
     * @param medicine 药品管理
     * @return 药品管理
     */
    @Override
    public List<Medicine> selectMedicineList(Medicine medicine)
    {
        return medicineMapper.selectMedicineList(medicine);
    }

    /**
     * 新增药品管理
     * 
     * @param medicine 药品管理
     * @return 结果
     */
    @Override
    public int insertMedicine(Medicine medicine)
    {
        medicine.setCreateTime(DateUtils.getNowDate());
        return medicineMapper.insertMedicine(medicine);
    }

    /**
     * 修改药品管理
     * 
     * @param medicine 药品管理
     * @return 结果
     */
    @Override
    public int updateMedicine(Medicine medicine)
    {
        medicine.setUpdateTime(DateUtils.getNowDate());
        return medicineMapper.updateMedicine(medicine);
    }

    /**
     * 批量删除药品管理
     * 
     * @param medicineIds 需要删除的药品管理主键
     * @return 结果
     */
    @Override
    public int deleteMedicineByMedicineIds(Long[] medicineIds)
    {
        return medicineMapper.deleteMedicineByMedicineIds(medicineIds);
    }

    /**
     * 删除药品管理信息
     * 
     * @param medicineId 药品管理主键
     * @return 结果
     */
    @Override
    public int deleteMedicineByMedicineId(Long medicineId)
    {
        return medicineMapper.deleteMedicineByMedicineId(medicineId);
    }
}
