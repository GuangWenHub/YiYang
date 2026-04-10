<template>
  <div class="chat-layout">
    <div class="history-panel">
      <button class="add-chat-btn" @click="addChat">+ 新增对话</button>
      <div class="history-title">历史记录</div>
      <ul class="history-list">
        <li
          v-for="item in historyList"
          :key="item.id"
          class="history-item"
          :class="{ active: item.id === currentChatId }"
          @click="selectChat(item)"
          style="display: flex; align-items: center; justify-content: space-between;"
        >
          <span style="flex:1;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">{{ item.name }}</span>
          <span v-if="item.id !== null" @click.stop="handleDelete(item.id)" style="margin-left:8px;cursor:pointer;display:flex;align-items:center;">
            <svg width="14" height="14" viewBox="0 0 1024 1024" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M320 896c0 35.2 28.8 64 64 64h256c35.2 0 64-28.8 64-64V320H320v576zm576-704h-192l-32-64H352l-32 64H128v64h768v-64z" fill="#f56c6c"/>
            </svg>
          </span>
        </li>
      </ul>
    </div>
    <div class="ai-chat-simple">
      <div class="chat-main">
        <div class="messages" ref="messagesRef">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.role]"
          >
            <span class="role">
              <template v-if="message.role === 'user'"></template>
              <template v-else>
                <img src="@/assets/logo/logo.png" alt="AI Logo" class="ai-logo" />
              </template>
            </span>
            <span class="content" v-if="message.role === 'user'">{{ message.content }}</span>
            <span class="content" v-else v-html="renderMarkdown(message.content)"></span>
          </div>
        </div>
        <div class="input-area">
          <textarea
            v-model="userInput"
            @keydown="handleKeydown"
            placeholder="请输入您的问题..."
            rows="1"
            ref="inputRef"
          ></textarea>
          <button class="send-button" @click="sendMessage" :disabled="isStreaming || !userInput.trim()">
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { sendChatMessage, getChatHistoryIds, getChatHistoryDetail, deleteChatHistory } from '@/api/chat-ai/chat-ai'

