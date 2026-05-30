package com.yiyang.ai.controller;

import com.yiyang.common.annotation.Anonymous;
import com.yiyang.common.core.domain.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import com.yiyang.elderly.domain.Elderly;
import com.yiyang.elderly.service.IElderlyService;
import com.yiyang.room.domain.Room;
import com.yiyang.room.domain.Bed;
import com.yiyang.room.service.IRoomService;
import com.yiyang.record.domain.HealthRecord;
import com.yiyang.record.service.IHealthRecordService;
import com.yiyang.round.domain.SysWardRound;
import com.yiyang.round.service.ISysWardRoundService;
import com.yiyang.medicine.domain.Medicine;
import com.yiyang.medicine.service.IMedicineService;
import com.yiyang.handover.domain.DoctorHandover;
import com.yiyang.handover.service.IDoctorHandoverService;
import com.yiyang.detail.domain.ProjectOrderDetail;
import com.yiyang.detail.service.IProjectOrderDetailService;
import com.yiyang.medicationRecord.domain.MedicationRecord;
import com.yiyang.medicationRecord.service.IMedicationRecordService;
import com.yiyang.careRecord.domain.CareRecord;
import com.yiyang.careRecord.service.ICareRecordService;
import com.yiyang.medicalOrder.domain.MedicalOrder;
import com.yiyang.medicalOrder.service.IMedicalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dify 服务接口 Controller
 * 为 Dify AI 提供数据查询接口
 * 
 * @author GuangWenLi
 * @date 2026-04-13
 */
@Anonymous
@RestController
@RequestMapping("/dify/serve")
public class DifyServeController {

    @Autowired
    private IElderlyService elderlyService;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private IHealthRecordService healthRecordService;

    @Autowired
    private ISysWardRoundService wardRoundService;

    @Autowired
    private IMedicineService medicineService;

    @Autowired
    private IDoctorHandoverService handoverService;

    @Autowired
    private IProjectOrderDetailService projectOrderDetailService;

    @Autowired
    private IMedicationRecordService medicationRecordService;

    @Autowired
    private IMedicalOrderService medicalOrderService;

    @Autowired
    private ICareRecordService careRecordService;

    /**
     * 查询老人信息
     * 
     * @param elderlyId 老人 ID
     * @return 老人信息
     */
//    @PreAuthorize("@ss.hasPermi('elderly:elderly:list')")
    @GetMapping("/elder")
    public AjaxResult getElder(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        Elderly elder = elderlyService.selectElderlyByElderlyId(elderlyId);
        return AjaxResult.success(elder);
    }

    /**
     * 查询老人的房间和床位信息
     * 
     * @param elderlyId 老人 ID
     * @return 房间和床位信息
     */
//    @PreAuthorize("@ss.hasPermi('room:room:list')")
    @GetMapping("/room")
    public AjaxResult getRoom(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        
        // 通过老人ID查询床位信息
        Bed bed = roomService.selectBedByElderlyId(elderlyId);
        
        if (bed == null) {
            return AjaxResult.warn("该老人未分配床位");
        }
        
        // 通过床位中的房间ID查询房间信息
        Room room = roomService.selectRoomByRoomId(bed.getRoomId());
        
        // 组装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("bed", bed);
        result.put("room", room);
        
        return AjaxResult.success(result);
    }

    /**
     * 查询健康档案记录
     * 
     * @param elderlyId 老人 ID
     * @return 健康档案记录列表（只返回必要字段）
     */
//    @PreAuthorize("@ss.hasPermi('record:record:list')")
    @GetMapping("/record")
    public AjaxResult getHealthRecord(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        HealthRecord record = new HealthRecord();
        record.setElderlyId(elderlyId);
        List<HealthRecord> records = healthRecordService.selectHealthRecordList(record);
        
        // 转换为只包含必要字段的Map列表
        List<Map<String, Object>> result = records.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("recordId", r.getRecordId());
            map.put("elderlyId", r.getElderlyId());
            map.put("elderlyName", r.getElderlyName());
            map.put("recordTime", r.getRecordTime());
            map.put("bloodPressure", r.getBloodPressure());
            map.put("heartRate", r.getHeartRate());
            map.put("temperature", r.getTemperature());
            map.put("bloodSugar", r.getBloodSugar());
            map.put("symptomDesc", r.getSymptomDesc());
            map.put("remark", r.getRemark());
            return map;
        }).collect(java.util.stream.Collectors.toList());
        
        return AjaxResult.success(result);
    }

    /**
     * 查询查房记录
     * 
     * @param elderlyId 老人 ID
     * @return 查房记录列表
     */
