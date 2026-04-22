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
          <span class="ai-name">工作建议</span>
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
        <template v-if="hasRole(['admin', 'admin2', 'doctor', 'caregiver'])">
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
              <div class="stat-label">今日新增老人</div>
            </div>
          </div>
        </template>

        <!-- 护理任务统计（管理员/管理员2：查看今日全部） -->
        <template v-if="hasRole(['caregiver'])">
          <div class="stat-card">
            <div class="stat-icon pending-care">
              <i class="el-icon-first-aid-kit"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingCareCount || 0 }}</div>
              <div class="stat-label">今日待执行护理</div>
            </div>
          </div>
        </template>
          <template v-if="hasRole(['admin', 'admin2'])">
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

        <!-- 护理任务统计（护工：查看自己的）管理员/管理员2：查看今日全部） -->
        <template v-if="hasRole(['caregiver'])">
          <div class="stat-card">
            <div class="stat-icon pending-care">
              <i class="el-icon-first-aid-kit"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingCareCount || 0 }}</div>
              <div class="stat-label">待执行护理</div>
            </div>
          </div>
        </template>
          <template v-if="hasRole(['admin', 'admin2'])">
          <div class="stat-card">
            <div class="stat-icon completed-care">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCareCount || 0 }}</div>
              <div class="stat-label">今日护理任务</div>
            </div>
          </div>
        </template>

        <!-- 用药任务统计（管理员/管理员2：查看今日全部） -->
        <template v-if="hasRole(['caregiver'])">
          <div class="stat-card">
            <div class="stat-icon pending-medication">
              <i class="el-icon-medicine-rack"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingMedicationCount || 0 }}</div>
              <div class="stat-label">今日待执行用药</div>
            </div>
          </div>
        </template>
        <template v-if="hasRole(['admin', 'admin2'])">
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

        <!-- 用药任务统计（护工：查看自己的） -->
        <template v-if="hasRole(['caregiver'])">
          <div class="stat-card">
            <div class="stat-icon pending-medication">
              <i class="el-icon-medicine-rack"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingMedicationCount || 0 }}</div>
              <div class="stat-label">待执行用药</div>
            </div>
          </div>
        </template>
        <template v-if="hasRole(['admin', 'admin2'])">
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
        <template v-if="hasRole(['doctor', 'admin', 'admin2'])">
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
        <template v-if="hasRole(['doctor', 'admin', 'admin2'])">
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

        <!-- 超时和异常统计（管理员/管理员2：查看全部） -->
        <template v-if="hasRole(['admin', 'admin2'])">
          <div class="stat-card">
            <div class="stat-icon timeout-care">
              <i class="el-icon-time"></i>
              <span v-if="statistics.timeoutCareCount > 0" class="stat-badge">{{ statistics.timeoutCareCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.timeoutCareCount || 0 }}</div>
              <div class="stat-label">超时生活照料</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon abnormal-care">
              <i class="el-icon-warning"></i>
              <span v-if="statistics.abnormalCareCount > 0" class="stat-badge">{{ statistics.abnormalCareCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.abnormalCareCount || 0 }}</div>
              <div class="stat-label">生活照料异常</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon timeout-med">
              <i class="el-icon-time"></i>
              <span v-if="statistics.timeoutMedicationCount > 0" class="stat-badge">{{ statistics.timeoutMedicationCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.timeoutMedicationCount || 0 }}</div>
              <div class="stat-label">超时用药任务</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon abnormal-med">
              <i class="el-icon-warning"></i>
              <span v-if="statistics.abnormalMedicationCount > 0" class="stat-badge">{{ statistics.abnormalMedicationCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.abnormalMedicationCount || 0 }}</div>
              <div class="stat-label">用药异常</div>
            </div>
          </div>
        </template>

        <!-- 超时和异常统计（护工：查看自己的） -->
        <template v-if="hasRole(['caregiver'])">
          <div class="stat-card">
            <div class="stat-icon timeout-care">
              <i class="el-icon-time"></i>
              <span v-if="statistics.timeoutCareCount > 0" class="stat-badge">{{ statistics.timeoutCareCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.timeoutCareCount || 0 }}</div>
              <div class="stat-label">超时生活照料</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon abnormal-care">
              <i class="el-icon-warning"></i>
              <span v-if="statistics.abnormalCareCount > 0" class="stat-badge">{{ statistics.abnormalCareCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.abnormalCareCount || 0 }}</div>
              <div class="stat-label">生活照料异常</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon timeout-med">
              <i class="el-icon-time"></i>
              <span v-if="statistics.timeoutMedicationCount > 0" class="stat-badge">{{ statistics.timeoutMedicationCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.timeoutMedicationCount || 0 }}</div>
              <div class="stat-label">超时用药任务</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon abnormal-med">
              <i class="el-icon-warning"></i>
              <span v-if="statistics.abnormalMedicationCount > 0" class="stat-badge">{{ statistics.abnormalMedicationCount }}</span>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.abnormalMedicationCount || 0 }}</div>
              <div class="stat-label">用药异常</div>
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
            <div class="stat-value">{{ statistics.unreadFeedbackCount || 0 }}</div>
            <div class="stat-label">未读留言</div>
          </div>
        </div>

        <!-- 床位统计 -->
        <template v-if="hasRole(['admin', 'admin2'])">
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
          v-for="(item, index) in quickAccessList" 
          :key="item.moduleKey + index"
          class="quick-item"
          :class="{ 'disabled': item.disabled }"
          @click="handleQuickAccessClick(item)"
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
import { getStatistics, getAiAssistantData } from '@/api/dashboard/dashboard'
import { mapGetters } from 'vuex'
import path from 'path'

