<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="welcome-title">{{ aiData.welcomeMessage }}</h1>
          <p class="welcome-time">
            <i class="el-icon-time"></i>
            {{ aiData.currentTime }}
          </p>
        </div>
        <div class="welcome-avatar">
          <img :src="avatar" alt="avatar" class="avatar-img">
        </div>
      </div>
    </div>

    <!-- AI助手区域 -->
    <div class="ai-assistant-section" v-if="aiData.suggestions && aiData.suggestions.length > 0">
      <div class="ai-header">
        <div class="ai-icon">
          <i class="el-icon-s-opportunity"></i>
        </div>
        <div class="ai-title">
          <span class="ai-name">智能助手 Jarvis</span>
          <span class="ai-status">
            <span class="status-dot"></span>
            在线
          </span>
        </div>
      </div>
      <div class="ai-suggestions">
        <div 
          v-for="(suggestion, index) in aiData.suggestions" 
          :key="index"
          class="suggestion-card"
          @click="handleSuggestionClick(suggestion)"
        >
          <div class="suggestion-icon" :class="suggestion.type">
            <i :class="getSuggestionIcon(suggestion.type)"></i>
          </div>
          <div class="suggestion-content">
            <h4 class="suggestion-title">{{ suggestion.title }}</h4>
            <p class="suggestion-desc">{{ suggestion.content }}</p>
            <span class="suggestion-action">{{ suggestion.action }} <i class="el-icon-arrow-right"></i></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 待办事项区域 -->
    <div class="todo-section" v-if="todoList.length > 0">
      <div class="section-header">
        <h3>
          <i class="el-icon-bell"></i>
          今日待办
          <el-badge :value="todoList.length" class="todo-badge" />
        </h3>
      </div>
      <div class="todo-list">
        <div 
          v-for="todo in todoList" 
          :key="todo.id"
          class="todo-item"
          :class="todo.priority"
          @click="handleTodoClick(todo)"
        >
          <div class="todo-icon">
            <i :class="getTodoIcon(todo.type)"></i>
          </div>
          <div class="todo-info">
            <div class="todo-title">{{ todo.title }}</div>
            <div class="todo-desc">{{ todo.description }}</div>
          </div>
          <div class="todo-priority">
            <el-tag :type="getPriorityType(todo.priority)" size="small">
              {{ getPriorityText(todo.priority) }}
            </el-tag>
          </div>
          <div class="todo-arrow">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计数据区域 -->
    <div class="statistics-section">
      <div class="section-header">
        <h3>
          <i class="el-icon-data-analysis"></i>
          工作数据概览
        </h3>
      </div>
      <div class="stats-grid">
        <!-- 老人统计 -->
        <template v-if="hasRole(['admin', 'doctor'])">
          <div class="stat-card">
            <div class="stat-icon elderly">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.elderlyCount || 0 }}</div>
              <div class="stat-label">老人总数</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon today-elderly">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.todayElderlyCount || 0 }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </div>
        </template>

        <!-- 护理任务统计 -->
        <template v-if="hasRole(['caregiver', 'doctor', 'admin'])">
          <div class="stat-card">
            <div class="stat-icon pending-care">
              <i class="el-icon-first-aid-kit"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingCareCount || 0 }}</div>
              <div class="stat-label">待执行护理</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon completed-care">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCareCount || 0 }}</div>
              <div class="stat-label">今日已完成护理</div>
            </div>
          </div>
        </template>

        <!-- 用药任务统计 -->
        <template v-if="hasRole(['caregiver', 'doctor', 'admin'])">
          <div class="stat-card">
            <div class="stat-icon pending-medication">
              <i class="el-icon-medicine-rack"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingMedicationCount || 0 }}</div>
              <div class="stat-label">待执行用药</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon completed-medication">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedMedicationCount || 0 }}</div>
              <div class="stat-label">今日已完成用药</div>
            </div>
          </div>
        </template>

        <!-- 项目单统计 -->
        <template v-if="hasRole(['doctor', 'admin'])">
          <div class="stat-card">
            <div class="stat-icon pending-order">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingProjectOrderCount || 0 }}</div>
              <div class="stat-label">待审核项目单</div>
            </div>
          </div>
        </template>

        <!-- 交班统计 -->
        <template v-if="hasRole(['doctor'])">
          <div class="stat-card">
            <div class="stat-icon handover">
              <i class="el-icon-refresh"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingHandoverCount || 0 }}</div>
              <div class="stat-label">待交班记录</div>
            </div>
          </div>
        </template>

        <!-- 健康异常统计 -->
        <template v-if="hasRole(['doctor', 'admin'])">
          <div class="stat-card">
            <div class="stat-icon abnormal">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.abnormalHealthCount || 0 }}</div>
              <div class="stat-label">异常健康记录</div>
            </div>
          </div>
        </template>

        <!-- 留言统计 -->
        <div class="stat-card">
          <div class="stat-icon feedback">
            <i class="el-icon-chat-dot-round"></i>
            <span v-if="statistics.unreadFeedbackCount > 0" class="stat-badge">{{ statistics.unreadFeedbackCount }}</span>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayFeedbackCount || 0 }}</div>
            <div class="stat-label">今日留言</div>
          </div>
        </div>

        <!-- 床位统计 -->
        <template v-if="hasRole(['admin'])">
          <div class="stat-card">
            <div class="stat-icon bed">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.bedUsageRate ? statistics.bedUsageRate.toFixed(1) + '%' : '0%' }}</div>
              <div class="stat-label">床位使用率</div>
              <div class="stat-sub">空闲 {{ statistics.freeBedCount || 0 }}/{{ statistics.totalBedCount || 0 }}</div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <!-- 快速入口 -->
    <div class="quick-access-section">
      <div class="section-header">
        <h3>
          <i class="el-icon-s-grid"></i>
          快速入口
        </h3>
      </div>
      <div class="quick-grid">
        <div 
          v-for="item in quickAccessList" 
          :key="item.route"
          class="quick-item"
          @click="$router.push(item.route)"
        >
          <div class="quick-icon" :class="item.color">
            <i :class="item.icon"></i>
          </div>
          <div class="quick-label">{{ item.label }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getStatistics, getTodoList, getAiAssistantData } from '@/api/dashboard/dashboard'
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      // 统计数据
      statistics: {},
      // 待办列表
      todoList: [],
      // AI助手数据
      aiData: {
        welcomeMessage: '',
        currentTime: '',
        suggestions: []
      },
      // 快速入口
      quickAccessList: [],
      // 当前时间定时器
      timeTimer: null
    }
  },
  computed: {
    ...mapGetters(['avatar', 'roles', 'name', 'nickName'])
  },
  created() {
    this.initQuickAccess()
    this.loadDashboardData()
  },
  mounted() {
    // 每秒更新时间
    this.timeTimer = setInterval(() => {
      this.updateCurrentTime()
    }, 1000)
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
  },
  methods: {
    // 加载首页数据
    loadDashboardData() {
      // 获取统计数据
      getStatistics().then(response => {
        this.statistics = response.data || {}
      })

      // 获取待办列表
      getTodoList().then(response => {
        this.todoList = response.data || []
      })

      // 获取AI助手数据
      getAiAssistantData().then(response => {
        this.aiData = response.data || {}
      })
    },

    // 更新当前时间
    updateCurrentTime() {
      if (this.aiData.currentTime) {
        const now = new Date()
        const timeStr = now.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        })
        this.$set(this.aiData, 'currentTime', timeStr)
      }
    },

    // 检查角色权限
    hasRole(roleKeys) {
      return roleKeys.some(role => this.roles.includes(role))
    },

    // 初始化快速入口
    initQuickAccess() {
      const allQuickAccess = [
        { label: '老人档案', icon: 'el-icon-user', route: '/elderly/elderly', color: 'blue', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '护理记录', icon: 'el-icon-first-aid-kit', route: '/careRecord/careRecord', color: 'green', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '用药记录', icon: 'el-icon-medicine-rack', route: '/medicationRecord/medicationRecord', color: 'orange', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '健康档案', icon: 'el-icon-document-checked', route: '/record/record', color: 'purple', roles: ['admin', 'doctor'] },
        { label: '医嘱管理', icon: 'el-icon-document', route: '/medicalOrder/medicalOrder', color: 'red', roles: ['admin', 'doctor'] },
        { label: '交班记录', icon: 'el-icon-refresh', route: '/handover/handover', color: 'cyan', roles: ['admin', 'doctor'] },
        { label: '项目单', icon: 'el-icon-document-copy', route: '/projectOrder/projectOrder', color: 'pink', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '留言反馈', icon: 'el-icon-chat-dot-round', route: '/feedback/feedback', color: 'yellow', roles: ['admin', 'doctor', 'caregiver', 'family'] },
        { label: '床位管理', icon: 'el-icon-office-building', route: '/room/room', color: 'indigo', roles: ['admin'] },
        { label: '药品管理', icon: 'el-icon-box', route: '/medicine/medicine', color: 'teal', roles: ['admin', 'doctor'] },
        { label: '服务项目管理', icon: 'el-icon-s-operation', route: '/serviceItem/serviceItem', color: 'lime', roles: ['admin'] },
        { label: '用户管理', icon: 'el-icon-s-custom', route: '/system/user', color: 'gray', roles: ['admin'] }
      ]

      this.quickAccessList = allQuickAccess.filter(item => 
        item.roles.some(role => this.roles.includes(role))
      )
    },

    // 获取建议图标
    getSuggestionIcon(type) {
      const iconMap = {
        task: 'el-icon-first-aid-kit',
        health: 'el-icon-warning',
        medical: 'el-icon-document',
        handover: 'el-icon-refresh',
        system: 'el-icon-monitor',
        general: 'el-icon-info'
      }
      return iconMap[type] || 'el-icon-info'
    },

    // 获取待办图标
    getTodoIcon(type) {
      const iconMap = {
        care: 'el-icon-first-aid-kit',
        medication: 'el-icon-medicine-rack',
        handover: 'el-icon-refresh',
        projectOrder: 'el-icon-document-copy',
        feedback: 'el-icon-chat-dot-round'
      }
      return iconMap[type] || 'el-icon-bell'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      const typeMap = {
        high: 'danger',
        medium: 'warning',
        low: 'info'
      }
      return typeMap[priority] || 'info'
    },

    // 获取优先级文本
    getPriorityText(priority) {
      const textMap = {
        high: '高',
        medium: '中',
        low: '低'
      }
      return textMap[priority] || '普通'
    },

    // 处理建议点击
    handleSuggestionClick(suggestion) {
      this.$message.info(`正在跳转到：${suggestion.action}`)
      // 根据建议类型跳转到对应页面
      const routeMap = {
        task: '/careRecord/index',
        health: '/record/index',
        medical: '/medicalOrder/index',
        handover: '/handover/index',
        system: '/elderly/index',
        general: '/dashboard/index'
      }
      if (routeMap[suggestion.type]) {
        this.$router.push(routeMap[suggestion.type])
      } else {
        this.$message.warning('暂未开放')
      }
    },

    // 处理待办点击
    handleTodoClick(todo) {
      if (todo.route) {
        this.$router.push(todo.route)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

// 欢迎区域
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 20px;
  color: white;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);

  .welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .welcome-title {
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 10px 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .welcome-time {
    font-size: 16px;
    opacity: 0.9;
    margin: 0;
    
    i {
      margin-right: 8px;
    }
  }

  .avatar-img {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    border: 4px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

// AI助手区域
.ai-assistant-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

  .ai-header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;

    .ai-icon {
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }
    }

    .ai-title {
      display: flex;
      flex-direction: column;

      .ai-name {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }

      .ai-status {
        font-size: 13px;
        color: #67c23a;
        display: flex;
        align-items: center;
        margin-top: 4px;

        .status-dot {
          width: 8px;
          height: 8px;
          background: #67c23a;
          border-radius: 50%;
          margin-right: 6px;
          animation: pulse 2s infinite;
        }
      }
    }
  }

  .ai-suggestions {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 16px;

    .suggestion-card {
      background: #f8f9fa;
      border-radius: 12px;
      padding: 20px;
      display: flex;
      align-items: flex-start;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
        border-color: #667eea;
      }

      .suggestion-icon {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        flex-shrink: 0;

        i {
          font-size: 20px;
          color: white;
        }

        &.task { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.health { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.medical { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        &.handover { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.system { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
        &.general { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); }
      }

      .suggestion-content {
        flex: 1;

        .suggestion-title {
          font-size: 15px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;
        }

        .suggestion-desc {
          font-size: 13px;
          color: #606266;
          margin: 0 0 12px 0;
          line-height: 1.5;
        }

        .suggestion-action {
          font-size: 13px;
          color: #667eea;
          font-weight: 500;

          i {
            margin-left: 4px;
            transition: transform 0.3s;
          }
        }
      }

      &:hover .suggestion-action i {
        transform: translateX(4px);
      }
    }
  }
}

// 待办事项区域
.todo-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

  .section-header {
    margin-bottom: 20px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #667eea;
      }
    }
  }

  .todo-badge {
    margin-left: 8px;
  }

  .todo-list {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .todo-item {
      display: flex;
      align-items: center;
      padding: 16px 20px;
      background: #f8f9fa;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      border-left: 4px solid transparent;

      &:hover {
        background: #f0f2f5;
        transform: translateX(4px);
      }

      &.high { border-left-color: #f56c6c; }
      &.medium { border-left-color: #e6a23c; }
      &.low { border-left-color: #909399; }

      .todo-icon {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 20px;
          color: white;
        }
      }

      .todo-info {
        flex: 1;

        .todo-title {
          font-size: 15px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .todo-desc {
          font-size: 13px;
          color: #606266;
        }
      }

      .todo-priority {
        margin-right: 16px;
      }

      .todo-arrow {
        color: #c0c4cc;
        transition: color 0.3s;
      }

      &:hover .todo-arrow {
        color: #667eea;
      }
    }
  }
}

// 统计数据区域
.statistics-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

  .section-header {
    margin-bottom: 20px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #667eea;
      }
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;

    .stat-card {
      background: #f8f9fa;
      border-radius: 12px;
      padding: 20px;
      display: flex;
      align-items: center;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
      }

      .stat-icon {
        width: 56px;
        height: 56px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        position: relative;

        i {
          font-size: 28px;
          color: white;
        }

        .stat-badge {
          position: absolute;
          top: -4px;
          right: -4px;
          background: #f56c6c;
          color: white;
          font-size: 12px;
          width: 20px;
          height: 20px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        &.elderly { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.today-elderly { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.pending-care { background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%); }
        &.completed-care { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.pending-medication { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
        &.completed-medication { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        &.pending-order { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); i { color: #e6a23c; } }
        &.handover { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); i { color: #67c23a; } }
        &.abnormal { background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); i { color: #f56c6c; } }
        &.feedback { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.bed { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
      }

      .stat-info {
        .stat-value {
          font-size: 24px;
          font-weight: 700;
          color: #303133;
          line-height: 1.2;
        }

        .stat-label {
          font-size: 13px;
          color: #606266;
          margin-top: 4px;
        }

        .stat-sub {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }
  }
}

// 快速入口区域
.quick-access-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

  .section-header {
    margin-bottom: 20px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #667eea;
      }
    }
  }

  .quick-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
    gap: 16px;

    .quick-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
      }

      .quick-icon {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;

        i {
          font-size: 24px;
          color: white;
        }

        &.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.green { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.orange { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
        &.purple { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); i { color: #667eea; } }
        &.red { background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%); }
        &.cyan { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        &.pink { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.yellow { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); i { color: #e6a23c; } }
        &.indigo { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.teal { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
        &.lime { background: linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%); i { color: #67c23a; } }
        &.gray { background: linear-gradient(135deg, #e0e0e0 0%, #f5f5f5 100%); i { color: #606266; } }
      }

      .quick-label {
        font-size: 13px;
        color: #606266;
        text-align: center;
      }
    }
  }
}

// 动画
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

// 响应式
@media (max-width: 768px) {
  .welcome-section {
    .welcome-content {
      flex-direction: column;
      text-align: center;
    }

    .welcome-avatar {
      margin-top: 20px;
    }
  }

  .ai-suggestions {
    grid-template-columns: 1fr !important;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr) !important;
  }
}
</style>
