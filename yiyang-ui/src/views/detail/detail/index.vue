<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属项目单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入所属项目单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分配护工ID" prop="creatorId">
        <el-input
          v-model="queryParams.creatorId"
          placeholder="请输入分配护工ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务开始日期" prop="startDate">
        <el-date-picker clearable
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择服务开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="服务结束日期" prop="endDate">
        <el-date-picker clearable
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择服务结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="服务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择服务状态" clearable>
          <el-option
            v-for="dict in dict.type.order_status"
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
          v-hasPermi="['detail:detail:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single || (ids.length > 0 && getSelectedItemStatus() === 4)"
          @click="handleUpdate"
          v-hasPermi="['detail:detail:edit']"
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
          v-hasPermi="['detail:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['detail:detail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="detailId" />
      <!-- <el-table-column label="所属项目单ID" align="center" prop="orderId" /> -->
      <el-table-column label="关联的老人ID" align="center">
        <template slot-scope="scope">
          {{ projectOrderMap[scope.row.orderId] ? projectOrderMap[scope.row.orderId].elderlyId : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="老人姓名" align="center">
        <template slot-scope="scope">
          {{ projectOrderMap[scope.row.orderId] ? projectOrderMap[scope.row.orderId].elderlyName : '-' }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="服务项目ID" align="center">
        <template slot-scope="scope">
          {{ projectOrderMap[scope.row.orderId] ? projectOrderMap[scope.row.orderId].itemId : '-' }}
        </template>
      </el-table-column> -->
      <el-table-column label="服务项目名称" align="center">
        <template slot-scope="scope">
          {{ projectOrderMap[scope.row.orderId] ? projectOrderMap[scope.row.orderId].itemName : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="分配护工ID" align="center" prop="creatorId" />
      <el-table-column label="服务开始日期" align="center" prop="startDate" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务结束日期" align="center" prop="endDate" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="打卡图片" align="center" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="服务状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status !== 4"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['detail:detail:edit']"
          >去打卡</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['detail:detail:remove']"
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

    <!-- 添加或修改项目单明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="所属项目单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入所属项目单ID" />
        </el-form-item>
        <el-form-item label="分配护工ID" prop="creatorId">
          <el-input v-model="form.creatorId" placeholder="请输入分配护工ID" />
        </el-form-item> -->
        <el-form-item label="服务开始日期" prop="startDate">
          <el-date-picker clearable
            v-model="form.startDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择服务开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务结束日期" prop="endDate">
          <el-date-picker clearable
            v-model="form.endDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择服务结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="打卡图片" prop="imageUrl">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="warning" @click="handleProblem">遇到问题</el-button>
        <el-button type="primary" @click="handleComplete">已完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/detail/detail"
import { getProjectOrder } from "@/api/projectOrder/projectOrder"

export default {
  name: "Detail",
  dicts: ['order_status'],
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
      // 项目单明细表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        creatorId: null,
        startDate: null,
        endDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderId: [
          { required: true, message: "所属项目单ID不能为空", trigger: "blur" }
        ],
        creatorId: [
          { required: true, message: "分配护工ID不能为空", trigger: "blur" }
        ],
      },
      // 项目单信息映射
      projectOrderMap: {}
    }
  },
  created() {
    // 获取当前登录用户ID
    let userId = this.$store.getters.userId
    if (!userId && this.$store.state.user) {
      userId = this.$store.state.user.userId || this.$store.state.user.id
    }
    
    // 获取当前用户角色
    const roles = this.$store.getters.roles || (this.$store.state.user && this.$store.state.user.roles) || []
    // 检查是否是护工角色
    const isCaregiver = roles.some(r => r === 'caregiver')
    
    // 只有护工角色才设置creatorId为当前用户ID，其他角色显示全部
    if (userId && isCaregiver) {
      this.queryParams.creatorId = userId
    }
    
    this.getList()
  },
  methods: {
    /** 查询项目单明细列表 */
    getList() {
      this.loading = true
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows
        this.total = response.total
        // 为每个明细获取对应的项目单信息
        this.loading = false
        this.detailList.forEach(detail => {
          this.getProjectOrderInfo(detail.orderId)
        })
      })
    },
    /** 获取项目单信息 */
    getProjectOrderInfo(orderId) {
      if (orderId && !this.projectOrderMap[orderId]) {
        getProjectOrder(orderId).then(response => {
          this.$set(this.projectOrderMap, orderId, response.data)
        })
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        detailId: null,
        orderId: null,
        creatorId: null,
        startDate: null,
        endDate: null,
        remark: null,
        status: null,
        imageUrl: null,
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
      this.ids = selection.map(item => item.detailId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加项目单明细"
    },
    /** 去打卡按钮操作 */
    handleUpdate(row) {
      this.reset()
      const detailId = row.detailId || this.ids
      getDetail(detailId).then(response => {
        this.form = response.data
        // 自动填充当前日期和时间
        const currentDate = new Date()
        const year = currentDate.getFullYear()
        const month = String(currentDate.getMonth() + 1).padStart(2, '0')
        const day = String(currentDate.getDate()).padStart(2, '0')
        const hours = String(currentDate.getHours()).padStart(2, '0')
        const minutes = String(currentDate.getMinutes()).padStart(2, '0')
        const seconds = String(currentDate.getSeconds()).padStart(2, '0')
        const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
        this.form.startDate = formattedDate
        this.form.endDate = formattedDate
        this.open = true
        this.title = "去打卡"
      })
    },
    /** 已完成按钮 */
    handleComplete() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 设置服务状态为已完成
          this.form.status = 4
          if (this.form.detailId != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("打卡成功，任务已完成")
              this.open = false
              this.getList()
            })
          } else {
            addDetail(this.form).then(response => {
              this.$modal.msgSuccess("打卡成功，任务已完成")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 遇到问题按钮 */
    handleProblem() {
      // 检查备注是否为空
      if (!this.form.remark || this.form.remark.trim() === '') {
        this.$modal.msgWarning("请备注问题")
        return
      }
      // 设置服务状态为待审核
      this.form.status = 1
      if (this.form.detailId != null) {
        updateDetail(this.form).then(response => {
          this.$modal.msgSuccess("已提交问题，等待审核")
          this.open = false
          this.getList()
        })
      } else {
        addDetail(this.form).then(response => {
          this.$modal.msgSuccess("已提交问题，等待审核")
          this.open = false
          this.getList()
        })
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const detailIds = row.detailId || this.ids
      this.$modal.confirm('是否确认删除项目单明细编号为"' + detailIds + '"的数据项？').then(function() {
        return delDetail(detailIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('detail/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    },
    /** 获取选中项的状态 */
    getSelectedItemStatus() {
      if (this.ids.length === 0) return 0
      const item = this.detailList.find(item => item.detailId === this.ids[0])
      return item ? item.status : 0
    }
  }
}
</script>