export default {
  name: 'Dashboard',
  data() {
    return {
      // 统计数据
      statistics: {},
      // AI助手数据
      aiData: {
        welcomeMessage: '',
        currentTime: '',
        suggestions: []
      },
      // 快速入口
      quickAccessList: [],
      // 当前时间定时器
      timeTimer: null,
      // 路由缓存
      routeCache: new Map(),
      // 路由查找状态
      routeLoading: false
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
      // 从 store 中获取侧边栏路由
      const sidebarRoutes = this.$store.getters.sidebarRouters || []
      
      console.log('=== 首页调试信息 ===')
      console.log('当前用户角色:', this.roles)
      console.log('侧边栏路由数量:', sidebarRoutes.length)
      console.log('所有侧边栏路由:', sidebarRoutes)
      
      // 定义快速入口配置（图标、颜色等UI属性 + 模块标识）
      const quickAccessConfig = [
        { label: '老人档案', icon: 'el-icon-user', moduleKey: 'elderly', color: 'blue', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '护理记录', icon: 'el-icon-first-aid-kit', moduleKey: 'careRecord', color: 'green', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '用药记录', icon: 'el-icon-medicine-rack', moduleKey: 'medicationRecord', color: 'orange', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '健康档案', icon: 'el-icon-document-checked', moduleKey: 'record', color: 'purple', roles: ['admin', 'doctor'] },
        { label: '医嘱管理', icon: 'el-icon-document', moduleKey: 'medicalOrder', color: 'red', roles: ['admin', 'doctor'] },
        { label: '交班记录', icon: 'el-icon-refresh', moduleKey: 'handover', color: 'cyan', roles: ['admin', 'doctor'] },
        { label: '项目单', icon: 'el-icon-document-copy', moduleKey: 'projectOrder', color: 'pink', roles: ['admin', 'doctor', 'caregiver'] },
        { label: '留言反馈', icon: 'el-icon-chat-dot-round', moduleKey: 'feedback', color: 'yellow', roles: ['admin', 'doctor', 'caregiver', 'family'] },
        { label: '床位管理', icon: 'el-icon-office-building', moduleKey: 'room', color: 'indigo', roles: ['admin'] },
        { label: '药品管理', icon: 'el-icon-box', moduleKey: 'medicine', color: 'teal', roles: ['admin', 'doctor'] },
        { label: '服务项目管理', icon: 'el-icon-s-operation', moduleKey: 'serviceItem', color: 'lime', roles: ['admin'] },
        { label: '用户管理', icon: 'el-icon-s-custom', moduleKey: 'system/user', color: 'gray', roles: ['admin'] }
      ]

      // 根据角色权限过滤，并从真实路由中获取路径
      this.quickAccessList = quickAccessConfig
        .filter(item => item.roles.some(role => this.roles.includes(role)))
        .map(item => {
          // 从真实路由中查找对应的路径（包括完整的层级路径）
          const routeInfo = this.findRouteWithFullPath(item.moduleKey, sidebarRoutes)
          console.log(`模块 [${item.label}] (key: ${item.moduleKey}):`, routeInfo ? `找到路由 -> ${routeInfo.fullPath}` : '❌ 未找到路由')
          
          return {
            ...item,
            route: routeInfo ? routeInfo.fullPath : null
          }
        })
        .filter(item => item.route !== null) // 只保留找到路由的项
      
      console.log('最终快速入口列表:', this.quickAccessList)
      console.log('==================')
    },

    // 根据模块key查找路由
    findRouteByModuleKey(moduleKey, routes) {
      // 检查缓存
      if (this.routeCache.has(moduleKey)) {
        console.log(`从缓存中获取路由: ${moduleKey}`)
        return this.routeCache.get(moduleKey)
      }
      
      try {
        for (const route of routes) {
          // 检查当前路由是否匹配
          if (this.isRouteMatch(moduleKey, route)) {
            // 缓存结果
            this.routeCache.set(moduleKey, route)
            console.log(`缓存路由: ${moduleKey} -> ${route.path}`)
            return route
          }
          // 递归查找子路由
          if (route.children && route.children.length > 0) {
            const found = this.findRouteByModuleKey(moduleKey, route.children)
            if (found) {
              // 缓存结果
              this.routeCache.set(moduleKey, found)
              return found
            }
          }
        }
      } catch (error) {
        console.error('路由查找错误:', error)
      }
      return null
    },

    // 查找路由并构建完整的层级路径
    findRouteWithFullPath(moduleKey, routes, parentPath = '') {
      try {
        for (const route of routes) {
          // 构建当前路由的完整路径，使用path.resolve，与侧边栏保持一致
          const currentPath = parentPath ? path.resolve(parentPath, route.path) : route.path
          
          console.log(`检查路由 - 模块: ${moduleKey}, 路径: ${currentPath}, 父路径: ${parentPath}`)
          
          // 检查当前路由是否匹配
          if (this.isRouteMatch(moduleKey, route)) {
            console.log(`找到匹配路由: ${moduleKey} -> ${currentPath}`)
            return {
              route: route,
              fullPath: currentPath
            }
          }
          
          // 递归查找子路由
          if (route.children && route.children.length > 0) {
            const found = this.findRouteWithFullPath(moduleKey, route.children, currentPath)
            if (found) {
              return found
            }
          }
        }
      } catch (error) {
        console.error('查找路由完整路径错误:', error)
      }
      return null
    },

    // 判断路由是否匹配模块key
    isRouteMatch(moduleKey, route) {
      const routePath = route.path || ''
      const routeName = (route.name || '').toLowerCase()
      const metaTitle = (route.meta && route.meta.title) || ''
      
      console.log(`路由匹配检查 - 模块: ${moduleKey}, 路由路径: ${routePath}, 路由名称: ${routeName}, 标题: ${metaTitle}`)
      
      // 多种匹配策略
      const strategies = [
        // 策略1: 路径完全匹配
        () => routePath === moduleKey,
        // 策略2: 路径包含模块key（更宽松的匹配）
        () => routePath.includes(moduleKey),
        // 策略3: 路由名称匹配
        () => routeName.includes(moduleKey.toLowerCase()),
        // 策略4: 标题包含模块key的中文映射
        () => {
          const titleMap = {
            'elderly': '老人',
            'careRecord': '护理',
            'medicationRecord': '用药',
            'record': '健康',
            'medicalOrder': '医嘱',
            'handover': '交班',
            'projectOrder': '项目单',
            'feedback': '留言',
            'room': '床位',
            'medicine': '药品',
            'serviceItem': '服务项目',
            'system/user': '用户'
          }
          const chineseKeyword = titleMap[moduleKey]
          return chineseKeyword && metaTitle.includes(chineseKeyword)
        }
      ]
      
      const isMatch = strategies.some(strategy => strategy())
      console.log(`路由匹配结果 - ${isMatch ? '匹配成功' : '匹配失败'}: ${moduleKey}`)
      return isMatch
    },

    // 获取建议图标
    getSuggestionIcon(type) {
      const iconMap = {
        warning: 'el-icon-circle-close',
        task: 'el-icon-s-order',
        review: 'el-icon-document-checked',
        handover: 'el-icon-refresh',
        health: 'el-icon-first-aid-kit',
        message: 'el-icon-chat-dot-round',
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
    async handleSuggestionClick(suggestion) {
      try {
        this.routeLoading = true
        
        // 优先使用 routeKey 字段
        const moduleKey = suggestion.routeKey
        
        if (moduleKey) {
          if (moduleKey === 'index') {
            this.$router.push('/index')
          } else {
            // 从真实路由中查找路径（包括完整的层级路径）
            const sidebarRoutes = this.$store.getters.sidebarRouters || []
            const routeInfo = this.findRouteWithFullPath(moduleKey, sidebarRoutes)
            if (routeInfo) {
              console.log(`找到路由: ${routeInfo.fullPath}`)
              this.$router.push(routeInfo.fullPath)
            } else {
              this.$message.warning('该功能暂未开放或您没有权限访问')
            }
          }
        } else {
          this.$message.warning('暂未开放')
        }
      } catch (error) {
        console.error('建议点击错误:', error)
        this.$message.error('跳转失败，请重试')
      } finally {
        this.routeLoading = false
      }
    },

    // 处理待办点击
    async handleTodoClick(todo) {
      try {
        this.routeLoading = true
        
        console.log(`待办事项点击 - 标题: ${todo.title}, 类型: ${todo.type}, 路由: ${todo.route}`)
        
        // 如果待办已经有route字段,直接使用
        if (todo.route) {
          console.log(`实际跳转URL: ${todo.route}`)
          this.$message.success(`正在处理${todo.title}...`)
          this.$router.push(todo.route)
        } else if (todo.type) {
          // 否则根据type查找对应的路由
          const moduleKeyMap = {
            care: 'careRecord',
            medication: 'medicationRecord',
            handover: 'handover',
            projectOrder: 'projectOrder',
            feedback: 'feedback'
          }
          
          const moduleKey = moduleKeyMap[todo.type]
          console.log(`待办类型映射 - 类型: ${todo.type}, 模块Key: ${moduleKey}`)
          
          if (moduleKey) {
            const sidebarRoutes = this.$store.getters.sidebarRouters || []
            const routeInfo = this.findRouteWithFullPath(moduleKey, sidebarRoutes)
            if (routeInfo) {
              console.log(`找到路由: ${routeInfo.fullPath}`)
              this.$message.success(`正在处理${todo.title}...`)
              console.log(`实际跳转URL: ${routeInfo.fullPath}`)
              this.$router.push(routeInfo.fullPath)
            } else {
              this.$message.warning('该功能暂未开放或您没有权限访问')
            }
          } else {
            this.$message.warning('暂未开放')
          }
        }
      } catch (error) {
        console.error('待办点击错误:', error)
        this.$message.error('跳转失败，请重试')
      } finally {
        this.routeLoading = false
      }
    },

    // 处理快速入口点击
    async handleQuickAccessClick(item) {
      try {
        if (item.disabled) return
        
        this.routeLoading = true
        
        if (item.route) {
          console.log(`快速入口点击 - 模块: ${item.label}, 路由: ${item.route}`)
          this.$message.success(`正在跳转到${item.label}...`)
          // 使用与侧边栏一致的路由跳转方式
          console.log(`实际跳转URL: ${item.route}`)
          this.$router.push(item.route)
        } else {
          this.$message.warning('该功能暂未开放')
        }
      } catch (error) {
        console.error('快速入口点击错误:', error)
        this.$message.error('跳转失败，请重试')
      } finally {
        this.routeLoading = false
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

        &.warning { background: linear-gradient(135deg, #f56c6c 0%, #e6a23c 100%); }
        &.task { background: linear-gradient(135deg, #409eff 0%, #667eea 100%); }
        &.review { background: linear-gradient(135deg, #67c23a 0%, #43e97b 100%); }
        &.handover { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.health { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.message { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
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
        &.timeout-care { background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%); }
        &.abnormal-care { background: linear-gradient(135deg, #e6a23c 0%, #f5af19 100%); }
        &.timeout-med { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
        &.abnormal-med { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
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
  
      &.disabled {
        opacity: 0.5;
        cursor: not-allowed;
            
        &:hover {
          transform: none;
          box-shadow: none;
        }
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
