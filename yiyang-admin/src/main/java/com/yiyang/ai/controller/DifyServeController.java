package com.yiyang.ai.controller;

import com.yiyang.common.core.domain.AjaxResult;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private ICareRecordService careRecordService;

    /**
     * 查询老人信息
     * 
     * @param elderlyId 老人 ID
     * @return 老人信息
     */
    @GetMapping("/elder")
    public AjaxResult getElder(Long elderlyId) {
        if (elderlyId == null) {
            List<Elderly> elders = elderlyService.selectElderlyList(new Elderly());
            return AjaxResult.success(elders);
        }
        Elderly elder = elderlyService.selectElderlyByElderlyId(elderlyId);
        return AjaxResult.success(elder);
    }

    /**
     * 查询房间信息
     * 
     * @param roomId 房间 ID
     * @return 房间信息
     */
    @GetMapping("/room")
    public AjaxResult getRoom(Long roomId) {
        if (roomId == null) {
            List<Room> rooms = roomService.selectRoomList(new Room());
            return AjaxResult.success(rooms);
        }
        Room room = roomService.selectRoomByRoomId(roomId);
        return AjaxResult.success(room);
    }

    /**
     * 查询健康档案记录
     * 
     * @param elderlyId 老人 ID
     * @return 健康档案记录列表
     */
    @GetMapping("/record")
    public AjaxResult getHealthRecord(Long elderlyId) {
        HealthRecord record = new HealthRecord();
        if (elderlyId != null) {
            record.setElderlyId(elderlyId);
        }
        List<HealthRecord> records = healthRecordService.selectHealthRecordList(record);
        return AjaxResult.success(records);
    }

    /**
     * 查询查房记录
     * 
     * @param elderlyId 老人 ID
     * @return 查房记录列表
     */
    @GetMapping("/round")
    public AjaxResult getWardRound(Long elderlyId) {
        SysWardRound wardRound = new SysWardRound();
        if (elderlyId != null) {
            wardRound.setElderlyId(elderlyId);
        }
        List<SysWardRound> rounds = wardRoundService.selectSysWardRoundList(wardRound);
        return AjaxResult.success(rounds);
    }

    /**
     * 查询药品信息
     * 
     * @param medicineId 药品 ID
     * @return 药品信息
     */
    @GetMapping("/medicine")
    public AjaxResult getMedicine(Long medicineId) {
        if (medicineId == null) {
            Medicine medicine = new Medicine();
            medicine.setStatus("0");
            List<Medicine> medicines = medicineService.selectMedicineList(medicine);
            return AjaxResult.success(medicines);
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
    @GetMapping("/handover")
    public AjaxResult getHandover(String date) {
        DoctorHandover handover = new DoctorHandover();
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            handover.setStartTime(java.sql.Date.valueOf(localDate));
        }
        List<DoctorHandover> handovers = handoverService.selectDoctorHandoverList(handover);
        return AjaxResult.success(handovers);
    }

    /**
     * 查询项目单明细
     * 
     * @param orderId 项目单 ID
     * @return 项目单明细列表
     */
    @GetMapping("/detail")
    public AjaxResult getDetail(Long orderId) {
        if (orderId == null) {
            List<ProjectOrderDetail> details = projectOrderDetailService.selectProjectOrderDetailList(new ProjectOrderDetail());
            return AjaxResult.success(details);
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
    @GetMapping("/medicationRecord")
    public AjaxResult getMedicationRecord(Long elderlyId) {
        MedicationRecord record = new MedicationRecord();
        if (elderlyId != null) {
            // 需要通过 orderId 关联查询，这里先返回所有记录
            // 实际使用时可以根据业务需求优化查询逻辑
        }
        List<MedicationRecord> records = medicationRecordService.selectMedicationRecordList(record);
        return AjaxResult.success(records);
    }

    /**
     * 查询生活照料记录
     * 
     * @param elderlyId 老人 ID
     * @return 生活照料记录列表
     */
    @GetMapping("/careRecord")
    public AjaxResult getCareRecord(Long elderlyId) {
        CareRecord record = new CareRecord();
        if (elderlyId != null) {
            record.setElderlyId(elderlyId);
        }
        List<CareRecord> records = careRecordService.selectCareRecordList(record);
        return AjaxResult.success(records);
    }

    /**
     * 综合查询接口（一次性返回所有模块的数据）
     * 
     * @param elderlyId 老人 ID
     * @return 所有模块的数据
     */
    @GetMapping("/all")
    public AjaxResult getAllData(Long elderlyId) {
        Map<String, Object> result = new HashMap<>();

        // 查询老人信息
        if (elderlyId != null) {
            Elderly elder = elderlyService.selectElderlyByElderlyId(elderlyId);
            result.put("elder", elder);

            // 查询床位信息
            Bed bed = roomService.selectBedByElderlyId(elderlyId);
            result.put("bed", bed);
        } else {
            result.put("elder", elderlyService.selectElderlyList(new Elderly()));
        }

        // 查询房间信息
        result.put("room", roomService.selectRoomList(new Room()));

        // 查询健康档案记录
        HealthRecord recordParam = new HealthRecord();
        if (elderlyId != null) {
            recordParam.setElderlyId(elderlyId);
        }
        result.put("record", healthRecordService.selectHealthRecordList(recordParam));

        // 查询查房记录
        SysWardRound roundParam = new SysWardRound();
        if (elderlyId != null) {
            roundParam.setElderlyId(elderlyId);
        }
        result.put("round", wardRoundService.selectSysWardRoundList(roundParam));

        // 查询药品信息
        Medicine medicineParam = new Medicine();
        medicineParam.setStatus("0");
        result.put("medicine", medicineService.selectMedicineList(medicineParam));

        // 查询交班记录
        result.put("handover", handoverService.selectDoctorHandoverList(new DoctorHandover()));

        // 查询项目单明细
        result.put("detail", projectOrderDetailService.selectProjectOrderDetailList(new ProjectOrderDetail()));

        // 查询用药记录
        MedicationRecord medicationRecordParam = new MedicationRecord();
        result.put("medicationRecord", medicationRecordService.selectMedicationRecordList(medicationRecordParam));

        // 查询生活照料记录
        CareRecord careRecordParam = new CareRecord();
        if (elderlyId != null) {
            careRecordParam.setElderlyId(elderlyId);
        }
        result.put("careRecord", careRecordService.selectCareRecordList(careRecordParam));

        return AjaxResult.success(result);
    }
}
