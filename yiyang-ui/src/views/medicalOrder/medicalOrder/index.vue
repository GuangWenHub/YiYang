<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入老人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生ID" prop="doctorId">
        <el-input
          v-model="queryParams.doctorId"
          placeholder="请输入医生ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['medicalOrder:medicalOrder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['medicalOrder:medicalOrder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['medicalOrder:medicalOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['medicalOrder:medicalOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicalOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div v-if="scope.row.medicationRecords && scope.row.medicationRecords.length > 0">
            <el-table :data="getDisplayMedicationRecords(scope.row)" style="width: 100%">
              <el-table-column label="用药记录ID" align="center" prop="recordId" />
              <el-table-column label="药品ID" align="center" prop="medicineId" />
              <el-table-column label="药品名称" align="center" prop="medicineName" />
              <el-table-column label="计划用药时间" align="center" prop="scheduledTime" width="180">
                <template slot-scope="recordScope">
                  <span>{{ parseTime(recordScope.row.scheduledTime, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="实际用药时间" align="center" prop="actualTime" width="180">
                <template slot-scope="recordScope">
                  <span>{{ recordScope.row.actualTime ? parseTime(recordScope.row.actualTime, '{y}-{m}-{d}') : '未执行' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="护工ID" align="center" prop="nurseId" />
              <el-table-column label="任务状态" align="center" prop="status">
                <template slot-scope="recordScope">
                  <el-tag :type="getTaskStatusType(recordScope.row.status)">
                    {{ getTaskStatusLabel(recordScope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="scope.row.medicationRecords.length > 5" style="margin-top: 10px; text-align: right;">
              <el-button type="text" size="small" @click="toggleExpand(scope.row)">
                {{ scope.row.expanded ? '收起' : '查看全部' }}
              </el-button>
            </div>
          </div>
          <div v-else style="padding: 10px; text-align: center; color: #999;">
            暂无用药记录
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="医嘱ID" align="center" prop="orderId" />
      <el-table-column label="老人ID" align="center" prop="elderlyId" />
      <el-table-column label="医嘱内容" align="center" prop="orderContent" />
      <el-table-column label="用药建议" align="center" prop="medicationAdvice" />
      <el-table-column label="医嘱日期" align="center" prop="orderDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['medicalOrder:medicalOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['medicalOrder:medicalOrder:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改医嘱开立对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入老人ID (逻辑关联，无物理外键)" @input="handleElderlyIdInput" />
          <div v-if="elderlyInfo" class="elderly-info">
            <span>老人姓名：{{ elderlyInfo.name }}</span>
            <span>性别：{{ elderlyInfo.sex === '0' ? '男' : elderlyInfo.sex === '1' ? '女' : '未知' }}</span>
          </div>
        </el-form-item>
        <el-form-item label="医嘱内容" prop="orderContent">
          <el-input v-model="form.orderContent" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用药建议" prop="medicationAdvice">
          <el-input v-model="form.medicationAdvice" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- <el-form-item label="医生ID" prop="doctorId">
          <el-input v-model="form.doctorId" placeholder="请输入医生ID" />
        </el-form-item> -->
        <el-form-item label="医嘱日期" prop="orderDate">
          <el-date-picker clearable
            v-model="form.orderDate"
            type="date"
            value-format="yyyy-MM-dd"
            :default-value="new Date()"
            placeholder="请选择医嘱日期">
          </el-date-picker>
        </el-form-item>
        
        <!-- 用药记录 -->
        <el-form-item label="用药记录">
          <div v-if="form.medicationRecords && form.medicationRecords.length > 0">
            <el-table :data="form.medicationRecords" style="width: 100%">
              <el-table-column label="药品名称" prop="medicineName">
                <template slot-scope="scope">
                  <el-autocomplete
                    v-model="scope.row.medicineName"
                    :fetch-suggestions="queryMedicines"
                    placeholder="请输入药品名称"
                    @select="(item) => handleMedicineSelect(item, scope.row)"
                    clearable
                  />
                </template>
              </el-table-column>
              <el-table-column label="药品ID" prop="medicineId" width="100">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.medicineId" placeholder="药品ID" @input="(value) => handleMedicineIdInputForRow(value, scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="计划用药时间" prop="scheduledTime">
                <template slot-scope="scope">
                  <el-date-picker clearable
                    v-model="scope.row.scheduledTime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="请选择计划用药时间">
                  </el-date-picker>
                </template>
              </el-table-column>
              <el-table-column label="分配护工" prop="nurseId">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.nurseId" placeholder="请选择护工" clearable>
                    <el-option
                      v-for="user in caregiverList"
                      :key="user.userId"
                      :label="user.nickName"
                      :value="user.userId"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" @click="removeMedicationRecord(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else style="padding: 10px; text-align: center; color: #999;">
            暂无用药记录
          </div>
          
          <!-- 快速生成用药记录 -->
          <div style="margin-top: 10px;">
            <el-button type="primary" size="small" @click="showQuickGenerateDialog">快速生成用药记录</el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    
    <!-- 快速生成用药计划对话框 -->
    <el-dialog :title="'快速生成用药计划'" :visible.sync="quickGenerateDialogVisible" width="600px" append-to-body>
      <el-form ref="quickGenerateForm" :model="quickGenerateForm" :rules="quickGenerateRules" label-width="120px">
        <el-form-item label="药品名称" prop="medicineName">
          <el-autocomplete
            v-model="quickGenerateForm.medicineName"
            :fetch-suggestions="queryMedicines"
            placeholder="请输入药品名称"
            @select="handleQuickGenerateMedicineSelect"
            clearable
          />
        </el-form-item>
        <el-form-item label="药品ID" prop="medicineId">
          <el-input v-model="quickGenerateForm.medicineId" placeholder="药品ID（输入名称自动填充）"/>
          <div v-if="medicineInfo" class="elderly-info">
            <span>药品价格：{{ medicineInfo.price }}</span>
            <span>药品库存：{{ medicineInfo.stock }}</span>
          </div>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker clearable
            v-model="quickGenerateForm.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            :default-value="new Date()"
            placeholder="请选择开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用药天数" prop="days">
          <el-input v-model="quickGenerateForm.days" type="number" placeholder="请输入用药天数" />
        </el-form-item>
        <el-form-item label="每日用药次数" prop="timesPerDay">
          <el-input v-model="quickGenerateForm.timesPerDay" type="number" placeholder="请输入每日用药次数" @input="handleTimesPerDayChange" />
        </el-form-item>
        <el-form-item label="用药时间点">
          <div v-for="(item, index) in quickGenerateForm.times" :key="index" style="margin-bottom: 10px;">
            <el-time-picker
              v-model="item.value"
              format="HH:mm"
              placeholder="选择时间"
              
              style="width: 100px; margin-right: 10px;"
            />
            <el-button type="danger" size="mini" @click="removeTime(index)" v-if="index > 0">删除</el-button>
          </div>
          <el-button type="primary" size="small" @click="addTime">添加时间点</el-button>
        </el-form-item>
        <el-form-item label="分配护工" prop="nurseId">
          <el-select v-model="quickGenerateForm.nurseId" placeholder="请选择护工" clearable>
            <el-option
              v-for="user in caregiverList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="generateMedicationRecords">生成</el-button>
        <el-button @click="quickGenerateDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMedicalOrder, getMedicalOrder, delMedicalOrder, addMedicalOrder, updateMedicalOrder } from "@/api/medicalOrder/medicalOrder"
import { getElderly } from "@/api/elderly/elderly"
import { getMedicine, listMedicine } from "@/api/medicine/medicine"
import { listUser } from "@/api/system/user"

export default {
  name: "MedicalOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 医嘱开立表格数据
      medicalOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 快速生成用药记录对话框
      quickGenerateDialogVisible: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elderlyId: null,
        doctorId: null,
      },
      // 表单参数
      form: {
        medicationRecords: []
      },
      // 快速生成表单参数
      quickGenerateForm: {
        medicineId: null,
        medicineName: null,
        startDate: null,
        days: "1",
        timesPerDay: "3",
        times: [],
        nurseId: null
      },
      // 表单校验
      rules: {
        elderlyId: [
          { required: true, message: "老人ID不能为空", trigger: "blur" }
        ],
        orderContent: [
          { required: true, message: "医嘱内容不能为空", trigger: "blur" }
        ],
        doctorId: [
          { required: true, message: "医生ID不能为空", trigger: "blur" }
        ],
      },
      // 快速生成表单校验
      quickGenerateRules: {
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "blur" }
        ],
        days: [
          { required: true, message: "用药天数不能为空", trigger: "blur" },
          { type: "string", pattern: /^[1-9]\d*$/, message: "用药天数至少为1", trigger: "blur" }
        ],
        timesPerDay: [
          { required: true, message: "每日用药次数不能为空", trigger: "blur" },
          { type: "string", pattern: /^[1-9]\d*$/, message: "每日用药次数至少为1", trigger: "blur" }
        ],
        nurseId: [
          { required: true, message: "护工ID不能为空", trigger: "blur" }
        ],
      },
      // 老人信息
      elderlyInfo: null,
      // 药品信息
      medicineInfo: null,
      // 护工信息
      nurseInfo: null,
      // 护工列表
      caregiverList: [],
      // 任务状态字典
      taskStatusOptions: []
    }
  },
  created() {
    this.getList()
    this.getCaregiverList()
    this.getDicts('task_status').then(response => {
      this.taskStatusOptions = response.data
    })
  },
  methods: {
    /** 查询医嘱开立列表 */
    getList() {
      this.loading = true
      listMedicalOrder(this.queryParams).then(response => {
        this.medicalOrderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      // 获取当前日期，格式为 yyyy-MM-dd
      const currentDate = new Date().toISOString().slice(0, 10)
      this.form = {
        orderId: null,
        elderlyId: null,
        orderContent: null,
        medicationAdvice: null,
        doctorId: null,
        orderDate: currentDate,
        medicationId: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
        medicationRecords: []
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      let userId = this.$store.getters.userId
        if (!userId && this.$store.state.user) {
          userId = this.$store.state.user.userId || this.$store.state.user.id
        }
      if (userId) 
        this.form.doctorId = userId
      this.open = true
      this.title = "添加医嘱开立"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()

      const orderId = row.orderId || this.ids
      getMedicalOrder(orderId).then(response => {
        this.form = response.data
        // 确保medicationRecords存在
        if (!this.form.medicationRecords) {
          this.form.medicationRecords = []
        } else {
          // 处理用药记录中的药品信息，确保药品名称和药品ID都有值
          this.form.medicationRecords.forEach(record => {
            if (record.medicineId && !record.medicineName) {
              // 如果有药品ID但没有药品名称，查询药品信息
              getMedicine(record.medicineId).then(medResponse => {
                if (medResponse.code === 200) {
                  record.medicineName = medResponse.data.medicineName
                }
              }).catch(() => {
                // 查询失败，不做处理
              })
            } else if (record.medicineName && !record.medicineId) {
              // 如果有药品名称但没有药品ID，查询药品信息
              listMedicine({ medicineName: record.medicineName, pageSize: 1 }).then(medResponse => {
                if (medResponse.code === 200 && medResponse.rows.length > 0) {
                  record.medicineId = medResponse.rows[0].medicineId
                }
              }).catch(() => {
                // 查询失败，不做处理
              })
            }
          })
        }
        // 设置当前用户ID为医生ID
        let userId = this.$store.getters.userId
        if (!userId && this.$store.state.user) {
          userId = this.$store.state.user.userId || this.$store.state.user.id
        }
        if (userId) {
          this.form.doctorId = userId
        }
        this.open = true
        this.title = "修改医嘱开立"
      })
    },
    /** 删除用药记录 */
    removeMedicationRecord(index) {
      this.form.medicationRecords.splice(index, 1)
    },
    /** 显示快速生成用药记录对话框 */
    showQuickGenerateDialog() {
      // 设置默认开始日期为当前日期
      const currentDate = new Date().toISOString().slice(0, 10)
      this.quickGenerateForm.startDate = currentDate
      // 设置默认用药次数为3
      this.quickGenerateForm.timesPerDay = "3"
      // 触发时间点生成
      this.handleTimesPerDayChange()
      this.quickGenerateDialogVisible = true
    },
    /** 添加时间点 */
    addTime() {
      this.quickGenerateForm.times.push({ value: new Date(2000, 0, 1, 0, 0, 0) })
    },
    /** 删除时间点 */
    removeTime(index) {
      this.quickGenerateForm.times.splice(index, 1)
    },
    /** 生成用药记录 */
    generateMedicationRecords() {
      this.$refs["quickGenerateForm"].validate(valid => {
        if (valid) {
          const { medicineId, medicineName, startDate, days, times, nurseId } = this.quickGenerateForm
          const start = new Date(startDate)
          const daysNum = parseInt(days, 10) // 将字符串转换为数字
          
          // 清空现有记录
          // this.form.medicationRecords = []
          
          // 生成用药记录
          for (let i = 0; i < daysNum; i++) {
            const currentDate = new Date(start)
            currentDate.setDate(start.getDate() + i)
            
            times.forEach(item => {
              if (item && item.value) {
                const time = item.value
                const scheduledTime = new Date(currentDate)
                scheduledTime.setHours(time.getHours(), time.getMinutes(), 0, 0)
                
                this.form.medicationRecords.push({
                  medicineId,
                  medicineName,
                  scheduledTime: scheduledTime.toISOString().slice(0, 19).replace('T', ' '),
                  nurseId,
                  status: '0' // 待执行
                })
              }
            })
          }
          
          this.quickGenerateDialogVisible = false
          this.$modal.msgSuccess('用药记录生成成功')
        }
      })
    },
    /** 获取护工列表 */
    getCaregiverList() {
      listUser({ pageSize: 100 }).then(response => {
        // 过滤出角色为护工的用户（角色 key 为 caregiver）
        this.caregiverList = response.rows.filter(user => {
          return user.roles && user.roles.some(role => role.roleKey === 'caregiver')
        })
      }).catch(error => {
        console.log('获取护工列表失败:', error)
      })
    },
    /** 老人ID输入事件处理 */
    handleElderlyIdInput() {
      const elderlyId = this.form.elderlyId
      if (elderlyId) {
        getElderly(elderlyId).then(response => {
          if (response.code === 200) {
            this.elderlyInfo = response.data
          } else {
            this.elderlyInfo = null
          }
        }).catch(() => {
          this.elderlyInfo = null
        })
      } else {
        this.elderlyInfo = null
      }
    },
    /** 药品ID输入事件处理 */
    handleMedicineIdInput() {
      const medicineId = this.quickGenerateForm.medicineId
      if (medicineId) {
        getMedicine(medicineId).then(response => {
          if (response.code === 200) {
            this.medicineInfo = response.data
          } else {
            this.medicineInfo = null
          }
        }).catch(() => {
          this.medicineInfo = null
        })
      } else {
        this.medicineInfo = null
      }
    },
    /** 护工ID输入事件处理 */
    handleNurseIdInput() {
      const nurseId = this.quickGenerateForm.nurseId
      if (nurseId) {
        const caregiver = this.caregiverList.find(user => user.userId === nurseId)
        if (caregiver) {
          this.nurseInfo = caregiver
        } else {
          this.nurseInfo = null
        }
      } else {
        this.nurseInfo = null
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderId != null) {
            updateMedicalOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMedicalOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除医嘱开立编号为"' + orderIds + '"的数据项？').then(function() {
        return delMedicalOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('medicalOrder/medicalOrder/export', {
        ...this.queryParams
      }, `medicalOrder_${new Date().getTime()}.xlsx`)
    },
    /** 获取显示的用药记录 */
    getDisplayMedicationRecords(row) {
      if (row.expanded) {
        return row.medicationRecords
      } else {
        return row.medicationRecords.slice(0, 5)
      }
    },
    /** 切换展开/收起状态 */
    toggleExpand(row) {
      row.expanded = !row.expanded
    },
    /** 查询药品 */
    queryMedicines(queryString, callback) {
      listMedicine({ pageSize: 100, medicineName: queryString }).then(response => {
        const medicines = response.rows.map(item => {
          return {
            value: item.medicineName,
            label: item.medicineName,
            medicineId: item.medicineId
          }
        })
        callback(medicines)
      }).catch(() => {
        callback([])
      })
    },
    /** 处理药品选择 */
    handleMedicineSelect(item, row) {
      row.medicineId = item.medicineId
      row.medicineName = item.value
    },
    /** 处理快速生成用药记录的药品选择 */
    handleQuickGenerateMedicineSelect(item) {
      this.quickGenerateForm.medicineId = item.medicineId
      this.quickGenerateForm.medicineName = item.value
      // 获取药品信息
      getMedicine(item.medicineId).then(response => {
        if (response.code === 200) {
          this.medicineInfo = response.data
        } else {
          this.medicineInfo = null
        }
      }).catch(() => {
        this.medicineInfo = null
      })
    },
    /** 处理用药记录表格中药品ID的输入 */
    handleMedicineIdInputForRow(medicineId, row) {
      if (medicineId) {
        getMedicine(medicineId).then(response => {
          if (response.code === 200) {
            row.medicineName = response.data.medicineName
          } else {
            row.medicineName = ''
          }
        }).catch(() => {
          row.medicineName = ''
        })
      } else {
        row.medicineName = ''
      }
    },
    /** 处理每日用药次数变化 */
    handleTimesPerDayChange() {
      const timesPerDay = parseInt(this.quickGenerateForm.timesPerDay, 10)
      if (isNaN(timesPerDay) || timesPerDay < 1) {
        return
      }
      
      // 清空现有时间点
      this.quickGenerateForm.times = []
      
      // 根据用药次数生成时间点
      const timePoints = [
        { hour: 8, minute: 0 },   // 8:00
        { hour: 12, minute: 0 },  // 12:00
        { hour: 18, minute: 0 },  // 18:00
        { hour: 22, minute: 0 },  // 22:00
        { hour: 4, minute: 0 }    // 4:00
      ]
      
      for (let i = 0; i < timesPerDay; i++) {
        const timePoint = timePoints[i % timePoints.length]
        this.quickGenerateForm.times.push({ 
          value: new Date(2000, 0, 1, timePoint.hour, timePoint.minute, 0) 
        })
      }
    },
    /** 获取任务状态标签 */
    getTaskStatusLabel(status) {
      const option = this.taskStatusOptions.find(item => item.dictValue === status)
      return option ? option.dictLabel : status
    },
    /** 获取任务状态类型 */
    getTaskStatusType(status) {
      switch (status) {
        case '0':
          return 'info'
        case '1':
          return 'success'
        case '2':
          return 'danger'
        default:
          return ''
      }
    }
  }
}
</script>

<style scoped>
.elderly-info {
  margin-top: 8px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
}

.elderly-info span {
  display: block;
  margin-bottom: 4px;
  color: #606266;
}

.elderly-info span:last-child {
  margin-bottom: 0;
}
</style>
