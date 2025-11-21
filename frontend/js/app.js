// Main application logic
class LightPayApp {
    constructor() {
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.checkAuthStatus();
    }

    setupEventListeners() {
        // Tab switching
        document.getElementById('loginTab').addEventListener('click', (e) => {
            e.preventDefault();
            this.showLoginForm();
        });

        document.getElementById('registerTab').addEventListener('click', (e) => {
            e.preventDefault();
            this.showRegisterForm();
        });

        // Form submissions
        document.getElementById('loginFormElement').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleLogin();
        });

        document.getElementById('registerFormElement').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleRegister();
        });

        // Logout
        document.getElementById('logoutBtn').addEventListener('click', () => {
            this.handleLogout();
        });

        // Login button
        document.getElementById('loginBtn').addEventListener('click', () => {
            this.showAuthSection();
        });

        // Dashboard navigation
        document.querySelectorAll('.btn-dashboard').forEach(button => {
            button.addEventListener('click', (e) => {
                this.showDashboardSection(e.target.getAttribute('data-section'));
            });
        });

        // Add bill button
        document.getElementById('addBillBtn').addEventListener('click', () => {
            this.showAddBillModal();
        });

        document.getElementById('saveBillBtn').addEventListener('click', () => {
            this.handleAddBill();
        });
    }

    checkAuthStatus() {
        if (authManager.isAuthenticated()) {
            this.showDashboard();
        } else {
            this.showAuthSection();
        }
    }

    showLoginForm() {
        document.getElementById('loginForm').style.display = 'block';
        document.getElementById('registerForm').style.display = 'none';
        document.getElementById('loginTab').classList.add('active');
        document.getElementById('registerTab').classList.remove('active');
    }

    showRegisterForm() {
        document.getElementById('loginForm').style.display = 'none';
        document.getElementById('registerForm').style.display = 'block';
        document.getElementById('loginTab').classList.remove('active');
        document.getElementById('registerTab').classList.add('active');
    }

    showAuthSection() {
        document.getElementById('authSection').style.display = 'block';
        document.getElementById('dashboardSection').style.display = 'none';
        document.getElementById('loginBtn').style.display = 'inline-block';
        document.getElementById('logoutBtn').style.display = 'none';
        document.getElementById('userWelcome').style.display = 'none';
    }

    async showDashboard() {
        document.getElementById('authSection').style.display = 'none';
        document.getElementById('dashboardSection').style.display = 'block';
        document.getElementById('loginBtn').style.display = 'none';
        document.getElementById('logoutBtn').style.display = 'inline-block';

        const user = authManager.getCurrentUser();
        document.getElementById('userWelcome').textContent = `Welcome, ${user.firstName}!`;
        document.getElementById('userWelcome').style.display = 'inline-block';

        // Load initial data
        await this.loadDashboardData();
        this.showDashboardSection('overview');
    }

    async loadDashboardData() {
        try {
            // Load bills and update counts
            const bills = await billsManager.getMyBills();
            const paidBills = bills.filter(bill => bill.status === 'PAID');
            const unpaidBills = bills.filter(bill => bill.status === 'UNPAID');
            const totalSpent = paidBills.reduce((sum, bill) => sum + parseFloat(bill.amount), 0);

            document.getElementById('pendingBillsCount').textContent = unpaidBills.length;
            document.getElementById('paidBillsCount').textContent = paidBills.length;
            document.getElementById('totalSpent').textContent = `$${totalSpent.toFixed(2)}`;

        } catch (error) {
            console.error('Failed to load dashboard data:', error);
        }
    }

    showDashboardSection(section) {
        // Hide all sections
        document.querySelectorAll('.dashboard-content').forEach(el => {
            el.style.display = 'none';
        });

        // Remove active class from all buttons
        document.querySelectorAll('.btn-dashboard').forEach(btn => {
            btn.classList.remove('active');
        });

        // Show selected section
        document.getElementById(section + 'Section').style.display = 'block';

        // Add active class to clicked button
        document.querySelector(`.btn-dashboard[data-section="${section}"]`).classList.add('active');

        // Load section-specific data
        switch (section) {
            case 'bills':
                billsManager.loadBills();
                break;
            case 'payments':
                paymentsManager.loadPayments();
                break;
            case 'profile':
                this.loadProfile();
                break;
        }
    }

    loadProfile() {
        const user = authManager.getCurrentUser();
        document.getElementById('profileInfo').innerHTML = `
            <div class="row">
                <div class="col-md-6">
                    <p><strong>First Name:</strong> ${user.firstName}</p>
                    <p><strong>Last Name:</strong> ${user.lastName}</p>
                    <p><strong>Email:</strong> ${user.email}</p>
                    <p><strong>Role:</strong> ${user.role}</p>
                </div>
            </div>
        `;
    }

    async handleLogin() {
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        const result = await authManager.login(email, password);

        if (result.success) {
            await this.showDashboard();
        } else {
            alert('Login failed: ' + result.error);
        }
    }

    async handleRegister() {
        const userData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('registerEmail').value,
            password: document.getElementById('registerPassword').value
        };

        const confirmPassword = document.getElementById('confirmPassword').value;

        if (userData.password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        const result = await authManager.register(userData);

        if (result.success) {
            await this.showDashboard();
        } else {
            alert('Registration failed: ' + result.error);
        }
    }

    handleLogout() {
        authManager.clearAuth();
        this.showAuthSection();

        // Clear forms
        document.getElementById('loginFormElement').reset();
        document.getElementById('registerFormElement').reset();
    }

    showAddBillModal() {
        // Set due date to tomorrow by default
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        document.getElementById('billDueDate').value = tomorrow.toISOString().split('T')[0];

        // Show modal
        new bootstrap.Modal(document.getElementById('addBillModal')).show();
    }

    async handleAddBill() {
        const billData = {
            amount: parseFloat(document.getElementById('billAmount').value),
            dueDate: document.getElementById('billDueDate').value,
            status: 'UNPAID'
        };

        try {
            await billsManager.createBill(billData);

            // Close modal and reset form
            bootstrap.Modal.getInstance(document.getElementById('addBillModal')).hide();
            document.getElementById('addBillForm').reset();

            // Reload bills and dashboard
            await billsManager.loadBills();
            await this.loadDashboardData();

            alert('Bill added successfully!');
        } catch (error) {
            alert('Failed to add bill: ' + error.message);
        }
    }
}

// Initialize the application when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new LightPayApp();
});