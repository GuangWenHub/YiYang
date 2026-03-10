<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="家属用户ID" prop="familyUserId">
        <el-input
          v-model="queryParams.familyUserId"
          placeholder="请输入家属用户ID"
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
      <el-form-item label="亲属关系" prop="relation">
        <el-input
          v-model="queryParams.relation"
          placeholder="请输入亲属关系"
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
          v-hasPermi="['familyElderly:familyElderly:add']"
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
          v-hasPermi="['familyElderly:familyElderly:edit']"
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
          v-hasPermi="['familyElderly:familyElderly:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['familyElderly:familyElderly:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="familyElderlyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关系ID" align="center" prop="id" />
      <el-table-column label="家属用户ID" align="center" prop="familyUserId" />
      <el-table-column label="家属用户账号" align="center" prop="familyUserName" />
      <el-table-column label="家属真实姓名" align="center" prop="familyRealName" />
      <el-table-column label="老人ID" align="center" prop="elderlyId" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="亲属关系" align="center" prop="relation" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['familyElderly:familyElderly:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['familyElderly:familyElderly:remove']"
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

    <!-- 添加或修改家属-老人关系对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="家属用户ID" prop="familyUserId">
          <el-input v-model="form.familyUserId" placeholder="请输入家属用户ID" />
        </el-form-item>
        <el-form-item label="老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入老人ID" />
        </el-form-item>
        <el-form-item label="亲属关系" prop="relation">
          <el-input v-model="form.relation" placeholder="请输入亲属关系" />
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
import { listFamilyElderly, getFamilyElderly, delFamilyElderly, addFamilyElderly, updateFamilyElderly } from "@/api/familyElderly/familyElderly"

export default {
  name: "FamilyElderly",
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
      // 家属-老人关系表格数据
      familyElderlyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        familyUserId: null,
        elderlyId: null,
        relation: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        familyUserId: [
          { required: true, message: "家属用户ID不能为空", trigger: "blur" }
        ],
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
    /** 查询家属-老人关系列表 */
    getList() {
      this.loading = true
      listFamilyElderly(this.queryParams).then(response => {
        this.familyElderlyList = response.rows
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
        id: null,
        familyUserId: null,
        elderlyId: null,
        relation: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加家属-老人关系"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getFamilyElderly(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改家属-老人关系"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFamilyElderly(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addFamilyElderly(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除家属-老人关系编号为"' + ids + '"的数据项？').then(function() {
        return delFamilyElderly(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('familyElderly/familyElderly/export', {
        ...this.queryParams
      }, `familyElderly_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