//    @PreAuthorize("@ss.hasPermi('round:round:list')")
    @GetMapping("/round")
    public AjaxResult getWardRound(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        SysWardRound wardRound = new SysWardRound();
        wardRound.setElderlyId(elderlyId);
        List<SysWardRound> rounds = wardRoundService.selectSysWardRoundList(wardRound);
        return AjaxResult.success(rounds);
    }

    /**
     * 查询药品信息
     * 
     * @param medicineId 药品 ID
     * @return 药品信息
     */
//    @PreAuthorize("@ss.hasPermi('medicine:medicine:list')")
    @GetMapping("/medicine")
    public AjaxResult getMedicine(Long medicineId) {
        if (medicineId == null) {
            return AjaxResult.error("请提供药品 ID");
        }
        Medicine medicine = medicineService.selectMedicineByMedicineId(medicineId);
        return AjaxResult.success(medicine);
    }

    /**
     * 查询交班记录
     * 
     * @param date 日期（yyyy-MM-dd 格式）
     * @return 交班记录列表
     */
//    @PreAuthorize("@ss.hasPermi('handover:handover:list')")
    @GetMapping("/handover")
    public AjaxResult getHandover(String date) {
        if (date == null || date.isEmpty()) {
            return AjaxResult.error("请提供日期");
        }
        DoctorHandover handover = new DoctorHandover();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        handover.setStartTime(java.sql.Date.valueOf(localDate));
        List<DoctorHandover> handovers = handoverService.selectDoctorHandoverList(handover);
        return AjaxResult.success(handovers);
    }

    /**
     * 查询项目单明细
     * 
     * @param orderId 项目单 ID
     * @return 项目单明细列表
     */
//    @PreAuthorize("@ss.hasPermi('detail:detail:list')")
    @GetMapping("/detail")
    public AjaxResult getDetail(Long orderId) {
        if (orderId == null) {
            return AjaxResult.error("请提供项目单 ID");
        }
        List<ProjectOrderDetail> details = projectOrderDetailService.selectProjectOrderDetailListByOrderId(orderId);
        return AjaxResult.success(details);
    }

    /**
     * 查询用药记录
     * 
     * @param elderlyId 老人 ID
     * @return 用药记录列表
     */
//    @PreAuthorize("@ss.hasPermi('medicationRecord:medicationRecord:list')")
    @GetMapping("/medicationRecord")
    public AjaxResult getMedicationRecord(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        // 先查询该老人的所有医嘱
        MedicalOrder order = new MedicalOrder();
        order.setElderlyId(elderlyId);
        List<MedicalOrder> orders = medicalOrderService.selectMedicalOrderList(order);
        
        // 收集所有医嘱ID
        List<Long> orderIds = orders.stream()
                .map(MedicalOrder::getOrderId)
                .collect(java.util.stream.Collectors.toList());
        
        // 根据医嘱ID查询用药记录
        List<MedicationRecord> records = new ArrayList<>();
        if (!orderIds.isEmpty()) {
            for (Long orderId : orderIds) {
                MedicationRecord record = new MedicationRecord();
                record.setOrderId(orderId);
                records.addAll(medicationRecordService.selectMedicationRecordList(record));
            }
        }
        return AjaxResult.success(records);
    }

    /**
     * 查询生活照料记录
     * 
     * @param elderlyId 老人 ID
     * @return 生活照料记录列表
     */
//    @PreAuthorize("@ss.hasPermi('ai:dify:careRecord')")
    @GetMapping("/careRecord")
    public AjaxResult getCareRecord(Long elderlyId) {
        if (elderlyId == null) {
            return AjaxResult.error("请提供老人 ID");
        }
        CareRecord record = new CareRecord();
        record.setElderlyId(elderlyId);
        List<CareRecord> records = careRecordService.selectCareRecordList(record);
        return AjaxResult.success(records);
    }

    /**
     * 通过模糊查找老人姓名
     * 
     * @param name 老人姓名（支持模糊查询）
     * @return 老人信息列表，包含老人ID及其他数据
     */
