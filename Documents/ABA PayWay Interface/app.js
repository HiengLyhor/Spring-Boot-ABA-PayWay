// 1. Fetch QR Code from Backend
async function generateQRCode() {
    const amount = 10.00; // Example amount
    const ccy = "USD";    // Example currency
    const response = await fetch(`http://localhost:8080/v1/aba/generate-qr-image?amount=${amount}&ccy=${ccy}`);

    if (response.ok) {
        const qrBlob = await response.blob();
        document.getElementById('qrImage').src = URL.createObjectURL(qrBlob);
        connectWebSocket(); // Connect to WebSocket after QR is loaded
    } else {
        alert("Failed to generate QR code");
    }
}

// 2. Connect to WebSocket
function connectWebSocket() {
    const socket = new SockJS('http://localhost:8080/ws-payment'); // Match your WebSocket endpoint
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("WebSocket connected!");

        // Subscribe to payment status updates
        stompClient.subscribe('/topic/payment-status', (message) => {
            const response = JSON.parse(message.body);
            if (response.status === 'SUCCESS') {
                document.getElementById('status-message').textContent = "Payment succeeded! Redirecting...";
                document.getElementById('status-message').style.color = "green";

                // Notify for 3 seconds before redirecting
                setTimeout(function () {
                    window.location.href = "./success-page.html"; // Redirect to success page
                }, 1500); // 3000 milliseconds = 3 seconds
            }
            document.getElementById('status-message').textContent = "Payment failed!";
            document.getElementById('status-message').style.color = "red";
            console.debug("Payment error reason:", response.status);
        });
    }, (error) => {
        console.error("WebSocket error:", error);
        document.getElementById('status-message').textContent = "Connection failed. Retrying...";
        setTimeout(connectWebSocket, 5000); // Retry after 5 seconds
    });
}

// This assume will be removed in production
function assumeCallback() {
    const url = 'http://localhost:8080/v1/aba/callback';

    const data = {
        tran_id: "123456789",
        apv: 123456,
        status: "00",
        merchant_ref_no: "ref0001"
    };

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response;
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

// Initialize
generateQRCode();