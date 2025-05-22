# ABA PayWay Integration API

A Java Spring Boot application that provides integration with ABA PayWay payment system, including QR code generation and payment callback handling.

## Features

- User authentication (JWT)
- Generate ABA PayWay QR codes for payments
- Handle payment callbacks from ABA PayWay
- RESTful API endpoints

## API Endpoints

### Authentication

- **POST** `/v1/user/login`
  - Authenticate user and get JWT token
  - Request body:
    ```json
    {
        "username": "Admin001",
        "password": "1234!@#$!QAZ1qaz"
    }
    ```
  - Response:
    ```json
    {
    "code": 200,
    "message": "Login successfully.",
    "username": "Admin001",
    "createDate": "2025-05-05T08:46:47.841+00:00",
    "expDate": "2025-06-04T08:46:47.829+00:00",
    "active": true,
    "token": "eyJhbGciOiJIUzI1N...55UV2I",
    "tokenExp": "2025-05-22T08:53:06.766+00:00"
    }
    ```

### ABA PayWay Integration

- **GET** `/v1/aba/generate-qr-image`
  - Generate QR code for payment
  - Required query parameters:
    - `amount`: Transaction amount (e.g., 10.0)
    - `ccy`: Currency code (e.g., USD)
  - No authentication required (Can be configure in <code>SecurityConfig.java</code>)
  - Returns QR code image

- **POST** `/v1/aba/callback`
  - Handle payment callback from ABA PayWay
  - Request body:
    ```json
    {
        "tran_id": "123456789",
        "apv": 123456,
        "status": "00",
        "merchant_ref_no": "ref0001"
    }
    ```
  - No authentication required (called by ABA PayWay system)
 
 ### WebSocket with Front-End
 
  - Java application has the WebSocket open at:  `http://localhost:8080/ws-payment`
  - The WebSocket listener is provided to <code>Documents/ABA PayWay Interface/app.js</code>
  - No authentication required 

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL (or your preferred database)
- ABA PayWay merchant account credentials

## Installation & Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/HiengLyhor/Spring-Boot-ABA-PayWay.git

2. Configure the application:
    - Modify the file <code>application.properties</code>

    - Update database connection details

    - Configure ABA PayWay credentials:

3. Build the application:

    ```bash
    mvn clean install

4. Run the application:

    ```bash
    java -jar target/your-application-name.jar

## Security
- JWT authentication for API endpoints

- HTTPS recommended for production

- Sensitive credentials stored in environment variables

## Deployment
The application can be deployed as a standard Spring Boot application:

- As a standalone JAR

- In a Docker container

- On any Java application server

## Environment Variables
| Variable          | Description                           | Required |
|-------------------|---------------------------------------|----------|
| spring.datasource.url            | Database connection URL               | Yes      |
| spring.datasource.username       | Database username                     | Yes      |
| spring.datasource.password       | Database password                     | Yes      |
| aba.api      | ABA PayWay API key                   | Yes      |
| aba.merchant   | ABA PayWay merchant ID                | Yes      |
| aba.public.key   | ABA PayWay public key provided by ABA                | Yes      |
| aba.callback   | Callback URL for PayWay once the transaction success                | Yes      |
| jwt.secret        | Secret for JWT token generation       | Yes      |

## Postman Collection
A Postman collection is available for testing the API endpoints. Import the provided JSON file into Postman to get started with API testing.

## Support
For support or questions, please contact:
- E-Mail: hienglyhor@gmail.com
- Telegram: [@Lyhor_Hieng](https://t.me/Lyhor_Hieng)

