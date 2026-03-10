<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联的老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入关联的老人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开单人用户ID" prop="creatorId">
        <el-input
          v-model="queryParams.creatorId"
          placeholder="请输入开单人用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开单时间" prop="createdTime">
        <el-date-picker clearable
          v-model="queryParams.createdTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开单时间">
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
          v-hasPermi="['projectOrder:projectOrder:add']"
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
          v-hasPermi="['projectOrder:projectOrder:edit']"
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
          v-hasPermi="['projectOrder:projectOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['projectOrder:projectOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目单ID" align="center" prop="orderId" />
      <el-table-column label="关联的老人ID" align="center" prop="elderlyId" />
      <el-table-column label="关联的老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="服务项目" align="center" prop="itemName" />
      <!-- <el-table-column label="开单人用户ID" align="center" prop="creatorId" />
      <el-table-column label="开单人角色" align="center" prop="creatorRole">
         <template slot-scope="scope">
          <dict-tag :options="dict.type.user_role_type" :value="scope.row.creatorRole"/>
        </template>
      </el-table-column> -->
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="护工" align="center" v-if="isAdmin">
        <template slot-scope="scope">
          <span v-if="scope.row.orderDetails && scope.row.orderDetails.length > 0">
            {{ getCaregiverName(scope.row.orderDetails[0].creatorId) }}
          </span>
          <span v-else>未分配</span>
        </template>
      </el-table-column>
      <el-table-column label="开单时间" align="center" prop="createdTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开单备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['projectOrder:projectOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['projectOrder:projectOrder:remove']"
          >删除</el-button>
          <el-button
            v-if="isAdmin"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAudit(scope.row)"
          >审核</el-button>
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

    <!-- 添加或修改项目单主对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联的老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联的老人ID" @input="handleElderlyIdInput" />
          <div v-if="elderlyInfo" class="elderly-info">
            <span>老人姓名：{{ elderlyInfo.name }}</span>
            <span>性别：{{ elderlyInfo.sex === '0' ? '男' : elderlyInfo.sex === '1' ? '女' : '未知' }}</span>
          </div>
        </el-form-item>
        <el-form-item label="服务项目" prop="itemId">
          <el-select v-model="form.itemId" placeholder="请选择服务项目" @change="handleItemIdChange">
            <el-option
              v-for="item in serviceItemList"
              :key="item.itemId"
              :label="item.name"
              :value="item.itemId"
            />
          </el-select>
          <div v-if="serviceItemInfo" class="elderly-info">
            <span>项目分类：{{ serviceItemInfo.category }}</span>
            <span>单价：{{ serviceItemInfo.price }}</span>
          </div>
        </el-form-item>
        <el-form-item label="开单人用户ID" prop="creatorId">
          <el-input
            v-model="form.creatorId"
            placeholder="系统将自动填充"
            disabled
          />
        </el-form-item>
        <el-form-item label="开单人角色" prop="creatorRole">
          <el-radio-group v-model="form.creatorRole" disabled>
            <el-radio
              v-for="dict in dict.type.user_role_type"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审核人用户ID" prop="auditorId">
          <el-input
            v-model="form.auditorId"
            placeholder="系统将自动填充"
            disabled
          />
        </el-form-item>

        <el-form-item label="开单时间" prop="createdTime">
          <el-date-picker clearable
            v-model="form.createdTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开单备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提交审核</el-button>
        <el-button @click="saveDraft">存草稿</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="审核项目单" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="100px">
        <el-form-item label="老人姓名">
          <el-input v-model="auditForm.elderlyName" disabled />
        </el-form-item>
        <el-form-item label="服务项目">
          <el-input v-model="auditForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="当前状态">
          <dict-tag :options="dict.type.order_status" :value="auditForm.status"/>
        </el-form-item>
        <el-form-item label="分配护工" prop="caregiverId">
          <el-select v-model="auditForm.caregiverId" placeholder="请选择护工" clearable>
            <el-option
              v-for="user in caregiverList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="审核备注" prop="remark">
          <el-input v-model="auditForm.remark" type="textarea" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditOpen = false">取 消</el-button>
        <el-button type="danger" @click="submitAudit(3)">驳回</el-button>
        <el-button type="primary" @click="submitAudit(2)">通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProjectOrder, getProjectOrder, delProjectOrder, addProjectOrder, updateProjectOrder, auditProjectOrder } from "@/api/projectOrder/projectOrder"
import { getElderly } from "@/api/elderly/elderly"
import { listServiceItem } from "@/api/serviceItem/serviceItem"
import { listUser } from "@/api/system/user"

