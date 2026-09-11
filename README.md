# 颐养管理系统

一款面向智慧医养结合服务中心的综合管理平台，深度融合"医"与"养"两大核心服务，通过数字化手段实现养老服务的标准化、精细化和智能化管理。

系统基于 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 前后端分离框架二次开发，后端采用 Spring Boot + MyBatis，前端采用 Vue 2 + Element UI，并集成基于 Dify 的 RAG 智能问答助手。

## 背景

随着我国老龄化程度持续加深，养老机构对"医养结合"服务的需求日益增长。本系统旨在解决传统养老服务中信息不透明、服务难追溯、多方协同效率低等问题。

![中国老龄化持续加深](docs/images/aging-chart.png)

## 核心特色

- **医养融合**：将医疗护理与生活照料有机结合，为老人提供全方位的健康保障
- **服务监管**：所有服务过程可追溯、可监管，确保服务质量
- **角色协同**：管理员、医生、护工、家属多方协同，形成完整的服务闭环
- **智能高效**：自动化的任务分配、超时检测和异常上报机制，提升运营效率
- **AI 助手**：集成基于 Dify 的 RAG 智能问答，按角色提供个性化解答

## 使用角色

| 角色 | 主要职责 |
|------|----------|
| 超级管理员 | 系统最高权限，全局配置和管理 |
| 管理员 | 用户权限、老人档案、服务项目、房间设施、组织架构管理 |
| 医生 | 健康档案、医嘱开立、健康评估、医疗交接班 |
| 护工 | 生活照料、用药跟踪、服务项目打卡、异常上报 |
| 家属 | 查看老人档案与健康记录、留言沟通 |

## 功能结构

![系统功能结构图](docs/images/system-structure.png)

## 系统界面

### 工作台首页

首页聚合工作建议与数据概览，待审核项目、异常健康记录、超时任务等一目了然。

![工作台首页](docs/images/dashboard.png)

### 服务项目库

管理员可维护足浴、推拿、血糖监测、喂药、洗头等服务项目，支持分类、定价与启用/停用。

![服务项目库](docs/images/service-items.png)

### 健康档案记录

医生为老人记录血压、心率、体温、体重、血糖及症状描述，形成连续的健康档案。

![健康档案记录](docs/images/health-records.png)

### 护工任务列表

护工查看分配到的服务项目任务，支持打卡、上传打卡图片、记录备注，超时任务自动标记。

![护工任务列表](docs/images/nurse-tasks.png)

### 家属视图

家属可查看关联老人的健康档案记录，了解老人健康状况。

![家属视图](docs/images/family-view.png)

### 留言板

医生、护工、家属之间可留言沟通，支持按角色发送与回复。

![留言板](docs/images/message-board.png)

### 颐养助手（AI 问答）

基于 Dify 的 RAG 智能问答助手，按角色（管理员/医生/护工/家属）提供个性化解答，支持查询老人档案、用药建议、操作指引等。

![颐养助手](docs/images/ai-assistant.png)

## AI 助手技术流程

AI 助手采用 RAG（检索增强生成）架构：用户提问经向量化后在知识库中做语义检索，检索结果与提示词融合后交由大语言模型流式生成，前端通过 SSE 逐字渲染。

![基于 Dify 的 RAG 问答系统流程](docs/images/rag-flow.png)

## 技术栈

**后端**

- Spring Boot 2.5.15
- Spring Security + JWT 认证
- MyBatis + PageHelper
- Druid 数据源
- MySQL 8.0
- Redis

**前端**

- Vue 2 + Vue CLI
- Element UI
- Axios
- ECharts

**AI**

- Dify（RAG 智能问答）

## 项目结构

```
YiYang
├── yiyang-admin        # 后端启动模块（Web 入口、控制器）
├── yiyang-common       # 通用工具与基础类
├── yiyang-framework    # 框架核心（安全、拦截器、数据源等）
├── yiyang-system       # 系统业务模块（用户、角色、菜单等）
├── yiyang-quartz       # 定时任务模块
├── yiyang-generator    # 代码生成模块
├── yiyang-ui           # 前端 Vue 工程
├── sql                 # 数据库脚本
└── docs                # 文档与系统截图
```

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.x
- MySQL 8.0
- Redis
- Node.js（前端构建）

### 后端

1. 创建数据库并导入 `sql/sqlAll.sql`
2. 修改 `yiyang-admin/src/main/resources/application-druid.yml` 中的数据库连接（地址、用户名、密码）
3. 修改 `yiyang-admin/src/main/resources/application.yml` 中的 Redis 连接与 JWT 密钥
4. （可选）配置 Dify AI 的 `url` 与 `key`
5. 启动 `yiyang-admin` 模块，默认端口 `8081`

### 前端

```bash
cd yiyang-ui
npm install
npm run dev
```

## 说明

- 配置文件中涉及数据库密码、Redis 密码、JWT 密钥、Dify API Key 等敏感信息均已替换为占位符，部署时请自行填写。
- 本项目基于 RuoYi 框架（MIT License）二次开发，详见 [LICENSE](LICENSE)。