//    @PreAuthorize("@ss.hasPermi('elderly:elderly:list')")
    @GetMapping("/elder/search")
    public AjaxResult searchElderByName(String name) {
        if (name == null || name.isEmpty()) {
            return AjaxResult.error("请提供老人姓名");
        }
        
        // 构建查询条件
        Elderly elderly = new Elderly();
        elderly.setName(name);
        
        // 执行模糊查询
        List<Elderly> elders = elderlyService.selectElderlyList(elderly);
        
        return AjaxResult.success(elders);
    }
    // /**
    //  * 综合查询接口（一次性返回所有模块的数据）
    //  * 
    //  * @param elderlyId 老人 ID
    //  * @return 所有模块的数据
    //  */
    // @PreAuthorize("@ss.hasPermi('ai:dify:all')")
    // @GetMapping("/all")
    // public AjaxResult getAllData(Long elderlyId) {
    //     if (elderlyId == null) {
    //         return AjaxResult.error("请提供老人 ID");
    //     }
    //     Map<String, Object> result = new HashMap<>();

    //     // 查询老人信息
    //     Elderly elder = elderlyService.selectElderlyByElderlyId(elderlyId);
    //     result.put("elder", elder);

    //     // 查询床位信息
    //     Bed bed = roomService.selectBedByElderlyId(elderlyId);
    //     result.put("bed", bed);

    //     // 查询房间信息
    //     if (bed != null) {
    //         Room room = roomService.selectRoomByRoomId(bed.getRoomId());
    //         result.put("room", room);
    //     }

    //     // 查询健康档案记录
    //     HealthRecord recordParam = new HealthRecord();
    //     recordParam.setElderlyId(elderlyId);
    //     List<HealthRecord> healthRecords = healthRecordService.selectHealthRecordList(recordParam);
        
    //     // 转换为只包含必要字段的Map列表
    //     List<Map<String, Object>> recordResult = healthRecords.stream().map(r -> {
    //         Map<String, Object> map = new HashMap<>();
    //         map.put("recordId", r.getRecordId());
    //         map.put("elderlyId", r.getElderlyId());
    //         map.put("elderlyName", r.getElderlyName());
    //         map.put("recordTime", r.getRecordTime());
    //         map.put("bloodPressure", r.getBloodPressure());
    //         map.put("heartRate", r.getHeartRate());
    //         map.put("temperature", r.getTemperature());
    //         map.put("bloodSugar", r.getBloodSugar());
    //         map.put("symptomDesc", r.getSymptomDesc());
    //         map.put("remark", r.getRemark());
    //         return map;
    //     }).collect(java.util.stream.Collectors.toList());
        
    //     result.put("record", recordResult);

    //     // 查询查房记录
    //     SysWardRound roundParam = new SysWardRound();
    //     roundParam.setElderlyId(elderlyId);
    //     result.put("round", wardRoundService.selectSysWardRoundList(roundParam));

    //     // 查询用药记录
    //     // 先查询该老人的所有医嘱
    //     MedicalOrder order = new MedicalOrder();
    //     order.setElderlyId(elderlyId);
    //     List<MedicalOrder> orders = medicalOrderService.selectMedicalOrderList(order);
        
    //     // 收集所有医嘱ID
    //     List<Long> orderIds = orders.stream()
    //             .map(MedicalOrder::getOrderId)
    //             .collect(java.util.stream.Collectors.toList());
        
    //     // 根据医嘱ID查询用药记录
    //     List<MedicationRecord> medicationRecords = new ArrayList<>();
    //     if (!orderIds.isEmpty()) {
    //         for (Long orderId : orderIds) {
    //             MedicationRecord record = new MedicationRecord();
    //             record.setOrderId(orderId);
    //             medicationRecords.addAll(medicationRecordService.selectMedicationRecordList(record));
    //         }
    //     }
    //     result.put("medicationRecord", medicationRecords);

    //     // 查询生活照料记录
    //     CareRecord careRecordParam = new CareRecord();
    //     careRecordParam.setElderlyId(elderlyId);
    //     result.put("careRecord", careRecordService.selectCareRecordList(careRecordParam));

    //     return AjaxResult.success(result);
    // }

}
