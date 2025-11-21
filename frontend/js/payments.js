// Payments management functions
class PaymentsManager {
    // Get user's payments
    async getMyPayments() {
        try {
            return await authManager.apiCall('/payments/my-payments');
        } catch (error) {
            console.error('Failed to fetch payments:', error);
            throw error;
        }
    }

    // Process payment
    async processPayment(paymentData) {
        try {
            return await authManager.apiCall('/payments/process', {
                method: 'POST',
                body: JSON.stringify(paymentData)
            });
        } catch (error) {
            console.error('Failed to process payment:', error);
            throw error;
        }
    }

    // Render payments list
    renderPaymentsList(payments) {
        if (!payments || payments.length === 0) {
            return '<div class="text-center text-muted p-4">No payment history found.</div>';
        }

        return payments.map(payment => `
            <div class="payment-item">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6>Transaction: ${payment.transactionId}</h6>
                        <p class="mb-1">Amount: $${payment.amountPaid}</p>
                        <p class="mb-1">Method: ${payment.paymentMethod}</p>
                        <p class="mb-1">Date: ${new Date(payment.paidAt).toLocaleDateString()}</p>
                        <span class="badge status-badge bg-success">
                            ${payment.status}
                        </span>
                    </div>
                </div>
            </div>
        `).join('');
    }

    // Load and display payments
    async loadPayments() {
        try {
            const paymentsList = document.getElementById('paymentsList');
            paymentsList.innerHTML = '<div class="text-center"><span class="loading-spinner"></span> Loading payments...</div>';

            const payments = await this.getMyPayments();
            paymentsList.innerHTML = this.renderPaymentsList(payments);
        } catch (error) {
            document.getElementById('paymentsList').innerHTML =
                '<div class="alert alert-danger">Failed to load payments: ' + error.message + '</div>';
        }
    }
}

// Create global payments instance
const paymentsManager = new PaymentsManager();