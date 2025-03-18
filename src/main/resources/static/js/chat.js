class ChatApp {
    constructor() {
        this.sessionId = Date.now().toString();
        this.messagesContainer = document.getElementById('messages');
        this.chatContainer = document.getElementById('chat-container');
        this.chatForm = document.getElementById('chatForm');
        this.messageInput = document.getElementById('messageInput');
        this.typingIndicator = document.querySelector('.typing-indicator');
        this.themeToggle = document.getElementById('themeToggle');

        this.initializeEventListeners();
        this.initializeTheme();
        this.showWelcomeMessage();
    }

    initializeEventListeners() {
        // 主题切换按钮事件
        this.themeToggle.addEventListener('click', () => this.toggleTheme());

        // 自动调整文本框高度
        this.messageInput.addEventListener('input', () => this.adjustTextareaHeight());

        // Enter发送，Shift+Enter换行
        this.messageInput.addEventListener('keydown', (e) => this.handleKeyPress(e));

        // 表单提交
        this.chatForm.addEventListener('submit', (e) => this.handleSubmit(e));

        // 监听系统主题变化
        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
            if (!localStorage.getItem('theme')) {
                this.setTheme(e.matches ? 'dark' : 'light');
            }
        });
    }

    initializeTheme() {
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme) {
            this.setTheme(savedTheme);
        } else {
            this.detectSystemTheme();
        }
    }

    showWelcomeMessage() {
        this.addMessage('你好，我是报表公式解释和写作助手，你可以向我咨询某一个场景的公式如何写，或者解释某一个公式', false);
    }

    setTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    }

    toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        this.setTheme(newTheme);
    }

    detectSystemTheme() {
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            this.setTheme('dark');
        } else {
            this.setTheme('light');
        }
    }

    adjustTextareaHeight() {
        this.messageInput.style.height = 'auto';
        this.messageInput.style.height = Math.min(this.messageInput.scrollHeight, 200) + 'px';
    }

    handleKeyPress(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            this.chatForm.dispatchEvent(new Event('submit'));
        }
    }

    async handleSubmit(e) {
        e.preventDefault();
        const message = this.messageInput.value.trim();
        if (!message) return;

        // Add user message
        this.addMessage(message, true);
        this.messageInput.value = '';
        this.messageInput.style.height = '52px';

        // Show typing indicator
        this.showTypingIndicator();

        try {
            const response = await fetch('/api/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    message: message,
                    sessionId: this.sessionId
                })
            });

            // 检查响应状态
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            this.hideTypingIndicator();

            // 更全面的响应处理
            if (data.success && data.message) {
                this.addMessage(data.message, false);
            } else if (data.error) {
                this.addMessage(`错误：${data.error}`, false);
            } else {
                this.addMessage('服务器返回了意外的响应。', false);
            }
        } catch (error) {
            this.hideTypingIndicator();
            console.error('Request error:', error);
            this.addMessage('网络错误，请检查您的连接并重试。', false);
        }
    }

    addMessage(content, isUser) {
        const messageRow = document.createElement('div');
        messageRow.className = `message-row ${isUser ? 'user' : 'assistant'}`;

        const messageDiv = document.createElement('div');
        messageDiv.className = 'message';

        const avatar = document.createElement('div');
        avatar.className = 'avatar';

        const messageContent = document.createElement('div');
        messageContent.className = 'message-content';
        messageContent.textContent = content;

        messageDiv.appendChild(avatar);
        messageDiv.appendChild(messageContent);
        messageRow.appendChild(messageDiv);
        this.messagesContainer.appendChild(messageRow);

        this.chatContainer.scrollTop = this.chatContainer.scrollHeight;
    }

    showTypingIndicator() {
        this.typingIndicator.style.display = 'block';
        this.chatContainer.scrollTop = this.chatContainer.scrollHeight;
    }

    hideTypingIndicator() {
        this.typingIndicator.style.display = 'none';
    }
}

// 初始化应用
document.addEventListener('DOMContentLoaded', () => {
    new ChatApp();
}); 