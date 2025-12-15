// Base URL of your backend API
const API_BASE = 'http://localhost:8080/api'; // adjust if needed

// Simple API call wrapper with optional Authorization token
const authManager = {
    getToken() {
        // Example: JWT stored in localStorage
        return localStorage.getItem('authToken');
    },

    async apiCall(endpoint, options = {}) {
        const defaultHeaders = {
            'Content-Type': 'application/json'
        };

        const token = this.getToken();
        if (token) {
            defaultHeaders['Authorization'] = `Bearer ${token}`;
        }

        const mergedHeaders = {
            ...defaultHeaders,
            ...(options.headers || {})
        };

        const fetchOptions = {
            ...options,
            headers: mergedHeaders
        };

        const res = await fetch(API_BASE + endpoint, fetchOptions);

        if (!res.ok) {
            const text = await res.text();
            throw new Error(`API error: ${res.status} - ${text}`);
        }

        return res.json();
    }
};

// Bills management class
class BillsManager {
    constructor() {
        this.userId = 1; // Hardcoded for testing. Replace with real userId after auth
    }

    // Get user's bills
    async getMyBills() {
        try {
            return await authManager.apiCall(`/bills/user/${this.userId}`);
        } catch (error) {
            console.error('Failed to fetch bills:', error);
            throw error;
        }
    }

    // Create new bill
    async createBill(billData) {
        try {
            const newBill = await authManager.apiCall('/bills', {
                method: 'POST',
                body: JSON.stringify(billData)
            });

            await this.loadBills();
            return newBill;
        } catch (error) {
            throw error;
        }
    }

    // Update bill status
    async updateBillStatus(billId, status) {
        try {
            return await authManager.apiCall(`/bills/${billId}/status?status=${status}`, {
                method: 'PUT'
            });
        } catch (error) {
            throw error;
        }
    }

    // Render bills list
    renderBillsList(bills) {
        if (!bills || bills.length === 0) {
            return '<div class="text-center text-muted p-4">No bills found.</div>';
        }

        return bills.map(bill => `
            <div class="bill-item ${bill.status.toLowerCase()}">
                <div class="d-flex justify-content-between align-items-start">
