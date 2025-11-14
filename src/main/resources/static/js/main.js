document.addEventListener('DOMContentLoaded', function() {
    loadWorkers();
    initializeAnimations();
    initializeServiceCategories();
});

function initializeAnimations() {
    // Add smooth scroll behavior
    document.documentElement.style.scrollBehavior = 'smooth';
    
    // Add intersection observer for fade-in animations
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observe feature boxes, worker cards, and service categories
    document.querySelectorAll('.feature-box, .worker-card, .service-category, .stat-card').forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(el);
    });
}

function scrollToServices() {
    document.getElementById('services').scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });
}

function scrollToWorkers() {
    document.getElementById('workers').scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });
}

function initializeServiceCategories() {
    // Add hover effects to service categories
    document.querySelectorAll('.service-category').forEach(category => {
        category.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px) scale(1.02)';
        });
        
        category.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });
}

function bookService(profession) {
    // Check if user is logged in
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.userId) {
        showLoginPrompt();
        return;
    }
    
    // Redirect to booking page with profession filter
    window.location.href = `booking-page.html?profession=${profession}`;
}

function showLoginPrompt() {
    const modal = document.createElement('div');
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.8);
        backdrop-filter: blur(10px);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
        opacity: 0;
        transition: opacity 0.3s ease;
    `;
    
    modal.innerHTML = `
        <div style="
            background: white;
            padding: 3rem;
            border-radius: 16px;
            text-align: center;
            max-width: 400px;
            transform: translateY(20px);
            transition: transform 0.3s ease;
        ">
            <div style="font-size: 3rem; margin-bottom: 1rem;">🔐</div>
            <h3 style="color: #8B7355; margin-bottom: 1rem; font-size: 1.5rem;">Login Required</h3>
            <p style="color: #6B7280; margin-bottom: 2rem;">Please login to book services with our verified professionals.</p>
            <div style="display: flex; gap: 1rem;">
                <button onclick="window.location.href='user-auth.html'" style="
                    flex: 1;
                    background: #8B7355;
                    color: white;
                    padding: 0.8rem 1.5rem;
                    border: none;
                    border-radius: 8px;
                    cursor: pointer;
                    font-weight: 500;
                ">Login</button>
                <button onclick="this.closest('div').parentElement.remove()" style="
                    flex: 1;
                    background: #6B7280;
                    color: white;
                    padding: 0.8rem 1.5rem;
                    border: none;
                    border-radius: 8px;
                    cursor: pointer;
                    font-weight: 500;
                ">Cancel</button>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    setTimeout(() => {
        modal.style.opacity = '1';
        modal.querySelector('div').style.transform = 'translateY(0)';
    }, 10);
}

async function loadWorkers(district = '', profession = '') {
    try {
        let url = `${API_BASE_URL}/workers`;
        const params = new URLSearchParams();
        if (district) params.append('district', district);
        if (profession) params.append('profession', profession);
        if (params.toString()) url += '?' + params.toString();
        
        const response = await fetch(url);
        const workers = await response.json();
        displayWorkers(workers);
    } catch (error) {
        console.error('Error loading workers:', error);
        document.getElementById('workers-grid').innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: #6B7280;">
                <div style="font-size: 3rem; margin-bottom: 1rem;">⚠️</div>
                <h3 style="color: #8B7355; margin-bottom: 1rem;">Connection Error</h3>
                <p>Unable to load workers at the moment.</p>
                <p>Please check your connection and try again.</p>
                <button onclick="loadWorkers()" style="
                    background: #8B7355;
                    color: white;
                    padding: 0.8rem 1.5rem;
                    border: none;
                    border-radius: 8px;
                    cursor: pointer;
                    margin-top: 1rem;
                    font-weight: 500;
                ">Retry</button>
            </div>
        `;
    }
}

function filterWorkers() {
    const district = document.getElementById('district').value;
    const profession = document.getElementById('profession') ? document.getElementById('profession').value : '';
    loadWorkers(district, profession);
}

function displayWorkers(workers) {
    const grid = document.getElementById('workers-grid');
    
    if (workers.length === 0) {
        grid.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: #6B7280;">
                <h3 style="color: #8B7355; margin-bottom: 1rem;">No Workers Available</h3>
                <p>No service providers found in the selected area.</p>
                <p>Try selecting a different district or check back later.</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = workers.map(worker => `
        <div class="worker-card">
            <div class="worker-avatar" style="width: 80px; height: 80px; border-radius: 50%; background: linear-gradient(45deg, #8B7355, #6B7280); margin: 0 auto 1rem; display: flex; align-items: center; justify-content: center; color: white; font-size: 1.5rem; font-weight: bold;">
                ${worker.fullName.charAt(0)}
            </div>
            <h3>${worker.fullName}</h3>
            <p class="profession">${worker.profession}</p>
            <p class="location">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#6B7280" stroke-width="2" style="display: inline; margin-right: 4px;">
                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/>
                    <circle cx="12" cy="10" r="3"/>
                </svg>
                ${worker.district}
            </p>
            <p class="rate">₹${worker.hourlyRate}/hour</p>
            <p class="rating">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#F59E0B" stroke-width="2" style="display: inline; margin-right: 4px;">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                </svg>
                ${worker.rating}/5 (${Math.floor(Math.random() * 50) + 10} reviews)
            </p>
            <div class="worker-badges">
                <span class="worker-badge verified">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline; margin-right: 4px;">
                        <path d="M9 12l2 2 4-4"/>
                        <circle cx="12" cy="12" r="10"/>
                    </svg>
                    Verified
                </span>
                ${worker.rating >= 4.5 ? '<span class="worker-badge top-rated"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline; margin-right: 4px;"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>Top Rated</span>' : ''}
            </div>
            <button onclick="bookWorker(${worker.id}, '${worker.fullName}', '${worker.profession}')" class="book-btn">Book Now</button>
        </div>
    `).join('');
    
    // Re-initialize animations for new cards
    setTimeout(() => {
        document.querySelectorAll('.worker-card').forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            setTimeout(() => {
                card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, index * 100);
        });
    }, 50);
}

// Smooth scrolling for navigation links
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

function bookWorker(workerId, workerName, profession) {
    // Check if user is logged in
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.userId) {
        showLoginPrompt();
        return;
    }
    
    // Redirect to booking page with worker details
    const params = new URLSearchParams({
        workerId: workerId,
        workerName: workerName,
        profession: profession
    });
    window.location.href = `booking-page.html?${params.toString()}`;
}