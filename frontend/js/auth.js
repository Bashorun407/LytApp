// Authentication functions
const API_BASE_URL = 'http://localhost:8080/api';

class AuthManager {
    constructor() {
        this.token = localStorage.getItem('authToken');
        this.currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    }

    // Save token and user data
    setAuth(token, user) {
        this.token = token;
        this.currentUser = user;
        localStorage.setItem('authToken', token);
        localStorage.setItem('currentUser', JSON.stringify(user));
    }

    // Clear authentication
    clearAuth() {
        this.token = null;
        this.currentUser = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('currentUser');
    }

    // Check if user is authenticated
    isAuthenticated() {
        return this.token !== null && this.currentUser !== null;
    }

    // Get headers for API calls
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        return headers;
    }

    // Make authenticated API call
    async apiCall(url, options = {}) {
        const defaultOptions = {
            headers: this.getHeaders()
        };

        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                ...defaultOptions,
                ...options
            });

            if (response.status === 401) {
                this.clearAuth();
                window.location.reload();
                throw new Error('Authentication required');
            }

            return await response.json();
        } catch (error) {
            console.error('API call failed:', error);
            throw error;
        }
    }

    // Login function
    async login(email, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (response.ok) {
                this.setAuth(data.token, {
                    email: data.email,
                    firstName: data.firstName,
                    lastName: data.lastName,
                    role: data.role
                });
                return { success: true, data };
            } else {
                return { success: false, error: data };
            }
        } catch (error) {
            return { success: false, error: error.message };
        }
    }

    // Register function
    async register(userData) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (response.ok) {
                this.setAuth(data.token, {
                    email: data.email,
                    firstName: data.firstName,
                    lastName: data.lastName,
                    role: data.role
                });
                return { success: true, data };
            } else {
                return { success: false, error: data };
            }
        } catch (error) {
            return { success: false, error: error.message };
        }
    }

    // Get current user
    getCurrentUser() {
        return this.currentUser;
    }
}

// Create global auth instance
const authManager = new AuthManager();