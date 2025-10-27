// API Base URL - Will be set based on environment
const API_BASE_URL = '/api';

// Show content after short delay
setTimeout(() => {
    document.getElementById('loading').style.display = 'none';
    document.getElementById('content').style.display = 'block';
    checkServices();
}, 2000);

async function checkServices() {
    const statusElement = document.getElementById('status');

    try {
        statusElement.innerHTML = 'Checking backend service...';

        // Test backend connection
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            statusElement.innerHTML = '<span class="text-success">✅ All services are running!</span>';
        } else {
            statusElement.innerHTML = '<span class="text-warning">⚠️ Backend is starting up...</span>';
        }
    } catch (error) {
        statusElement.innerHTML = '<span class="text-danger">❌ Backend service not available yet. Please wait...</span>';
    }
}