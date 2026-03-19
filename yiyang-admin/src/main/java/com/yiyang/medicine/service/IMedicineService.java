package com.yiyang.medicine.service;

import java.util.List;
import com.yiyang.medicine.domain.Medicine;

/**
 * 药品管理Service接口
 * 
 * @author GuangWen
 * @date 2026-03-18
 */
public interface IMedicineService 
{
    /**
     * 查询药品管理
     * 
     * @param medicineId 药品管理主键
     * @return 药品管理
     */
    public Medicine selectMedicineByMedicineId(Long medicineId);

    /**
     * 查询药品管理列表
     * 
     * @param medicine 药品管理
     * @return 药品管理集合
     */
    public List<Medicine> selectMedicineList(Medicine medicine);

    /**
     * 新增药品管理
     * 
     * @param medicine 药品管理
     * @return 结果
     */
    public int insertMedicine(Medicine medicine);

    /**
     * 修改药品管理
     * 
     * @param medicine 药品管理
     * @return 结果
     */
    public int updateMedicine(Medicine medicine);

    /**
     * 批量删除药品管理
     * 
     * @param medicineIds 需要删除的药品管理主键集合
     * @return 结果
     */
    public int deleteMedicineByMedicineIds(Long[] medicineIds);

    /**
     * 删除药品管理信息
     * 
     * @param medicineId 药品管理主键
     * @return 结果
     */
    public int deleteMedicineByMedicineId(Long medicineId);
}
