<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房间号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入房间号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼层" prop="floor">
        <el-input
          v-model="queryParams.floor"
          placeholder="请输入楼层"
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
          v-hasPermi="['room:room:add']"
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
          v-hasPermi="['room:room:edit']"
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
          v-hasPermi="['room:room:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['room:room:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="房间ID" align="center" prop="roomId" />
      <el-table-column label="房间号" align="center" prop="roomNumber" />
      <el-table-column label="楼层" align="center" prop="floor" />
      <el-table-column label="床位容量" align="center" prop="capacity" />
      <el-table-column label="床位状态" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.bedList && scope.row.bedList.length > 0">
            <div v-for="(bed, index) in scope.row.bedList" :key="index" style="margin-bottom: 6px; padding: 4px 8px; background-color: #f5f7fa; border-radius: 4px; display: flex; justify-content: space-between; align-items: center;">
              <div style="display: flex; align-items: center;">
                <span style="font-weight: 500; margin-right: 8px;">{{ bed.bedNumber }}:</span>
                <dict-tag :options="dict.type.bed_status" :value="bed.status"/>
              </div>
              <div v-if="bed.elderlyId && elderlyMap[bed.elderlyId]" style="background-color: #ecf5ff; padding: 2px 8px; border-radius: 12px; font-size: 12px; color: #409EFF;">
                {{ elderlyMap[bed.elderlyId].name }}
              </div>
              <div v-else style="font-size: 12px; color: #909399;">未分配</div>
            </div>
          </div>
          <span v-else>无床位</span>
        </template>
      </el-table-column>
      <el-table-column label="房间图片" align="center" prop="imageUrl">
        <template slot-scope="scope">
          <image-preview :src="scope.row.avatar" :width="50" :height="50"/>
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
            v-hasPermi="['room:room:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['room:room:remove']"
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

    <!-- 添加或修改房间信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-input v-model="form.floor" placeholder="请输入楼层" />
        </el-form-item>
        <el-form-item label="床位容量" prop="capacity">
          <el-input v-model="form.capacity" placeholder="请输入床位容量" />
        </el-form-item>
        <el-form-item label="房间图片路径" prop="imageUrl">
          <image-upload v-model="form.avatar"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-divider content-position="center">床位信息信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddBed">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteBed">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="bedList" :row-class-name="rowBedIndex" @selection-change="handleBedSelectionChange" ref="bed">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="床位编号" prop="bedNumber" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.bedNumber" placeholder="请输入床位编号" />
            </template>
          </el-table-column>
          <el-table-column label="床位状态" prop="status" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择床位状态">
                <el-option
                  v-for="dict in dict.type.bed_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="当前分配的老人ID" prop="elderlyId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.elderlyId" placeholder="请输入当前分配的老人ID" @input="handleElderlyIdInput(scope.row)" />
              <div v-if="scope.row.elderlyId && elderlyMap[scope.row.elderlyId]" style="margin-top: 4px; padding: 2px 8px; background-color: #ecf5ff; border-radius: 12px; font-size: 12px; color: #409EFF; display: inline-block;">
                {{ elderlyMap[scope.row.elderlyId].name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRoom, getRoom, delRoom, addRoom, updateRoom } from "@/api/room/room"
import { getElderly } from "@/api/elderly/elderly"

export default {
  name: "Room",
  dicts: ['bed_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedBed: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房间信息表格数据
      roomList: [],
      // 床位信息表格数据
      bedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomNumber: null,
        floor: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roomNumber: [
          { required: true, message: "房间号不能为空", trigger: "blur" }
        ],
        capacity: [
          { required: true, message: "床位容量不能为空", trigger: "blur" }
        ],
      },
      // 老人信息映射
      elderlyMap: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询房间信息列表 */
    getList() {
      this.loading = true
      listRoom(this.queryParams).then(response => {
        this.roomList = response.rows
        this.total = response.total
        // 为每个有老人ID的床位获取老人信息
        this.roomList.forEach(room => {
          if (room.bedList && room.bedList.length > 0) {
            room.bedList.forEach(bed => {
              if (bed.elderlyId) {
                this.getElderlyInfo(bed.elderlyId)
              }
            })
          }
        })
        this.loading = false
      })
    },
    /** 获取老人信息 */
    getElderlyInfo(elderlyId) {
      if (elderlyId && !this.elderlyMap[elderlyId]) {
        getElderly(elderlyId).then(response => {
          if (response.code === 200) {
            this.$set(this.elderlyMap, elderlyId, response.data)
          }
        }).catch(() => {
          // 忽略错误
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
        roomId: null,
        roomNumber: null,
        floor: null,
        capacity: null,
        imageUrl: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      this.bedList = []
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
      this.ids = selection.map(item => item.roomId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加房间信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const roomId = row.roomId || this.ids
      getRoom(roomId).then(response => {
        this.form = response.data
        this.bedList = response.data.bedList
        // 为每个有老人ID的床位获取老人信息
        if (response.data.bedList && response.data.bedList.length > 0) {
          response.data.bedList.forEach(bed => {
            if (bed.elderlyId) {
              this.getElderlyInfo(bed.elderlyId)
            }
          })
        }
        this.open = true
        this.title = "修改房间信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.bedList = this.bedList
          if (this.form.roomId != null) {
            updateRoom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRoom(this.form).then(response => {
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
      const roomIds = row.roomId || this.ids
      this.$modal.confirm('是否确认删除房间信息编号为"' + roomIds + '"的数据项？').then(function() {
        return delRoom(roomIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 床位信息序号 */
    rowBedIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 床位信息添加按钮操作 */
    handleAddBed() {
      let obj = {}
      obj.bedNumber = ""
      obj.status = ""
      obj.elderlyId = ""
      obj.remark = ""
      this.bedList.push(obj)
    },
    /** 床位信息删除按钮操作 */
    handleDeleteBed() {
      if (this.checkedBed.length == 0) {
        this.$modal.msgError("请先选择要删除的床位信息数据")
      } else {
        const bedList = this.bedList
        const checkedBed = this.checkedBed
        this.bedList = bedList.filter(function(item) {
          return checkedBed.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleBedSelectionChange(selection) {
      this.checkedBed = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('room/room/export', {
        ...this.queryParams
      }, `room_${new Date().getTime()}.xlsx`)
    },
    /** 老人ID输入事件处理 */
    handleElderlyIdInput(bed) {
      const elderlyId = bed.elderlyId
      if (elderlyId) {
        this.getElderlyInfo(elderlyId)
      }
    },
    /** 获取床位状态标签 */
    getBedStatusLabel(status) {
      const bedStatusDict = this.dict.type.bed_status
      if (bedStatusDict) {
        const statusItem = bedStatusDict.find(item => item.value == status)
        return statusItem ? statusItem.label : '未知状态'
      }
      return '未知状态'
    }
  }
}
</script>
