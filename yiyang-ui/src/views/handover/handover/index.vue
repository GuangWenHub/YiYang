<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="当前医生ID" prop="currentDoctorId">
        <el-input
          v-model="queryParams.currentDoctorId"
          placeholder="请输入当前医生ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="待交接医生ID" prop="nextDoctorId">
        <el-input
          v-model="queryParams.nextDoctorId"
          placeholder="请输入交接医生ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上班时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择上班时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="下班时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择下班时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.handover_status"
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
          v-hasPermi="['handover:handover:add']"
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
          v-hasPermi="['handover:handover:edit']"
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
          v-hasPermi="['handover:handover:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['handover:handover:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="handoverList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="交接记录ID" align="center" prop="handoverId" />
      <el-table-column label="当前医生ID" align="center" prop="currentDoctorId" />
      <el-table-column label="待交接医生ID" align="center" prop="nextDoctorId" />
      <el-table-column label="上班时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下班时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.handover_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="当前情况概述" align="center" prop="overview" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleHandover(scope.row)"
            v-hasPermi="['handover:handover:handover']"
          >去交接</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['handover:handover:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['handover:handover:remove']"
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

    <!-- 添加或修改医生交班记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="当前医生 ID" prop="currentDoctorId">
          <el-input v-model="form.currentDoctorId" placeholder="请输入当前医生 ID" />
        </el-form-item>
        <el-form-item label="待交接医生 ID" prop="nextDoctorId">
          <el-input v-model="form.nextDoctorId" placeholder="请输入待交接医生 ID" />
        </el-form-item>
        <el-form-item label="上班时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择上班时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="下班时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择下班时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.handover_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="当前情况概述" prop="overview">
          <el-input v-model="form.overview" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 去交接对话框 -->
    <el-dialog :title="handoverTitle" :visible.sync="handoverOpen" width="500px" append-to-body>
      <el-form ref="handoverForm" :model="handoverForm" label-width="80px">
        <el-form-item label="交接记录 ID" prop="handoverId">
          <el-input v-model="handoverForm.handoverId" disabled />
        </el-form-item>
        <el-form-item label="当前医生 ID" prop="currentDoctorId">
          <el-input v-model="handoverForm.currentDoctorId" disabled />
        </el-form-item>
        <el-form-item label="上班时间" prop="startTime">
          <el-date-picker
            v-model="handoverForm.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择上班时间"
            disabled>
          </el-date-picker>
        </el-form-item>
        <el-form-item label="当前情况概述" prop="overview">
          <el-input v-model="handoverForm.overview" type="textarea" disabled />
        </el-form-item>
        <el-form-item label="交接医生 ID" prop="nextDoctorId">
          <el-input v-model="handoverForm.nextDoctorId" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitHandover">确认交接</el-button>
        <el-button @click="cancelHandover">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listHandover, getHandover, delHandover, addHandover, updateHandover, handover } from "@/api/handover/handover"

export default {
  name: "Handover",
  dicts: ['handover_status'],
  data() {
    return {
      // 当前登录用户 ID
      currentUserId: null,
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
      // 医生交班记录表格数据
      handoverList: [],
      // 弹出层标题
      title: "",
      // 去交接弹出层标题
      handoverTitle: "去交接",
      // 是否显示弹出层
      open: false,
      // 是否显示交接弹出层
      handoverOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        currentDoctorId: null,
        nextDoctorId: null,
        startTime: null,
        endTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 交接表单参数
      handoverForm: {},
      // 表单校验
      rules: {
        currentDoctorId: [
          { required: true, message: "当前医生 ID 不能为空", trigger: "blur" }
        ],
        nextDoctorId: [
          { required: true, message: "待交接医生 ID 不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "上班时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "下班时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    // 获取当前登录用户 ID (参考 projectOrder 的方法)
    this.currentUserId = this.$store.getters.userId
    if (!this.currentUserId && this.$store.state.user) {
      this.currentUserId = this.$store.state.user.userId || this.$store.state.user.id
    }
    console.log('初始化 - 当前登录用户 ID:', this.currentUserId)
    this.getList()
  },
  methods: {
    /** 查询医生交班记录列表 */
    getList() {
      this.loading = true
      listHandover(this.queryParams).then(response => {
        this.handoverList = response.rows
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
        handoverId: null,
        currentDoctorId: null,
        nextDoctorId: null,
        startTime: null,
        endTime: null,
        status: null,
        overview: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      this.resetForm("form")
    },
    // 交接表单重置
    resetHandover() {
      this.handoverForm = {
        handoverId: null,
        currentDoctorId: null,
        nextDoctorId: null,
        startTime: null,
        endTime: null,
        status: null,
        overview: null
      }
      this.resetForm("handoverForm")
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
      this.ids = selection.map(item => item.handoverId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加医生交班记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const handoverId = row.handoverId || this.ids
      getHandover(handoverId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改医生交班记录"
      })
    },
    /** 去交接按钮操作 */
    handleHandover(row) {
      this.resetHandover()
      const handoverId = row.handoverId
      
      // --- 获取当前登录用户 ID (参考 projectOrder 的方法) ---
      let userId = this.$store.getters.userId
      if (!userId && this.$store.state.user) {
        userId = this.$store.state.user.userId || this.$store.state.user.id
      }
      
      console.log('当前登录用户 ID:', userId)
      console.log('交接记录 ID:', handoverId)
      
      getHandover(handoverId).then(response => {
        console.log('获取到的交接记录:', response.data)
        this.handoverForm = response.data
        
        // 强制设置当前登录用户 ID 到 nextDoctorId
        if (userId) {
          this.$set(this.handoverForm, 'nextDoctorId', userId)
          console.log('设置的 nextDoctorId:', this.handoverForm.nextDoctorId)
        } else {
          this.$modal.msgWarning('未获取到当前登录用户 ID，请重新登录')
        }
        
        // 设置为已交接状态
        this.handoverForm.status = '1'
        this.handoverOpen = true
        this.handoverTitle = "去交接"
        
        // 确保输入框显示当前用户 ID（需要等待 DOM 更新）
        this.$nextTick(() => {
          if (userId) {
            this.handoverForm.nextDoctorId = userId
            console.log('最终设置的 nextDoctorId:', this.handoverForm.nextDoctorId)
          }
        })
      })
    },
    /** 取消交接 */
    cancelHandover() {
      this.handoverOpen = false
      this.resetHandover()
    },
    /** 提交交接 */
    submitHandover() {
      // 使用专门的 handover API，需要 handover:handover:handover 权限
      handover(this.handoverForm).then(response => {
        this.$modal.msgSuccess("交接成功")
        this.handoverOpen = false
        this.resetHandover()
        this.getList()
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.handoverId != null) {
            updateHandover(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addHandover(this.form).then(response => {
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
      const handoverIds = row.handoverId || this.ids
      this.$modal.confirm('是否确认删除医生交班记录编号为"' + handoverIds + '"的数据项？').then(function() {
        return delHandover(handoverIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('handover/handover/export', {
        ...this.queryParams
      }, `handover_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