export default {
  name: 'ChatAI',
  data() {
    return {
      messages: [],
      userInput: '',
      isStreaming: false,
      currentChatId: null,
      isNewChat: true,
      historyList: []
    }
  },
  mounted() {
    this.loadChatHistory()
  },
  methods: {
    async loadChatHistory() {
      try {
        const response = await getChatHistoryIds()
        if (response.data.length === 0) {
          this.historyList = [{ id: null, name: '新会话' }]
          this.currentChatId = null
          this.isNewChat = true
        } else {
          this.historyList = response.data
          this.currentChatId = response.data[0].id
          this.isNewChat = false
          await this.loadChatDetail(response.data[0].id)
        }
      } catch (error) {
        this.$message.error('加载聊天历史失败')
        console.error('加载聊天历史失败:', error)
      }
    },
    
    async loadChatDetail(chatId) {
      try {
        const response = await getChatHistoryDetail(chatId)
        console.log('加载聊天详情响应:', response)
        // 若依框架返回格式: { code: 200, data: [...] }
        const historyData = response.data || response
        if (!Array.isArray(historyData)) {
          console.error('历史数据格式错误:', historyData)
          this.messages = []
          return
        }
        this.messages = this.transformHistoryData(historyData)
        console.log('转换后的消息:', this.messages)
      } catch (error) {
        console.error('加载聊天详情失败:', error)
      }
    },
    
    addChat() {
      this.currentChatId = null
      this.isNewChat = true
      this.historyList.unshift({ id: null, name: '新会话' })
      this.messages = []
    },
    
    async selectChat(item) {
      this.currentChatId = item.id
      this.isNewChat = item.id === null
      this.messages = []
      
      if (item.id !== null) {
        await this.loadChatDetail(item.id)
      }
    },
    
    async sendMessage() {
      if (this.isStreaming || !this.userInput.trim()) return
      
      const prompt = this.userInput.trim()
      this.messages.push({ role: 'user', content: prompt })
      this.userInput = ''
      this.scrollToBottom()
      
      const aiMsg = { role: 'assistant', content: '' }
      this.messages.push(aiMsg)
      this.isStreaming = true
      
      try {
        const chatId = this.isNewChat ? null : this.currentChatId
        
        console.log('1. 准备发送请求，数据:', { prompt, chatId })
        
        const response = await sendChatMessage({ prompt, chatId })
        console.log('2. 收到响应，状态码:', response.status)
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        // 检查响应头中是否包含新的会话ID（新对话时）
        if (this.isNewChat) {
          console.log('响应头信息:', [...response.headers.entries()])
          const newChatId = response.headers.get('X-Chat-Id') || response.headers.get('chat-id') || response.headers.get('Chat-Id')
          console.log('尝试获取的会话ID:', newChatId)
          if (newChatId) {
            this.currentChatId = newChatId
            // 更新历史列表中对应的项
            const newChatItem = this.historyList.find(item => item.id === null)
            if (newChatItem) {
              newChatItem.id = newChatId
            }
            console.log('成功设置会话ID:', newChatId)
          } else {
            console.log('未在响应头中找到会话ID')
          }
        }
        
        if (!response.body) {
          console.error('❌ response.body 为空!')
          console.log('response 对象:', response)
          console.log('response.bodyUsed:', response.bodyUsed)
          throw new Error('浏览器不支持流式响应')
        }
        
        console.log('✅ response.body 存在，准备读取流')
        const reader = response.body.getReader()
        const decoder = new TextDecoder('utf-8')
        let buffer = ''
        let chunkCount = 0
        let isComplete = false // 标记是否收到完整响应
        let updateTimer = null // 防抖定时器
        const UPDATE_INTERVAL = 50 // 更新间隔(毫秒),平衡流畅度和性能

        while (true) {
          try {
            console.log('⏳ 等待读取数据块...')
            const { value, done } = await reader.read()
            console.log('📦 读取结果 - done:', done, 'value长度:', value ? value.length : 0)
            
            if (done) {
              console.log('✅ 流读取完成')
              // 清除定时器,确保最后的内容显示
              if (updateTimer) {
                clearTimeout(updateTimer)
                this.$nextTick(() => {
                  this.scrollToBottom()
                })
              }
              break
            }
            
            chunkCount++
            // 解码新接收的数据并添加到缓冲区
            const chunk = decoder.decode(value, { stream: true })
            console.log('📨 收到数据块 #', chunkCount, ':', chunk)
            buffer += chunk
            
            // 按行处理SSE数据
            const lines = buffer.split('\n')
            // 保留最后一个可能不完整的行
            buffer = lines.pop() || ''
            
            for (const line of lines) {
              const trimmedLine = line.trim()
              if (!trimmedLine) continue
              
              console.log('处理行:', trimmedLine)
              
              // 处理SSE格式: data: xxx
                if (trimmedLine.startsWith('data: ')) {
                  const data = trimmedLine.substring(6)
                  
                  // 跳过结束标记
                  if (data === '[DONE]') {
                    console.log('收到结束标记')
                    isComplete = true
                    continue
                  }
                  
                  // 如果已经收到结束标记，不再处理后续内容
                  if (isComplete) {
                    console.log('已收到结束标记，跳过后续内容:', data)
                    continue
                  }
                  
                  try {
                    // 尝试解析JSON
                    const jsonData = JSON.parse(data)
                    console.log('解析JSON:', jsonData)
                    
                    // 根据Dify API的响应格式提取内容
                    let newText = ''
                    const eventType = jsonData.event
                    
                    console.log('事件类型:', eventType)
                    console.log('jsonData.answer:', jsonData.answer)
                    console.log('jsonData.thought:', jsonData.thought)
                    console.log('jsonData.content:', jsonData.content)
                    
                    // 处理不同的事件类型
                  if (eventType === 'agent_thought' && jsonData.thought) {
                    // Agent的思考过程，包含完整回答，直接覆盖
                    newText = jsonData.thought
                    console.log('匹配: agent_thought + thought (完整内容，覆盖)', newText)
                  } else if (eventType === 'message' && jsonData.answer) {
                    // Chat模式的回答
                    newText = jsonData.answer
                    console.log('匹配: message + answer')
                  } else if (eventType === 'agent_message' && jsonData.answer) {
                    // Agent模式的回答（片段），正常追加
                    newText = jsonData.answer
                    console.log('匹配: agent_message + answer (片段)', newText)
                  } else if (jsonData.answer) {
                    // 通用answer字段
                    newText = jsonData.answer
                    console.log('匹配: 通用answer')
                  } else if (jsonData.content) {
                    // 通用content字段
                    newText = jsonData.content
                    console.log('匹配: 通用content')
                  } else if (jsonData.thought) {
                    // 通用thought字段
                    newText = jsonData.thought
                    console.log('匹配: 通用thought, 内容:', newText)
                  } else if (jsonData.text) {
                    // 处理OpenAI格式的响应
                    newText = jsonData.text
                    console.log('匹配: OpenAI text')
                  } else {
                    console.log('未匹配任何条件')
                  }
                    
                    // 显示内容
                    if (newText) {
                      // 检查是否是agent_thought事件（完整内容）
                      if (eventType === 'agent_thought') {
                        // 完整内容，直接覆盖
                        aiMsg.content = newText
                        console.log('✅ 覆盖为完整内容:', newText)
                      } else {
                        // 流式输出，正常追加
                        aiMsg.content += newText
                        console.log('✅ 流式输出:', newText)
                      }
                      
                      // 使用防抖优化更新频率,避免频繁DOM操作
                      if (updateTimer) {
                        clearTimeout(updateTimer)
                      }
                      updateTimer = setTimeout(() => {
                        this.$nextTick(() => {
                          this.scrollToBottom()
                        })
                      }, UPDATE_INTERVAL)
                    } else {
                      console.log('❌ 未找到可显示的文本内容,事件类型:', eventType)
                    }
                  } catch (e) {
                    console.error('JSON解析失败:', e)
                    // 如果不是JSON，直接作为文本处理
                    if (data !== '[DONE]') {
                      // 流式输出，正常追加
                      aiMsg.content += data
                      console.log('直接流式输出:', data)
                      this.$nextTick(() => {
                        this.scrollToBottom()
                      })
                    }
                  }
              } else if (trimmedLine.startsWith('{')) {
                // 如果已经收到结束标记，不再处理后续内容
                if (isComplete) {
                  console.log('已收到结束标记，跳过后续内容:', trimmedLine)
                  continue
                }
                
                // 处理非标准SSE格式的JSON响应
                try {
                  const jsonData = JSON.parse(trimmedLine)
                  console.log('解析非SSE JSON:', jsonData)
                  
                  let newText = ''
                  const eventType = jsonData.event
                  
                  if (eventType === 'message' && jsonData.answer) {
                    newText = jsonData.answer
                  } else if (eventType === 'agent_message' && jsonData.answer) {
                    newText = jsonData.answer
                  } else if (eventType === 'agent_thought' && jsonData.thought) {
                    newText = jsonData.thought
                  } else if (jsonData.answer) {
                    newText = jsonData.answer
                  } else if (jsonData.content) {
                    newText = jsonData.content
                  } else if (jsonData.thought) {
                    newText = jsonData.thought
                  } else if (jsonData.text) {
                    newText = jsonData.text
                  }
                  
                  if (newText) {
                    // 流式输出，正常追加
                    aiMsg.content += newText
                    console.log('✅ 流式输出(非SSE):', newText)
                    
                    // 使用防抖优化更新频率
                    if (updateTimer) {
                      clearTimeout(updateTimer)
                    }
                    updateTimer = setTimeout(() => {
                      this.$nextTick(() => {
                        this.scrollToBottom()
                      })
                    }, UPDATE_INTERVAL)
                  }
                } catch (e) {
                  // 忽略无法解析的行
                }
              }
            }
          } catch (readError) {
            console.error('读取流错误:', readError)
            break
          }
        }

        console.log('流式读取完成，最终内容:', aiMsg.content)
        this.isStreaming = false
        this.scrollToBottom()
        await this.refreshHistoryList()
        
      } catch (error) {
        console.error('发送消息失败:', error)
        aiMsg.content = '抱歉，发生了错误，请稍后重试。'
        this.isStreaming = false
        this.scrollToBottom()
      }
    },
    
    handleKeydown(e) {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault()
        this.sendMessage()
      }
    },
    
    transformHistoryData(historyData) {
      const transformedMessages = []
      historyData.forEach(item => {
        if (item.query) {
          transformedMessages.push({
            role: 'user',
            content: item.query
          })
        }
        if (item.answer !== undefined) {
          transformedMessages.push({
            role: 'assistant',
            content: item.answer
          })
        }
      })
      return transformedMessages
    },
    
    scrollToBottom() {
      this.$nextTick(() => {
        const messagesRef = this.$refs.messagesRef
        if (messagesRef) {
          messagesRef.scrollTop = messagesRef.scrollHeight
        }
      })
    },
    
    async refreshHistoryList() {
      try {
        const response = await getChatHistoryIds()
        if (response.data && response.data.length > 0) {
          this.historyList = response.data
          
          if (this.isNewChat && this.messages.length > 0) {
            const firstUserMessage = this.messages.find(msg => msg.role === 'user')
            if (firstUserMessage) {
              const latestItem = response.data[0]
              if (latestItem) {
                this.currentChatId = latestItem.id
                this.isNewChat = false
              }
            }
          }
        }
      } catch (error) {
        console.error('刷新历史列表失败:', error)
      }
    },
    
    async handleDelete(chatId) {
      try {
        await deleteChatHistory(chatId)
        
        const response = await getChatHistoryIds()
        if (response.data.length === 0) {
          this.historyList = [{ id: null, name: '新会话' }]
          this.currentChatId = null
          this.isNewChat = true
          this.messages = []
        } else {
          this.historyList = response.data
          if (this.currentChatId === chatId) {
            this.currentChatId = response.data[0].id
            this.isNewChat = false
            await this.loadChatDetail(response.data[0].id)
          }
        }
        this.$message.success('删除成功')
      } catch (error) {
        this.$message.error('删除失败')
        console.error('删除聊天历史失败:', error)
      }
    },
    
    renderMarkdown(text) {
      if (!text) return ''
      
      // 简单的markdown渲染
      let result = text
      
      // 处理换行
      result = result.replace(/\n/g, '<br>')
      // 处理粗体
      result = result.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      // 处理斜体
      result = result.replace(/\*(.*?)\*/g, '<em>$1</em>')
      // 处理代码块
      result = result.replace(/```(.*?)```/g, '<pre><code>$1</code></pre>')
      // 处理链接
      result = result.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>')
      
      return result
    }
  }
}
</script>

