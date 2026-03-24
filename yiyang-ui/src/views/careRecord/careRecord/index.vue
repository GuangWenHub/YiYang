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
      <el-form-item label="照料类型" prop="careType">
        <el-select v-model="queryParams.careType" placeholder="请选择照料类型" clearable>
          <el-option
            v-for="dict in dict.type.activity_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预计照料时间" prop="careTime">
        <el-date-picker clearable
          v-model="queryParams.careTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预计照料时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
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
          v-hasPermi="['careRecord:careRecord:add']"
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
          v-hasPermi="['careRecord:careRecord:edit']"
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
          v-hasPermi="['careRecord:careRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['careRecord:careRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="careRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="recordId" />
      <el-table-column label="老人ID" align="center" prop="elderlyId" />
      <el-table-column label="护工ID" align="center" prop="creatorId" />
      <el-table-column label="照料类型" align="center" prop="careType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.activity_type" :value="scope.row.careType"/>
        </template>
      </el-table-column>
      <el-table-column label="照料内容" align="center" prop="careContent" />
      <el-table-column label="计划照料时间" align="center" prop="careTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.careTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="照料开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="照料结束时间" align="center" prop="approvedTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.approvedTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.task_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="打卡图片" align="center" prop="imageUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
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
            v-hasPermi="['careRecord:careRecord:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['careRecord:careRecord:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleCheckIn(scope.row)"
            v-hasPermi="['careRecord:careRecord:checkIn']"
          >去打卡</el-button>
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

    <!-- 添加或修改生活照料记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入老人ID" />
        </el-form-item>
        <el-form-item label="照料类型" prop="careType">
          <el-select v-model="form.careType" placeholder="请选择照料类型">
            <el-option
              v-for="dict in dict.type.activity_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="照料内容">
          <el-input  v-model="form.careContent" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="计划照料时间" prop="careTime">
          <el-date-picker clearable
            v-model="form.careTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择计划照料时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="照料开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择照料开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="照料结束时间" prop="approvedTime">
          <el-date-picker clearable
            v-model="form.approvedTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择照料结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.task_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="打卡图片URL" prop="imageUrl">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 打卡对话框 -->
    <el-dialog :title="'生活照料打卡'" :visible.sync="checkInOpen" width="500px" append-to-body>
      <el-form ref="checkInForm" :model="form" label-width="80px">
        <el-form-item label="老人ID">
          <el-input v-model="form.elderlyId" disabled />
        </el-form-item>
        <el-form-item label="照料类型">
          <dict-tag :options="dict.type.activity_type" :value="form.careType"/>
        </el-form-item>
        <el-form-item label="照料内容">
          <el-input v-model="form.careContent" type="textarea" disabled />
        </el-form-item>
        <el-form-item label="计划照料时间">
          <span>{{ parseTime(form.careTime, '{y}-{m}-{d}') }}</span>
        </el-form-item>
        <el-form-item label="实际开始时间">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择实际开始时间"
          />
        </el-form-item>
        <el-form-item label="实际结束时间">
          <el-date-picker
            v-model="form.approvedTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择实际结束时间"
          />
        </el-form-item>
        <el-form-item label="打卡图片">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleCompleteCheckIn">完成打卡</el-button>
        <el-button type="danger" @click="handleExceptionReport">异常上报</el-button>
        <el-button @click="checkInCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCareRecord, getCareRecord, delCareRecord, addCareRecord, updateCareRecord } from "@/api/careRecord/careRecord"

export default {
  name: "CareRecord",
  dicts: ['task_status', 'activity_type'],
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
      // 生活照料记录表格数据
      careRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示打卡弹出层
      checkInOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elderlyId: null,
        creatorId: null,
        careType: null,
        careTime: null,
        startTime: null,
        approvedTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        elderlyId: [
          { required: true, message: "老人ID不能为空", trigger: "blur" }
        ],
        creatorId: [
          { required: true, message: "护工ID不能为空", trigger: "change" }
        ],
        careType: [
          { required: true, message: "照料类型不能为空", trigger: "change" }
        ],
        careContent: [
          { required: true, message: "照料内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询生活照料记录列表 */
    getList() {
      this.loading = true
      listCareRecord(this.queryParams).then(response => {
        this.careRecordList = response.rows
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
        creatorId: null,
        careType: null,
        careContent: null,
        careTime: null,
        startTime: null,
        approvedTime: null,
        status: null,
        imageUrl: null,
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
      this.title = "添加生活照料记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getCareRecord(recordId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改生活照料记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateCareRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCareRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除生活照料记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delCareRecord(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('careRecord/careRecord/export', {
        ...this.queryParams
      }, `careRecord_${new Date().getTime()}.xlsx`)
    },
    /** 去打卡按钮操作 */
    handleCheckIn(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getCareRecord(recordId).then(response => {
        this.form = response.data
        // 设置默认时间为当前时间
        const now = new Date()
        this.form.startTime = now.toISOString().slice(0, 19).replace('T', ' ')
        this.form.approvedTime = now.toISOString().slice(0, 19).replace('T', ' ')
        this.checkInOpen = true
      })
    },
    /** 打卡取消按钮操作 */
    checkInCancel() {
      this.checkInOpen = false
      this.reset()
    },
    /** 完成打卡按钮操作 */
    handleCompleteCheckIn() {
      this.form.status = '1' // 已完成
      this.submitForm()
      this.checkInOpen = false
    },
    /** 异常上报按钮操作 */
    handleExceptionReport() {
      this.form.status = '2' // 异常
      this.submitForm()
      this.checkInOpen = false
    }
  }
}
</script>