export default {
  name: "ProjectOrder",
  dicts: ['order_status', 'user_role_type'],
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
      // 项目单主表格数据
      projectOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示审核对话框
      auditOpen: false,
      // 是否为管理员
      isAdmin: false,
      // 是否为普通管理员（admin2），不显示草稿
      isAdmin2: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elderlyId: null,
        creatorId: null,
        status: null,
        createdTime: null,
      },
      // 表单参数
      form: {},
      // 审核表单参数
      auditForm: {},
      // 老人信息
      elderlyInfo: null,
      // 服务项目列表
      serviceItemList: [],
      // 当前选中的服务项目信息
      serviceItemInfo: null,
      // 护工列表
      caregiverList: [],
      // 表单校验
      rules: {
        elderlyId: [
          { required: true, message: "关联的老人ID不能为空", trigger: "blur" }
        ],
        creatorId: [
          { required: true, message: "开单人用户ID不能为空", trigger: "blur" }
        ],
        creatorRole: [
          { required: true, message: "开单人角色：1-管理员,2-医生不能为空", trigger: "blur" }
        ],
      },
      // 审核表单校验
      auditRules: {
        newStatus: [
          { required: true, message: "请选择审核状态", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getServiceItemList()
    this.checkAdminRole()
    this.getCaregiverList()
  },
  methods: {
    /** 检查是否为管理员角色 */
    checkAdminRole() {
      const roles = this.$store.getters.roles || (this.$store.state.user && this.$store.state.user.roles) || []
      this.isAdmin = roles.some(r => r === 'admin' || r === 'admin2')
      this.isAdmin2 = roles.some(r => r === 'admin2')
    },
    /** 获取护工列表 */
    getCaregiverList() {
      listUser({ pageSize: 100 }).then(response => {
        console.log('用户列表数据:', response.rows)
        // 过滤出角色为护工的用户（角色key为caregiver）
        this.caregiverList = response.rows.filter(user => {
          return user.roles && user.roles.some(role => role.roleKey === 'caregiver')
        })
      })
    },
    /** 根据护工ID获取护工名字 */
    getCaregiverName(caregiverId) {
      if (!caregiverId) return '未分配'
      const user = this.caregiverList.find(u => u.userId === caregiverId)
      return user ? user.nickName : '未知护工'
    },
    /** 获取服务项目列表 */
    getServiceItemList() {
      listServiceItem({ pageSize: 100 }).then(response => {
        this.serviceItemList = response.rows
      })
    },
    /** 查询项目单主列表 */
    getList() {
      this.loading = true
      listProjectOrder(this.queryParams).then(response => {
        let list = response.rows
        // 如果是admin2（普通管理员），过滤掉草稿状态的项目单
        if (this.isAdmin2) {
          list = list.filter(item => item.status !== 0)
        }
        this.projectOrderList = list
        this.total = list.length
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
        orderId: null,
        elderlyId: null,
        itemId: null,
        creatorId: null,
        creatorRole: null,
        auditorId: null,
        status: null,
        createdTime: null,
        submittedTime: null,
        approvedTime: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      // 重置老人信息
      this.elderlyInfo = null
      // 重置服务项目信息
      this.serviceItemInfo = null
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
    /** 服务项目选择事件处理 */
    handleItemIdChange() {
      const itemId = this.form.itemId
      if (itemId) {
        const serviceItem = this.serviceItemList.find(item => item.itemId === itemId)
        if (serviceItem) {
          this.serviceItemInfo = serviceItem
        } else {
          this.serviceItemInfo = null
        }
      } else {
        this.serviceItemInfo = null
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()

      // --- 1. 获取用户 ID (已有逻辑) ---
      let userId = this.$store.getters.userId
      if (!userId && this.$store.state.user) {
        userId = this.$store.state.user.userId || this.$store.state.user.id
      }

      // --- 2. 获取用户角色 (新增逻辑) ---
      let userRoleValue = null

      // 获取当前用户的角色列表 (通常是字符串数组，如 ['admin', 'common'])
      // 若依标准 getters: roles, 或者 state.user.roles
      const roles = this.$store.getters.roles || (this.$store.state.user && this.$store.state.user.roles) || []

      console.log('当前用户角色列表:', roles) // 调试用：请在控制台查看这里打印的是什么

      // 根据实际项目中的角色标识进行映射
      // 假设：包含 'admin' 或 'administrator' 映射为 1，包含 'doctor' 或 'common' 映射为 2
      if (roles && roles.length > 0) {
        // 策略 A: 优先判断是否有管理员角色
        if (roles.some(r => r === 'admin' || r === 'administrator' || r === '超级管理员')) {
          userRoleValue = 1
        }
        // 策略 B: 否则判断是否有医生/普通用户角色
        else if (roles.some(r => r === 'doctor' || r === 'common' || r === '普通用户')) {
          userRoleValue = 2
        }
        // 策略 C: 如果只有一个角色且不知道是什么，暂时默认取第一个逻辑对应的值，或者根据需求调整
        else {
           // 如果系统只有两种角色，且不是管理员，则默认为医生
           // 请根据你控制台打印的 roles 内容调整上面的判断条件
           userRoleValue = 2
        }
      }

      // --- 3. 赋值与校验 ---
      if (userId) {
        this.form.creatorId = userId

        // 如果成功获取到角色值，则赋值
        if (userRoleValue !== null) {
          this.form.creatorRole = userRoleValue
        } else {
          // 如果没匹配到，可以给个默认值，或者提示
          this.form.creatorRole = 2 // 默认给医生，或者保持 null 让用户(如果能改的话)选，但既然禁用了就必须给值
          this.$modal.msgWarning("未识别到具体角色类型，已默认设置为医生")
        }

        // 设置固定的审核人用户ID为101
        this.form.auditorId = 101
        // 设置开单时间为当前时间
        this.form.createdTime = new Date().toISOString().split('T')[0]

        this.open = true
        this.title = "添加项目单主"
      } else {
        this.$modal.msgWarning("未检测到登录用户信息，请重新登录")
        console.log('当前 Store User 状态:', this.$store.state.user)
      }
    },



    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getProjectOrder(orderId).then(response => {
        this.form = response.data
        // 设置固定的审核人用户ID为101
        this.form.auditorId = 101
        // 查询老人信息
        if (this.form.elderlyId) {
          getElderly(this.form.elderlyId).then(elderlyResponse => {
            if (elderlyResponse.code === 200) {
              this.elderlyInfo = elderlyResponse.data
            }
          })
        }
        // 查询服务项目信息
        if (this.form.itemId) {
          const serviceItem = this.serviceItemList.find(item => item.itemId === this.form.itemId)
          if (serviceItem) {
            this.serviceItemInfo = serviceItem
          }
        }
        this.open = true
        this.title = "修改项目单主"
      })
    },
    /** 提交审核按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 设置状态为提交审核
          this.form.status = 1
          if (this.form.orderId != null) {
            updateProjectOrder(this.form).then(response => {
              this.$modal.msgSuccess("提交审核成功")
              this.open = false
              this.getList()
            })
          } else {
            addProjectOrder(this.form).then(response => {
              this.$modal.msgSuccess("提交审核成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 存草稿按钮 */
    saveDraft() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 设置状态为草稿
          this.form.status = 0
          if (this.form.orderId != null) {
            updateProjectOrder(this.form).then(response => {
              this.$modal.msgSuccess("保存草稿成功")
              this.open = false
              this.getList()
            })
          } else {
            addProjectOrder(this.form).then(response => {
              this.$modal.msgSuccess("保存草稿成功")
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
      this.$modal.confirm('是否确认删除项目单主编号为"' + orderIds + '"的数据项？').then(function() {
        return delProjectOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('projectOrder/projectOrder/export', {
        ...this.queryParams
      }, `projectOrder_${new Date().getTime()}.xlsx`)
    },
    /** 审核按钮操作 */
    handleAudit(row) {
      // 从orderDetails中获取已有的护工ID
      let caregiverId = null
      if (row.orderDetails && row.orderDetails.length > 0) {
        caregiverId = row.orderDetails[0].creatorId
      }
      
      this.auditForm = {
        orderId: row.orderId,
        elderlyId: row.elderlyId,
        elderlyName: row.elderlyName,
        itemId: row.itemId,
        itemName: row.itemName,
        status: row.status,
        newStatus: row.status,
        caregiverId: caregiverId,
        remark: ''
      }
      this.auditOpen = true
    },
    /** 提交审核 */
    submitAudit(status) {
      // 如果是通过，必须选择护工
      if (status === 1 && !this.auditForm.caregiverId) {
        this.$modal.msgError("通过审核必须分配护工")
        return
      }
      
      const auditData = {
        orderId: this.auditForm.orderId,
        status: status,
        remark: this.auditForm.remark,
        orderDetails: []
      }
      
      if (status === 1 && this.auditForm.caregiverId) {
        auditData.orderDetails.push({
          creatorId: this.auditForm.caregiverId
        })
      }
      
      auditProjectOrder(auditData).then(response => {
        this.$modal.msgSuccess("审核成功")
        this.auditOpen = false
        this.getList()
      })
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
