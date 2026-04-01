<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddMessage"
          v-hasPermi="['feedback:feedback:add']"
        >留言</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleAddSuggestion"
          v-hasPermi="['feedback:feedback:add']"
        >提交建议</el-button>
      </el-col>
      <el-col :span="12">
        <el-select v-model="viewMode" placeholder="选择查看模式" size="mini" @change="getList">
          <el-option label="我的留言" value="my" />
          <el-option label="全部留言" value="all" v-if="hasAllPermission" />
        </el-select>
      </el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="发送者" prop="senderName">
        <el-input
          v-model="queryParams.senderName"
          placeholder="请输入发送者名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送者角色" prop="senderRole">
        <el-input
          v-model="queryParams.senderRole"
          placeholder="请输入发送者角色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="类型" clearable>
          <el-option label="留言" value="0" />
          <el-option label="建议" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="feedbackList"
      @selection-change="handleSelectionChange"
      row-key="feedbackId"
      :expand-row-keys="expandedRows"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div v-if="replyMap[scope.row.feedbackId] && replyMap[scope.row.feedbackId].length > 0" class="chat-container">
            <div v-for="(reply, index) in replyMap[scope.row.feedbackId]" :key="reply.feedbackId" class="chat-message">
              <div :class="['chat-bubble', reply.senderId === $store.getters.userId ? 'chat-bubble-right' : 'chat-bubble-left']">
                <div class="chat-header">
                  <div class="chat-name">{{ reply.senderName }}</div>
                  <div class="chat-role">{{ reply.senderRole }}</div>
                  <div class="chat-time">{{ parseTime(reply.createTime) }}</div>
                </div>
                <div class="chat-content">{{ reply.content }}</div>
                <div class="chat-footer">
                  <el-tag v-if="reply.status === '0'" type="warning" size="small">未读</el-tag>
                  <el-tag v-else type="success" size="small">已读</el-tag>
                  <div class="chat-actions">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-view"
                      @click="handleViewReply(reply)"
                      v-hasPermi="['feedback:feedback:query']"
                    >查看</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      @click="handleReply(reply)"
                      v-hasPermi="['feedback:feedback:add']"
                    >回复</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleDeleteReply(reply)"
                      v-hasPermi="['feedback:feedback:remove']"
                    >删除</el-button>
                  </div>
                </div>
                <!-- 回复的回复 -->
                <div v-if="replyMap[reply.feedbackId] && replyMap[reply.feedbackId].length > 0" class="chat-replies">
                  <div v-for="(childReply, childIndex) in replyMap[reply.feedbackId]" :key="childReply.feedbackId" class="chat-message">
                    <div :class="['chat-bubble', childReply.senderId === $store.getters.userId ? 'chat-bubble-right' : 'chat-bubble-left']">
                      <div class="chat-header">
                        <div class="chat-name">{{ childReply.senderName }}</div>
                        <div class="chat-role">{{ childReply.senderRole }}</div>
                        <div class="chat-time">{{ parseTime(childReply.createTime) }}</div>
                      </div>
                      <div class="chat-content">{{ childReply.content }}</div>
                      <div class="chat-footer">
                        <el-tag v-if="childReply.status === '0'" type="warning" size="small">未读</el-tag>
                        <el-tag v-else type="success" size="small">已读</el-tag>
                        <div class="chat-actions">
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-view"
                            @click="handleViewReply(childReply)"
                            v-hasPermi="['feedback:feedback:query']"
                          >查看</el-button>
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDeleteReply(childReply)"
                            v-hasPermi="['feedback:feedback:remove']"
                          >删除</el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else style="padding: 20px; text-align: center; color: #999;">
            暂无回复
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型" align="center" prop="type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === '0'" type="info">留言</el-tag>
          <el-tag v-else type="success">建议</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发送者" align="center" prop="senderName" />
      <el-table-column label="发送者角色" align="center" prop="senderRole" />
      <el-table-column label="接收者" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.receiverId">
            {{ scope.row.receiverName }}
          </span>
          <span v-else>全体成员</span>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">未读</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已读</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="primary">已回复</el-tag>
          <el-tag v-else type="danger">有回复未读</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['feedback:feedback:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['feedback:feedback:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 留言对话框 -->
    <el-dialog :title="messageTitle" :visible.sync="messageOpen" width="600px" append-to-body>
      <el-form ref="messageForm" :model="messageForm" :rules="messageRules" label-width="80px">
        <el-form-item label="接收者" prop="receiverId">
          <el-select v-model="messageForm.receiverId" placeholder="请选择接收者" :disabled="isAdmin && messageForm.receiverId === null">
            <el-option label="全体成员" value="" v-if="isAdmin" />
            <el-option
              v-for="user in userOptions"
              :key="user.userId"
              :label="user.nickName + '(' + user.userName + ')'
"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="messageForm.content"
            type="textarea"
            placeholder="请输入留言内容"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMessageForm">确 定</el-button>
        <el-button @click="messageCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 建议对话框 -->
    <el-dialog :title="suggestionTitle" :visible.sync="suggestionOpen" width="600px" append-to-body>
      <el-form ref="suggestionForm" :model="suggestionForm" :rules="suggestionRules" label-width="80px">
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="suggestionForm.content"
            type="textarea"
            placeholder="请输入建议内容"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSuggestionForm">确 定</el-button>
        <el-button @click="suggestionCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog :title="'查看反馈'" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-form ref="viewForm" :model="viewForm" label-width="80px">
        <el-form-item label="类型">
          <el-tag v-if="viewForm.type === '0'" type="info">留言</el-tag>
          <el-tag v-else type="success">建议</el-tag>
        </el-form-item>
        <el-form-item label="发送者">
          <span>{{ viewForm.senderName }}</span>
        </el-form-item>
        <el-form-item label="发送者角色">
          <span>{{ viewForm.senderRole }}</span>
        </el-form-item>
        <el-form-item label="接收者">
          <span v-if="viewForm.receiverId">{{ viewForm.receiverName }}</span>
          <span v-else>全体成员</span>
        </el-form-item>
        <el-form-item label="内容">
          <span>{{ viewForm.content }}</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-tag v-if="viewForm.status === '0'" type="warning">未读</el-tag>
          <el-tag v-else-if="viewForm.status === '1'" type="success">已读</el-tag>
          <el-tag v-else-if="viewForm.status === '2'" type="primary">已回复</el-tag>
          <el-tag v-else type="danger">有回复未读</el-tag>
        </el-form-item>
        <el-form-item label="创建时间">
          <span>{{ parseTime(viewForm.createTime) }}</span>
        </el-form-item>
      </el-form>
      
      <!-- 回复列表 -->
      <div v-if="replyList.length > 0" style="margin-top: 20px;">
        <h4>回复记录</h4>
        <el-divider></el-divider>
        <el-table :data="replyList" style="width: 100%">
          <el-table-column label="发送者" align="center" prop="senderName">
            <template slot-scope="scope">
              <div>
                <div>{{ scope.row.senderName }}</div>
                <div style="font-size: 12px; color: #999;">{{ scope.row.senderRole }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="内容" align="center" prop="content" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === '0'" type="warning">未读</el-tag>
              <el-tag v-else type="success">已读</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="回复时间" align="center" prop="createTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleReply(viewForm)" v-hasPermi="['feedback:feedback:add']">回复</el-button>
        <el-button @click="viewCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog :title="replyTitle" :visible.sync="replyOpen" width="600px" append-to-body>
      <el-form ref="replyForm" :model="replyForm" :rules="replyRules" label-width="80px">
        <el-form-item label="接收者">
          <span>{{ replyForm.receiverName }}</span>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="replyForm.content"
            type="textarea"
            placeholder="请输入回复内容"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReplyForm">确 定</el-button>
        <el-button @click="replyCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFeedback, getReceivedFeedback, getSentFeedback, getAllFeedback, getFeedback, addFeedback, delFeedback, getFeedbackReplies, replyFeedback, updateFeedbackStatus } from "@/api/feedback/feedback"
import { listUser } from "@/api/system/user"

export default {
  name: "Feedback",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 沟通反馈表格数据
      feedbackList: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 查看模式
      viewMode: 'my',
      // 是否为管理员
      isAdmin: false,
      // 留言对话框
      messageOpen: false,
      messageTitle: "",
      messageForm: {
        receiverId: null,
        receiverName: null,
        receiverRole: null,
        type: "0",
        content: ""
      },
      messageRules: {
        receiverId: [
          { required: true, message: "接收者不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      },
      // 建议对话框
      suggestionOpen: false,
      suggestionTitle: "",
      suggestionForm: {
        type: "1",
        content: ""
      },
      suggestionRules: {
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      },
      // 查看对话框
      viewOpen: false,
      viewForm: {},
      // 回复列表
      replyList: [],
      // 回复对话框
      replyOpen: false,
      replyTitle: "",
      replyForm: {
        parentId: null,
        receiverId: null,
        receiverName: null,
        receiverRole: null,
        type: "0",
        content: ""
      },
      replyRules: {
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      },
      // 展开的行
      expandedRows: [],
      // 回复列表，按反馈ID存储
      replyMap: {},
      // 用户选项
      userOptions: [],
      // 是否拥有查看全部留言的权限
      hasAllPermission: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        senderName: undefined,
        senderRole: undefined,
        type: undefined
      }
    }
  },
  created() {
    this.checkAdmin()
    this.checkAllPermission()
    this.getUserList()
    this.getList()
  },
  methods: {
    /** 查询沟通反馈列表 */
    getList() {
      this.loading = true
      if (this.viewMode === 'all' && this.hasAllPermission) {
        // 查看全部留言
        getAllFeedback().then(response => {
          // 过滤掉有父id的回复，只显示主反馈
          const mainFeedback = response.rows.filter(item => !item.parentId)
          this.feedbackList = mainFeedback
          this.total = mainFeedback.length
          // 为每个反馈获取回复列表
          this.feedbackList.forEach(item => {
            this.getReplyList(item.feedbackId)
          })
          this.loading = false
        }).catch(error => {
          console.error('获取反馈列表失败:', error)
          this.feedbackList = []
          this.total = 0
          this.loading = false
          this.$modal.msgError('获取反馈列表失败，请刷新页面重试')
        })
      } else {
        // 同时获取用户收到的和发送的反馈
        Promise.all([getReceivedFeedback(), getSentFeedback()]).then(([receivedResponse, sentResponse]) => {
          // 合并两个列表，并去重
          const allFeedback = [...receivedResponse.rows, ...sentResponse.rows]
          const uniqueFeedback = this.removeDuplicates(allFeedback, 'feedbackId')
          // 过滤掉有父id的回复，只显示主反馈
          const mainFeedback = uniqueFeedback.filter(item => !item.parentId)
          // 按创建时间排序
          mainFeedback.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
          this.feedbackList = mainFeedback
          this.total = mainFeedback.length
          // 为每个反馈获取回复列表
          this.feedbackList.forEach(item => {
            this.getReplyList(item.feedbackId)
          })
          this.loading = false
        }).catch(error => {
          console.error('获取反馈列表失败:', error)
          this.feedbackList = []
          this.total = 0
          this.loading = false
          this.$modal.msgError('获取反馈列表失败，请刷新页面重试')
        })
      }
    },



    /** 去重函数 */
    removeDuplicates(arr, key) {
      const seen = new Set()
      return arr.filter(item => {
        const value = item[key]
        if (seen.has(value)) {
          return false
        }
        seen.add(value)
        return true
      })
    },
    /** 查询用户列表 */
    getUserList() {
      // 为了避免数据权限控制导致的用户列表不完整，直接使用系统用户列表接口
      // 并添加一个特殊参数来获取所有用户
      listUser({ pageNum: 1, pageSize: 100, all: true }).then(response => {
        this.userOptions = response.rows
      })
    },

    /** 检查是否为管理员 */
    checkAdmin() {
      // 检查用户是否拥有管理员或超级管理员角色
      const roles = this.$store.getters.roles || []
      // 管理员角色通常是 'admin' 或 'sysadmin'
      this.isAdmin = roles.includes('admin') || roles.includes('sysadmin')
    },
    /** 检查是否拥有查看全部留言的权限 */
    checkAllPermission() {
      // 检查用户是否拥有 feedback:feedback:all 权限或 ALL_PERMISSION 权限
      const permissions = this.$store.getters.permissions || []
      this.hasAllPermission = permissions.includes('feedback:feedback:all') || permissions.includes('*:*:*')
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
      this.ids = selection.map(item => item.feedbackId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增留言按钮操作 */
    handleAddMessage() {
      this.resetMessageForm()
      this.messageOpen = true
      this.messageTitle = "添加留言"
    },
    /** 新增建议按钮操作 */
    handleAddSuggestion() {
      this.resetSuggestionForm()
      this.suggestionOpen = true
      this.suggestionTitle = "提交建议"
    },
    /** 查看按钮操作 */
    handleView(row) {
      getFeedback(row.feedbackId).then(response => {
        this.viewForm = response.data
        // 获取回复列表
        this.getReplyList(row.feedbackId)
        this.viewOpen = true
        
        // 如果状态是有回复未读，更新为已回复
        if (this.viewForm.status === '3') {
          updateFeedbackStatus(this.viewForm.feedbackId, '2').then(() => {
            // 刷新反馈列表
            this.getList()
          })
        }
      })
    },
    /** 获取回复列表 */
    getReplyList(parentId) {
      getFeedbackReplies(parentId).then(response => {
        const replies = response.rows
        this.replyMap[parentId] = replies
        // 递归获取每个回复的回复
        replies.forEach(reply => {
          this.getReplyList(reply.feedbackId)
        })
      }).catch(error => {
        console.error('获取回复列表失败:', error)
        this.replyMap[parentId] = []
      })
    },
    /** 检查回复是否有子回复 */
    hasChildReplies(feedbackId) {
      return this.replyMap[feedbackId] && this.replyMap[feedbackId].length > 0
    },

    /** 回复按钮操作 */
    handleReply(row) {
      this.resetReplyForm()
      this.replyForm.parentId = row.feedbackId
      // 设置接收者为原发送者
      this.replyForm.receiverId = row.senderId
      this.replyForm.receiverName = row.senderName
      this.replyForm.receiverRole = row.senderRole
      this.replyOpen = true
      this.replyTitle = "回复反馈"
    },
    // 回复表单重置
    resetReplyForm() {
      this.replyForm = {
        parentId: null,
        receiverId: null,
        receiverName: null,
        receiverRole: null,
        type: "0",
        content: ""
      }
      this.resetForm("replyForm")
    },
    // 回复取消按钮
    replyCancel() {
      this.replyOpen = false
      this.resetReplyForm()
    },
    /** 提交回复表单 */
    submitReplyForm() {
      this.$refs["replyForm"].validate(valid => {
        if (valid) {
          replyFeedback(this.replyForm).then(response => {
            this.$modal.msgSuccess("回复成功")
            this.replyOpen = false
            // 刷新回复列表
            this.getReplyList(this.replyForm.parentId)
            // 刷新反馈列表
            this.getList()
            // 展开当前回复的反馈行
            this.expandedRows = [this.replyForm.parentId]
          }).catch(error => {
            this.$modal.msgError(error.msg || "回复失败")
          })
        }
      })
    },
    /** 查看回复 */
    handleViewReply(row) {
      getFeedback(row.feedbackId).then(response => {
        this.viewForm = response.data
        this.viewOpen = true
        // 如果状态是未读，更新为已读
        if (this.viewForm.status === '0') {
          updateFeedbackStatus(this.viewForm.feedbackId, '1').then(() => {
            // 刷新回复列表
            this.getReplyList(this.viewForm.parentId)
            // 检查所有回复是否都已读
            this.checkAllRepliesRead(this.viewForm.parentId)
          })
        }
      })
    },
    /** 检查所有回复是否都已读 */
    checkAllRepliesRead(parentId) {
      getFeedbackReplies(parentId).then(response => {
        const replies = response.rows
        // 检查是否所有回复都已读
        const allRead = replies.every(reply => reply.status === '1')
        if (allRead) {
          // 如果所有回复都已读，将主反馈的状态更新为已回复
          updateFeedbackStatus(parentId, '2').then(() => {
            // 刷新反馈列表
            this.getList()
          })
        }
      })
    },

    /** 删除回复 */
    handleDeleteReply(row) {
      const feedbackId = row.feedbackId
      const parentId = row.parentId
      this.$modal.confirm('是否确认删除回复？').then(function() {
        return delFeedback(feedbackId)
      }).then(() => {
        this.$modal.msgSuccess("删除成功")
        // 刷新回复列表
        this.getReplyList(parentId)
        // 刷新反馈列表
        this.getList()
      }).catch(() => {})
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const feedbackIds = row.feedbackId
      this.$modal.confirm('是否确认删除沟通反馈编号为"' + feedbackIds + '"的数据项？').then(function() {
        return delFeedback(feedbackIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    // 留言表单重置
    resetMessageForm() {
      this.messageForm = {
        receiverId: null,
        receiverName: null,
        receiverRole: null,
        type: "0",
        content: ""
      }
      this.resetForm("messageForm")
    },
    // 建议表单重置
    resetSuggestionForm() {
      this.suggestionForm = {
        type: "1",
        content: ""
      }
      this.resetForm("suggestionForm")
    },
    // 留言取消按钮
    messageCancel() {
      this.messageOpen = false
      this.resetMessageForm()
    },
    // 建议取消按钮
    suggestionCancel() {
      this.suggestionOpen = false
      this.resetSuggestionForm()
    },
    // 查看取消按钮
    viewCancel() {
      this.viewOpen = false
    },
    /** 提交留言表单 */
    submitMessageForm() {
      this.$refs["messageForm"].validate(valid => {
        if (valid) {
          // 查找接收者信息
          if (this.messageForm.receiverId) {
            const user = this.userOptions.find(item => item.userId === this.messageForm.receiverId)
            if (user) {
              this.messageForm.receiverName = user.nickName
              this.messageForm.receiverRole = user.role ? user.role.roleName : ''
            }
          } else {
            // 全体成员
            this.messageForm.receiverId = null
            this.messageForm.receiverName = null
            this.messageForm.receiverRole = null
          }
          addFeedback(this.messageForm).then(response => {
            this.$modal.msgSuccess("留言成功")
            this.messageOpen = false
            this.getList()
          })
        }
      })
    },
    /** 提交建议表单 */
    submitSuggestionForm() {
      this.$refs["suggestionForm"].validate(valid => {
        if (valid) {
          // 建议直接发送给管理员
          this.suggestionForm.receiverId = 1 // 假设管理员ID为1
          addFeedback(this.suggestionForm).then(response => {
            this.$modal.msgSuccess("建议提交成功")
            this.suggestionOpen = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.chat-container {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 8px;
  margin-top: 10px;
}

.chat-message {
  margin-bottom: 15px;
}

.chat-bubble {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 18px;
  position: relative;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chat-bubble-left {
  background-color: #ffffff;
  border-bottom-left-radius: 4px;
  align-self: flex-start;
}

.chat-bubble-right {
  background-color: #e6f7ff;
  border-bottom-right-radius: 4px;
  align-self: flex-end;
  margin-left: auto;
}

.chat-header {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  font-size: 12px;
}

.chat-name {
  font-weight: bold;
  margin-right: 8px;
}

.chat-role {
  color: #999;
  margin-right: 8px;
}

.chat-time {
  color: #999;
}

.chat-content {
  margin-bottom: 5px;
  line-height: 1.4;
  word-break: break-word;
}

.chat-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}

.chat-actions {
  display: flex;
  gap: 5px;
}

.chat-replies {
  margin-top: 10px;
  margin-left: 40px;
  border-left: 2px solid #e6e6e6;
  padding-left: 15px;
}
</style>
