<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="计划用药时间" prop="scheduledTime">
        <el-date-picker clearable
          v-model="queryParams.scheduledTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择计划用药时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际用药时间" prop="actualTime">
        <el-date-picker clearable
          v-model="queryParams.actualTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择实际用药时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="分配护工ID" prop="nurseId">
        <el-input
          v-model="queryParams.nurseId"
          placeholder="请输入分配护工ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
            <el-option
              v-for="dict in dict.type.task_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
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
          v-hasPermi="['medicationRecord:medicationRecord:add']"
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
          v-hasPermi="['medicationRecord:medicationRecord:edit']"
        >去打卡</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['medicationRecord:medicationRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['medicationRecord:medicationRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicationRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用药记录ID" align="center" prop="recordId" />
      <el-table-column label="药品ID " align="center" prop="medicineId" />
      <el-table-column label="药品名称" align="center" prop="medicineName" />
      <el-table-column label="计划用药时间" align="center" prop="scheduledTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.scheduledTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际用药时间" align="center" prop="actualTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分配护工ID" align="center" prop="nurseId" />
      <el-table-column label="打卡图片URL" align="center" prop="imageUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.task_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['medicationRecord:medicationRecord:edit']"
          >去打卡</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['medicationRecord:medicationRecord:remove']"
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

    <!-- 添加或去打卡用药跟踪对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="药品名称" prop="medicineName">
          <el-input v-model="form.medicineName" placeholder="药品名称" disabled />
        </el-form-item>
        <el-form-item label="医嘱内容" prop="orderContent">
          <el-input v-model="form.orderContent" type="textarea" placeholder="医嘱内容" disabled />
        </el-form-item>
        <el-form-item label="用药建议" prop="medicationAdvice">
          <el-input v-model="form.medicationAdvice" type="textarea" placeholder="用药建议" disabled />
        </el-form-item>
        <el-form-item label="实际用药时间" prop="actualTime">
          <el-date-picker clearable
            v-model="form.actualTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择实际用药时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="打卡图片URL" prop="imageUrl">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleCompleteCheckIn">完成打卡</el-button>
        <el-button type="danger" @click="handleExceptionReport">异常上报</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMedicationRecord, getMedicationRecord, delMedicationRecord, addMedicationRecord, updateMedicationRecord } from "@/api/medicationRecord/medicationRecord"

export default {
  name: "MedicationRecord",
  dicts: ['task_status'],
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
      // 用药跟踪表格数据
      medicationRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        scheduledTime: null,
        actualTime: null,
        nurseId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        nurseId: [
          { required: true, message: "分配护工ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    // 检查用户角色，如果是护工，只显示与当前登录id对应的记录
    const roles = this.$store.getters.roles || []
    if (roles.includes('caregiver')) {
      this.queryParams.nurseId = this.$store.getters.userId
    }
    this.getList()
  },
  methods: {
    /** 查询用药跟踪列表 */
    getList() {
      this.loading = true
      listMedicationRecord(this.queryParams).then(response => {
        this.medicationRecordList = response.rows
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
      this.form = {
        recordId: null,
        orderId: null,
        medicineId: null,
        medicineName: null,
        orderContent: null,
        medicationAdvice: null,
        scheduledTime: null,
        actualTime: null,
        nurseId: null,
        imageUrl: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加用药跟踪"
    },
    /** 去打卡按钮操作 */
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getMedicationRecord(recordId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "去打卡"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateMedicationRecord(this.form).then(response => {
              this.$modal.msgSuccess("去打卡成功")
              this.open = false
              this.getList()
            })
          } else {
            addMedicationRecord(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids
      this.$modal.confirm('是否确认删除用药跟踪编号为"' + recordIds + '"的数据项？').then(function() {
        return delMedicationRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('medicationRecord/medicationRecord/export', {
        ...this.queryParams
      }, `medicationRecord_${new Date().getTime()}.xlsx`)
    },
    /** 完成打卡 */
    handleCompleteCheckIn() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.status = '1' // 完成状态
          this.form.actualTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
          updateMedicationRecord(this.form).then(response => {
            this.$modal.msgSuccess("打卡成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    /** 异常上报 */
    handleExceptionReport() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.status = '2' // 异常状态
          this.form.actualTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
          updateMedicationRecord(this.form).then(response => {
            this.$modal.msgSuccess("异常上报成功")
            this.open = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>
