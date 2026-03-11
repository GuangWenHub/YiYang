<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房间ID" prop="roomId">
        <el-input
          v-model="queryParams.roomId"
          placeholder="请输入房间ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="床位ID" prop="bedId">
        <el-input
          v-model="queryParams.bedId"
          placeholder="请输入床位ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入老人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="查房时间" prop="roundTime">
        <el-date-picker clearable
          v-model="queryParams.roundTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择查房时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="查房人ID" prop="roundUserId">
        <el-input
          v-model="queryParams.roundUserId"
          placeholder="请输入查房人ID"
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
          v-hasPermi="['round:round:add']"
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
          v-hasPermi="['round:round:edit']"
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
          v-hasPermi="['round:round:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['round:round:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roundList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="查房记录ID" align="center" prop="roundId" />
      <el-table-column label="房间ID" align="center" prop="roomId" />
      <el-table-column label="床位ID" align="center" prop="bedId" />
      <el-table-column label="老人ID" align="center" prop="elderlyId" />
      <el-table-column label="查房时间" align="center" prop="roundTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.roundTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="查房人ID" align="center" prop="roundUserId" />
      <el-table-column label="查房状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_round_type" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="异常描述" align="center" prop="abnormalDesc" />
      <el-table-column label="处理措施" align="center" prop="handleMeasure" />
      <el-table-column label="查房图片" align="center" prop="imageUrl" width="100">
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
            v-hasPermi="['round:round:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['round:round:remove']"
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

    <!-- 添加或修改查房记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入老人ID" @input="handleElderlyIdInput" />
          <div v-if="elderlyInfo" class="elderly-info">
            <span>老人姓名：{{ elderlyInfo.name }}</span>
            <span>性别：{{ elderlyInfo.sex === '0' ? '男' : elderlyInfo.sex === '1' ? '女' : '未知' }}</span>
          </div>
        </el-form-item>
        <el-form-item label="房间ID" prop="roomId">
          <el-input v-model="form.roomId" placeholder="请输入房间ID" />
          <div v-if="roomInfo" class="room-info">
            <span>房间号：{{ roomInfo.roomNumber }}</span>
          </div>
        </el-form-item>
        <el-form-item label="床位ID" prop="bedId">
          <el-input v-model="form.bedId" placeholder="请输入床位ID" />
          <div v-if="bedInfo" class="bed-info">
            <span>床位号：{{ bedInfo.bedNumber }}</span>
          </div>
        </el-form-item>
        <el-form-item label="查房时间" prop="roundTime">
          <el-date-picker clearable
            v-model="form.roundTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择查房时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="异常描述" prop="abnormalDesc">
          <el-input v-model="form.abnormalDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="处理措施" prop="handleMeasure">
          <el-input v-model="form.handleMeasure" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="查房图片" prop="imageUrl">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handlePass">通过</el-button>
        <el-button type="danger" @click="handleAbnormal">发现异常</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRound, getRound, delRound, addRound, updateRound } from "@/api/round/round"
import { getElderly } from "@/api/elderly/elderly"
import { getBedByElderlyId, getRoom } from "@/api/room/room"

export default {
  name: "Round",
  dicts: ['sys_round_type'],
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
      // 查房记录表格数据
      roundList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomId: null,
        bedId: null,
        elderlyId: null,
        roundTime: null,
        roundUserId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 老人信息
      elderlyInfo: null,
      // 房间信息
      roomInfo: null,
      // 床位信息
      bedInfo: null,
      // 表单校验
      rules: {
        roomId: [
          { required: true, message: "房间ID不能为空", trigger: "blur" }
        ],
        bedId: [
          { required: true, message: "床位ID不能为空", trigger: "blur" }
        ],
        elderlyId: [
          { required: true, message: "老人ID不能为空", trigger: "blur" }
        ],
        roundTime: [
          { required: true, message: "查房时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "查房状态：0-正常，1-异常不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询查房记录列表 */
    getList() {
      this.loading = true
      listRound(this.queryParams).then(response => {
        this.roundList = response.rows
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
      // 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      const currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`

      this.form = {
        roundId: null,
        roomId: null,
        bedId: null,
        elderlyId: null,
        roundTime: currentTime,
        roundUserId: this.$store.getters.userId,
        status: null,
        abnormalDesc: null,
        handleMeasure: null,
        imageUrl: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      // 重置老人信息
      this.elderlyInfo = null
      // 重置房间和床位信息
      this.roomInfo = null
      this.bedInfo = null
      this.resetForm("form")
    },
    /** 老人ID输入事件处理 */
    handleElderlyIdInput() {
      const elderlyId = this.form.elderlyId
      if (elderlyId) {
        getElderly(elderlyId).then(response => {
          if (response.code === 200) {
            this.elderlyInfo = response.data
            // 根据老人ID查询对应的床位和房间信息
            getBedByElderlyId(elderlyId).then(bedResponse => {
              if (bedResponse.code === 200 && bedResponse.data) {
                this.form.bedId = bedResponse.data.bedId
                this.form.roomId = bedResponse.data.roomId
                this.bedInfo = bedResponse.data
                // 根据房间ID查询房间信息
                getRoom(bedResponse.data.roomId).then(roomResponse => {
                  if (roomResponse.code === 200 && roomResponse.data) {
                    this.roomInfo = roomResponse.data
                  }
                }).catch(() => {
                  // 忽略错误
                })
              }
            }).catch(() => {
              // 忽略错误
            })
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
      this.ids = selection.map(item => item.roundId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加查房记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const roundId = row.roundId || this.ids
      getRound(roundId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改查房记录"
        // 获取老人信息
        if (this.form.elderlyId) {
          getElderly(this.form.elderlyId).then(elderlyResponse => {
            if (elderlyResponse.code === 200) {
              this.elderlyInfo = elderlyResponse.data
              // 根据老人ID查询对应的床位和房间信息
              getBedByElderlyId(this.form.elderlyId).then(bedResponse => {
                if (bedResponse.code === 200 && bedResponse.data) {
                  this.bedInfo = bedResponse.data
                  // 根据房间ID查询房间信息
                  getRoom(bedResponse.data.roomId).then(roomResponse => {
                    if (roomResponse.code === 200 && roomResponse.data) {
                      this.roomInfo = roomResponse.data
                    }
                  }).catch(() => {
                    // 忽略错误
                  })
                }
              }).catch(() => {
                // 忽略错误
              })
            }
          }).catch(() => {
            this.elderlyInfo = null
          })
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roundId != null) {
            updateRound(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRound(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 通过按钮 */
    handlePass() {
      this.form.status = 1
      this.submitForm()
    },
    /** 发现异常按钮 */
    handleAbnormal() {
      if (!this.form.abnormalDesc) {
        this.$modal.alert('请填写异常描述')
        return
      }
      this.form.status = 2
      this.submitForm()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const roundIds = row.roundId || this.ids
      this.$modal.confirm('是否确认删除查房记录编号为"' + roundIds + '"的数据项？').then(function() {
        return delRound(roundIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('round/round/export', {
        ...this.queryParams
      }, `round_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
