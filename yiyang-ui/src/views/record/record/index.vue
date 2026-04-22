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
      <el-form-item label="记录时间" prop="recordTime">
        <el-date-picker clearable
          v-model="queryParams.recordTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择记录时间">
        </el-date-picker>
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
          v-hasPermi="['record:record:add']"
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
          v-hasPermi="['record:record:edit']"
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
          v-hasPermi="['record:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['record:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="健康记录ID" align="center" prop="recordId" />
      <el-table-column label="老人ID" align="center" prop="elderlyId" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="记录时间" align="center" prop="recordTime" width="220">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="血压" align="center" prop="bloodPressure" />
      <el-table-column label="心率" align="center" prop="heartRate" />
      <el-table-column label="体温" align="center" prop="temperature" />
      <el-table-column label="体重" align="center" prop="weight" />
      <el-table-column label="血糖" align="center" prop="bloodSugar" />
      <el-table-column label="症状描述" align="center" prop="symptomDesc" />
      <el-table-column label="其他备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['record:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['record:record:remove']"
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

    <!-- 添加或修改健康档案记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联的老人ID" @input="handleElderlyIdInput" />
          <div v-if="elderlyInfo" class="elderly-info">
            <span>老人姓名：{{ elderlyInfo.name }}</span>
            <span>性别：{{ elderlyInfo.sex === '0' ? '男' : elderlyInfo.sex === '1' ? '女' : '未知' }}</span>
          </div>
        </el-form-item>
        <el-form-item label="记录人ID" prop="recorderId">
          <el-input v-model="form.recorderId" placeholder="系统将自动填充" disabled />
        </el-form-item>

        <el-form-item label="血压" prop="bloodPressure">
          <el-input v-model="form.bloodPressure" placeholder="请输入血压" />
        </el-form-item>
        <el-form-item label="心率" prop="heartRate">
          <el-input v-model="form.heartRate" placeholder="请输入心率" />
        </el-form-item>
        <el-form-item label="体温" prop="temperature">
          <el-input v-model="form.temperature" placeholder="请输入体温" />
        </el-form-item>
        <el-form-item label="体重" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入体重" />
        </el-form-item>
        <el-form-item label="血糖" prop="bloodSugar">
          <el-input v-model="form.bloodSugar" placeholder="请输入血糖" />
        </el-form-item>
        <el-form-item label="症状描述" prop="symptomDesc">
          <el-input v-model="form.symptomDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="其他备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
         <el-form-item label="记录时间" prop="recordTime">
          <el-date-picker clearable
            v-model="form.recordTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择记录时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/record/record"
import { getElderly } from "@/api/elderly/elderly"

export default {
  name: "Record",
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
      // 健康档案记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elderlyId: null,
        recordTime: null,
        symptomDesc: null,
      },
      // 表单参数
      form: {},
      // 老人信息
      elderlyInfo: null,
      // 表单校验
      rules: {
        elderlyId: [
          { required: true, message: "老人ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询健康档案记录列表 */
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows.reverse()
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
        elderlyId: null,
        recorderId: null,
        recordTime: null,
        bloodPressure: null,
        heartRate: null,
        temperature: null,
        weight: null,
        bloodSugar: null,
        symptomDesc: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      // 重置老人信息
      this.elderlyInfo = null
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      // 自动填充当前用户ID
      let userId = this.$store.getters.userId
      if (!userId && this.$store.state.user) {
        userId = this.$store.state.user.userId || this.$store.state.user.id
      }
      if (userId) {
        this.form.recorderId = userId
      }
      // 自动填充当前时间
      const currentDate = new Date()
      const year = currentDate.getFullYear()
      const month = String(currentDate.getMonth() + 1).padStart(2, '0')
      const day = String(currentDate.getDate()).padStart(2, '0')
      const hours = String(currentDate.getHours()).padStart(2, '0')
      const minutes = String(currentDate.getMinutes()).padStart(2, '0')
      const seconds = String(currentDate.getSeconds()).padStart(2, '0')
      const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      this.form.recordTime = formattedDate
      this.open = true
      this.title = "添加健康档案记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data
        // 确保记录人ID存在
        if (!this.form.recorderId) {
          let userId = this.$store.getters.userId
          if (!userId && this.$store.state.user) {
            userId = this.$store.state.user.userId || this.$store.state.user.id
          }
          if (userId) {
            this.form.recorderId = userId
          }
        }
        // 确保记录时间存在
        if (!this.form.recordTime) {
          const currentDate = new Date()
          const year = currentDate.getFullYear()
          const month = String(currentDate.getMonth() + 1).padStart(2, '0')
          const day = String(currentDate.getDate()).padStart(2, '0')
          const hours = String(currentDate.getHours()).padStart(2, '0')
          const minutes = String(currentDate.getMinutes()).padStart(2, '0')
          const seconds = String(currentDate.getSeconds()).padStart(2, '0')
          const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          this.form.recordTime = formattedDate
        }
        // 查询老人信息
        if (this.form.elderlyId) {
          getElderly(this.form.elderlyId).then(elderlyResponse => {
            if (elderlyResponse.code === 200) {
              this.elderlyInfo = elderlyResponse.data
            }
          })
        }
        this.open = true
        this.title = "修改健康档案记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除健康档案记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('record/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
