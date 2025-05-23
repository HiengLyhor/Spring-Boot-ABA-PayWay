const txnId = getCurrentDateTime(); // This txnId will provide each transaction request
console.debug("Transaction ID:", txnId);

async function generateQRCode() {

    event.preventDefault();
    
    var amount = document.getElementById('amount').value;
    var ccy = document.getElementById('currency').value;

    console.log("Amount:", amount);
    console.log("Currency:", ccy);
    if (!amount || !ccy) {
        return;
    }

    document.getElementById("qr-request").style.display = "none";
    document.getElementById("qr-view").style = null;

    await showQR(amount, ccy); // Pass the variables to showQR
}

async function showQR(amount, ccy) { // Accept the parameters

    const response = await fetch(`http://localhost:8080/v1/aba/generate-qr-image?amount=${amount}&ccy=${ccy}&txnId=${txnId}`);

    if (response.ok) {
        const qrBlob = await response.blob();
        document.getElementById('qrImage').src = URL.createObjectURL(qrBlob);
        connectWebSocket(); // Connect to WebSocket after QR is loaded
    } else {
        alert("Failed to generate QR code");
    }
}

function getCurrentDateTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const milliseconds = String(now.getMilliseconds()).padStart(3, '0');
    
    return `${year}${month}${day}${hours}${minutes}${seconds}${milliseconds}`;
}

// 2. Connect to WebSocket
function connectWebSocket() {
    const socket = new SockJS('http://localhost:8080/ws-payment');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("WebSocket connected!");

        // Subscribe to payment status updates
        stompClient.subscribe('/topic/payment-status', (message) => {

            const response = JSON.parse(message.body);

            if (response.transactionId !== txnId) {
                console.log("Ignoring message for different transaction ID:", response.transactionId);
                return; // Ignore messages for different transaction IDs
            }

            if (response.status === 'SUCCESS') {
                console.log("Payment succeeded!");
                document.getElementById('status-message').textContent = "Payment succeeded! Redirecting...";
                document.getElementById('status-message').style.color = "green";

                // Notify for 1.5 seconds before redirecting
                setTimeout(function () {
                    window.location.href = "./success-page.html"; // Redirect to success page
                }, 1500); // 1500 milliseconds = 1.5 seconds

            }else {
                document.getElementById('status-message').textContent = "Payment failed!";
                document.getElementById('status-message').style.color = "red";
                console.debug("Payment error reason:", response.status);
            }
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
        tran_id: txnId,
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
