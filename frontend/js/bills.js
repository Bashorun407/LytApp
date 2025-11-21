// Bills management functions
class BillsManager {
    // Get user's bills
    async getMyBills() {
        try {
            return await authManager.apiCall('/bills/my-bills');
        } catch (error) {
            console.error('Failed to fetch bills:', error);
            throw error;
        }
    }

    // Create new bill
    async createBill(billData) {
        try {
            return await authManager.apiCall('/bills', {
                method: 'POST',
                body: JSON.stringify(billData)
            });
        } catch (error) {
            console.error('Failed to create bill:', error);
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
            console.error('Failed to update bill:', error);
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
                    <div>
                        <h6>Bill #${bill.id}</h6>
                        <p class="mb-1">Amount: $${bill.amount}</p>
                        <p class="mb-1">Due: ${new Date(bill.dueDate).toLocaleDateString()}</p>
                        <span class="badge status-badge bg-${this.getStatusColor(bill.status)}">
                            ${bill.status}
                        </span>
                    </div>
                    <div>
                        ${bill.status === 'UNPAID' ? `
                            <button class="btn btn-success btn-sm pay-bill-btn" data-bill-id="${bill.id}">
                                Pay Now
                            </button>
                        ` : ''}
                    </div>
                </div>
            </div>
        `).join('');
    }

    getStatusColor(status) {
        switch (status) {
            case 'PAID': return 'success';
            case 'UNPAID': return 'warning';
            case 'OVERDUE': return 'danger';
            default: return 'secondary';
        }
    }

    // Load and display bills
    async loadBills() {
        try {
            const billsList = document.getElementById('billsList');
            billsList.innerHTML = '<div class="text-center"><span class="loading-spinner"></span> Loading bills...</div>';

            const bills = await this.getMyBills();
            billsList.innerHTML = this.renderBillsList(bills);

            // Add event listeners to pay buttons
            document.querySelectorAll('.pay-bill-btn').forEach(button => {
                button.addEventListener('click', (e) => {
                    const billId = e.target.getAttribute('data-bill-id');
                    this.payBill(billId);
                });
            });
        } catch (error) {
            document.getElementById('billsList').innerHTML =
                '<div class="alert alert-danger">Failed to load bills: ' + error.message + '</div>';
        }
    }

    // Pay bill function
    async payBill(billId) {
        try {
            // In a real app, you'd integrate with a payment gateway
            // For now, we'll just mark the bill as paid
            await this.updateBillStatus(billId, 'PAID');

            // Reload bills
            await this.loadBills();

            // Show success message
            alert('Payment processed successfully!');
        } catch (error) {
            alert('Payment failed: ' + error.message);
        }
    }
}

// Create global bills instance
const billsManager = new BillsManager();