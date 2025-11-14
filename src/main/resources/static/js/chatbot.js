class Chatbot {
    constructor() {
        this.isOpen = false;
        this.messages = [];
        this.responses = {
            greeting: ["Hello! How can I help you today?", "Hi there! What can I assist you with?"],
            booking: ["To book a service, select a service category and choose your preferred professional.", "You can book services by browsing our verified professionals and clicking 'Book Now'."],
            payment: ["We accept UPI, credit/debit cards, and cash on service completion.", "Payment is secure and processed after service confirmation."],
            workers: ["All our workers are background-verified and skill-tested.", "We have 500+ verified professionals across Kerala."],
            support: ["For urgent issues, call +91 9876543210 or email support@workline.com", "Our support team is available 24/7 to assist you."],
            default: ["I'm here to help! You can ask about booking services, payments, or our workers.", "Feel free to ask about our services, safety measures, or how to get started."]
        };
        this.init();
    }

    init() {
        this.createChatbot();
        this.addEventListeners();
    }

    createChatbot() {
        const chatbotHTML = `
            <div id="chatbot-container">
                <div id="chatbot-button">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                        <path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/>
                    </svg>
                </div>
                <div id="chatbot-window" class="chatbot-hidden">
                    <div id="chatbot-header">
                        <div class="chatbot-title">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                                <circle cx="12" cy="12" r="3"/>
                                <path d="M12 1v6m0 6v6"/>
                                <path d="M1 12h6m6 0h6"/>
                            </svg>
                            Workline Assistant
                        </div>
                        <button id="chatbot-close">×</button>
                    </div>
                    <div id="chatbot-messages"></div>
                    <div id="chatbot-input-area">
                        <input type="text" id="chatbot-input" placeholder="Type your message...">
                        <button id="chatbot-send">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="22" y1="2" x2="11" y2="13"/>
                                <polygon points="22,2 15,22 11,13 2,9 22,2"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', chatbotHTML);
        this.addWelcomeMessage();
    }

    addEventListeners() {
        document.getElementById('chatbot-button').addEventListener('click', () => this.toggleChat());
        document.getElementById('chatbot-close').addEventListener('click', () => this.closeChat());
        document.getElementById('chatbot-send').addEventListener('click', () => this.sendMessage());
        document.getElementById('chatbot-input').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.sendMessage();
        });
    }

    toggleChat() {
        const window = document.getElementById('chatbot-window');
        const button = document.getElementById('chatbot-button');
        
        if (this.isOpen) {
            this.closeChat();
        } else {
            window.classList.remove('chatbot-hidden');
            button.style.display = 'none';
            this.isOpen = true;
        }
    }

    closeChat() {
        const window = document.getElementById('chatbot-window');
        const button = document.getElementById('chatbot-button');
        
        window.classList.add('chatbot-hidden');
        button.style.display = 'flex';
        this.isOpen = false;
    }

    addWelcomeMessage() {
        setTimeout(() => {
            this.addMessage('bot', 'Hello! I\'m your Workline assistant. How can I help you today?');
            this.addQuickReplies(['Book a Service', 'Payment Info', 'About Workers', 'Contact Support']);
        }, 1000);
    }

    addMessage(sender, text) {
        const messagesContainer = document.getElementById('chatbot-messages');
        const messageDiv = document.createElement('div');
        messageDiv.className = `chatbot-message ${sender}`;
        messageDiv.innerHTML = `<div class="message-text">${text}</div>`;
        messagesContainer.appendChild(messageDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    addQuickReplies(replies) {
        const messagesContainer = document.getElementById('chatbot-messages');
        const quickRepliesDiv = document.createElement('div');
        quickRepliesDiv.className = 'quick-replies';
        
        replies.forEach(reply => {
            const button = document.createElement('button');
            button.className = 'quick-reply-btn';
            button.textContent = reply;
            button.addEventListener('click', () => {
                this.handleQuickReply(reply);
                quickRepliesDiv.remove();
            });
            quickRepliesDiv.appendChild(button);
        });
        
        messagesContainer.appendChild(quickRepliesDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    handleQuickReply(reply) {
        this.addMessage('user', reply);
        
        setTimeout(() => {
            let response;
            if (reply.includes('Book')) {
                response = this.responses.booking[Math.floor(Math.random() * this.responses.booking.length)];
            } else if (reply.includes('Payment')) {
                response = this.responses.payment[Math.floor(Math.random() * this.responses.payment.length)];
            } else if (reply.includes('Workers')) {
                response = this.responses.workers[Math.floor(Math.random() * this.responses.workers.length)];
            } else if (reply.includes('Support')) {
                response = this.responses.support[Math.floor(Math.random() * this.responses.support.length)];
            } else {
                response = this.responses.default[Math.floor(Math.random() * this.responses.default.length)];
            }
            
            this.addMessage('bot', response);
            
            if (reply.includes('Book')) {
                this.addQuickReplies(['View Services', 'Find Workers', 'How to Book']);
            } else {
                this.addQuickReplies(['Book a Service', 'Payment Info', 'Contact Support']);
            }
        }, 800);
    }

    sendMessage() {
        const input = document.getElementById('chatbot-input');
        const message = input.value.trim();
        
        if (!message) return;
        
        this.addMessage('user', message);
        input.value = '';
        
        setTimeout(() => {
            const response = this.generateResponse(message);
            this.addMessage('bot', response);
        }, 800);
    }

    generateResponse(message) {
        const msg = message.toLowerCase();
        
        if (msg.includes('hello') || msg.includes('hi') || msg.includes('hey')) {
            return this.responses.greeting[Math.floor(Math.random() * this.responses.greeting.length)];
        } else if (msg.includes('book') || msg.includes('service') || msg.includes('appointment')) {
            return this.responses.booking[Math.floor(Math.random() * this.responses.booking.length)];
        } else if (msg.includes('pay') || msg.includes('payment') || msg.includes('cost') || msg.includes('price')) {
            return this.responses.payment[Math.floor(Math.random() * this.responses.payment.length)];
        } else if (msg.includes('worker') || msg.includes('professional') || msg.includes('verified')) {
            return this.responses.workers[Math.floor(Math.random() * this.responses.workers.length)];
        } else if (msg.includes('help') || msg.includes('support') || msg.includes('contact')) {
            return this.responses.support[Math.floor(Math.random() * this.responses.support.length)];
        } else {
            return this.responses.default[Math.floor(Math.random() * this.responses.default.length)];
        }
    }
}

// Initialize chatbot when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new Chatbot();
});