<style scoped>
.chat-layout {
  display: flex;
  height:calc( 100vh - 85px);
  background: #f5f6fa;
}

.history-panel {
  width: 200px;
  background: #f7f7fa;
  border-right: 1px solid #e0e0e0;
  padding: 2rem 1rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.add-chat-btn {
  width: 100%;
  padding: 0.75em 0;
  margin-bottom: 1em;
  background: #007cf0;
  color: #fff;
  border: none;
  border-radius: 0.5em;
  font-size: 1em;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.add-chat-btn:hover {
  background: #005fa3;
}
.history-item.active {
  background: #e6f0fd;
  font-weight: bold;
}

.history-title {
  font-weight: bold;
  font-size: 1.1em;
  margin-bottom: 1.5rem;
}

.history-list {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1;
  overflow-y: auto;
}

.history-item {
  padding: 0.75em 0.5em;
  border-radius: 0.5em;
  cursor: pointer;
  transition: background 0.2s;
}
.history-item:hover {
  background: #e6f0fd;
}
.ai-chat-simple {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  background: #f5f6fa;
  box-sizing: border-box;
  padding: 30px;
  font-size: 1.05rem;
}
.chat-main {
  width: 100%;
  min-width:800px;
  margin: 0;
  background: #fff;
  display: flex;
  flex-direction: column;
  height: 90vh;
  min-height: 605px;
  overflow: hidden;
  border-radius:8px;
}
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 1rem;
  box-sizing: border-box;
}
.message {
  width: 100%;
  display: flex;
  margin-bottom: 1rem;
}
.message.user {
  justify-content: flex-end;
}
.message .content {
  padding-top:5px;
}
.message.user .content {
  background: #e6f0fd;
  color: #222;
  border-radius: 1.0em 0.2em 1.0em 1.25em;
  padding: 0.50em 1.2em;
  max-width: 90%;
  word-break: break-all;
  display: inline-block;
  text-align: left;
  margin-left: 3rem;
  margin-right: 0;
  font-size: 16px;
}
.message.user .role {
  color: #007CF0;
  font-weight: bold;
  margin-left: 0.5em;
  margin-right: 0;
}
.message.assistant {
  justify-content: flex-start;
}
.message.assistant .content {
  color: #222;
  border-radius: 0.2em 1.25em 1.25em 1.25em;
  padding: 0.50em 1.2em;
  max-width: 90%;
  word-break: break-all;
  display: inline-block;
  text-align: left;
  margin-right: 3rem;
  margin-left: 0;
  font-size: 16px;
}
.message.assistant .role {
  color: #ff9800;
  font-weight: bold;
  margin-right: 0.5em;
}
.input-area {
  display: flex;
  gap: 0.5rem;
  align-items: flex-end;
  background: #fafbfc;
  padding: 1.5rem 2rem;
  border-top: 1px solid #eee;
}
textarea {
  flex: 1;
  resize: none;
  border: none;
  background: transparent;
  padding: 1rem;
  font-size: 16px;
  line-height: 24px;
  max-height: 180px;
  border-radius: 0.75rem;
  outline: none;
  min-height: 1.5rem;
}
.send-button {
  min-width: 80px;
  height: 3rem;
  border: none;
  border-radius: 0.75rem;
  background: #007CF0;
  color: #fff;
  font-weight: bold;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.send-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.ai-logo {
  width: 32px;
  height: 32px;
  vertical-align: middle;
  margin-right: 0.5em;
  border-radius: 8px;
  background: #fff;
}
.content p {
font-size:16px;
}
.content table {
  border-collapse: collapse;
  width: 100%;
  margin: 1em 0;
}
.content th,
.content td {
  border: 1px solid #d0d7de;
  padding: 8px 12px;
  text-align: left;
}
.content th {
  background: #f6f8fa;
  font-weight: bold;
}
</style>
<style>
.content pre {
  background: #f6f8fa;
  padding: 1em;
  border-radius: 6px;
  overflow-x: auto;
}
.content code {
  font-family: 'Fira Mono', 'Consolas', 'Menlo', monospace;
  font-size: 1em;
}
</style>
