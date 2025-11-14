// Success Animation Utilities
class SuccessAnimations {
    
    // Show success notification toast
    static showSuccessToast(message, type = 'success') {
        const notification = document.createElement('div');
        notification.className = `success-notification ${type === 'payment' ? 'payment-success' : ''}`;
        
        notification.innerHTML = `
            <div class="icon">✓</div>
            <div class="message">${message}</div>
            <button class="close-btn" onclick="this.parentElement.remove()">×</button>
        `;
        
        document.body.appendChild(notification);
        
        // Auto remove after 4 seconds
        setTimeout(() => {
            if (notification.parentElement) {
                notification.classList.add('hide');
                setTimeout(() => {
                    if (notification.parentElement) {
                        notification.remove();
                    }
                }, 300);
            }
        }, 4000);
        
        return notification;
    }
    
    // Show full screen success overlay
    static showSuccessOverlay(title, message, duration = 2000) {
        const overlay = document.createElement('div');
        overlay.className = 'success-overlay';
        
        overlay.innerHTML = `
            <div class="success-content">
                <div class="success-icon">✓</div>
                <h2>${title}</h2>
                <p>${message}</p>
            </div>
        `;
        
        document.body.appendChild(overlay);
        
        // Auto remove after specified duration
        setTimeout(() => {
            if (overlay.parentElement) {
                overlay.classList.add('hide');
                setTimeout(() => {
                    if (overlay.parentElement) {
                        overlay.remove();
                    }
                }, 300);
            }
        }, duration);
        
        return overlay;
    }
    
    // Add loading animation to button
    static addLoadingToButton(button, originalText) {
        button.classList.add('loading-btn');
        button.textContent = originalText || button.textContent;
        button.disabled = true;
    }
    
    // Remove loading animation from button
    static removeLoadingFromButton(button, originalText) {
        button.classList.remove('loading-btn');
        button.textContent = originalText || button.textContent;
        button.disabled = false;
    }
    
    // Account creation success
    static accountCreated(userType = 'user') {
        this.showSuccessOverlay(
            'Account Created Successfully!',
            `Welcome to Workline! Your ${userType} account has been created.`,
            2500
        );
        
        setTimeout(() => {
            this.showSuccessToast(`🎉 Welcome to Workline! Your ${userType} account is ready.`);
        }, 2800);
    }
    
    // Login success
    static loginSuccess(userName) {
        this.showSuccessToast(`👋 Welcome back, ${userName}! Login successful.`);
    }
    
    // Payment completed
    static paymentCompleted(amount, method) {
        this.showSuccessOverlay(
            'Payment Successful!',
            `₹${amount} payment completed via ${method}`,
            2000
        );
        
        setTimeout(() => {
            this.showSuccessToast(`💳 Payment of ₹${amount} completed successfully!`, 'payment');
        }, 2300);
    }
    
    // Booking confirmed
    static bookingConfirmed(workerName, service) {
        this.showSuccessOverlay(
            'Booking Confirmed!',
            `Your ${service} booking with ${workerName} has been confirmed.`,
            2500
        );
        
        setTimeout(() => {
            this.showSuccessToast(`📅 Booking confirmed with ${workerName} for ${service}!`);
        }, 2800);
    }
    
    // Profile updated
    static profileUpdated() {
        this.showSuccessToast('✏️ Profile updated successfully!');
    }
    
    // Worker setup completed
    static workerSetupCompleted() {
        this.showSuccessOverlay(
            'Setup Complete!',
            'Your worker profile is now active and ready to receive bookings.',
            3000
        );
        
        setTimeout(() => {
            this.showSuccessToast('🔧 Worker profile setup completed! You can now receive bookings.');
        }, 3300);
    }
    
    // Booking status updated
    static bookingStatusUpdated(status) {
        const statusMessages = {
            'CONFIRMED': '✅ Booking confirmed successfully!',
            'COMPLETED': '🎉 Service completed! Thank you for using Workline.',
            'CANCELLED': '❌ Booking cancelled successfully.',
            'PENDING': '⏳ Booking status updated to pending.'
        };
        
        this.showSuccessToast(statusMessages[status] || `Booking status updated to ${status.toLowerCase()}.`);
    }
    
    // Review submitted
    static reviewSubmitted() {
        this.showSuccessToast('⭐ Thank you for your review! It helps improve our service.');
    }
    
    // Generic success message
    static showSuccess(message, useOverlay = false, title = 'Success!') {
        if (useOverlay) {
            this.showSuccessOverlay(title, message);
        } else {
            this.showSuccessToast(message);
        }
    }
}

// Make it globally available
window.SuccessAnimations = SuccessAnimations;