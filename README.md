# 🔗 URL Shortener Backend  
### Scalable URL Shortener API (Spring Boot + Redis + JWT)

A **production-ready backend system** that converts long URLs into short links with high performance, caching, authentication, and analytics support.

---

## 🌐 Live API
👉 http://3.106.218.248:8080/  

⚠️ Backend-only service — test via Postman or Swagger

---

## ⚡ Quick Test (2 Minutes)

### 🔐 1. Login (Get JWT Token)
POST /auth/login

{
  "email": "test@example.com",
  "password": "123456"
}

➡️ Copy JWT token from response

---

### 🔗 2. Create Short URL
POST /api/urls

Headers:
Authorization: Bearer <your_token>

Body:
{
  "originalUrl": "https://google.com"
}

---

### 🔁 3. Redirect
Open in browser:
http://3.106.218.248:8080/{shortCode}

---

### 📊 4. Get Stats
GET /api/urls/{shortCode}/stats

---

### 📈 5. Get Analytics
GET /api/urls/{shortCode}/analytics

---

## 🧠 Key Highlights

- ⚡ Optimized performance using Redis caching  
- 🔐 Secure authentication with JWT (1-hour expiry)  
- 🚀 Scalable and modular backend architecture  
- 📊 Asynchronous analytics processing  
- 🛡️ Rate limiting for abuse prevention  
- 🐳 Dockerized for easy deployment  

---

## 🧱 Architecture

Client → Controller → Service → Repository → Database  
                     ↓  
                   Redis (Cache)

---

## 🚀 Features

- 🔗 URL shortening using Base62 encoding  
- 🔐 JWT-based authentication & authorization  
- ⚡ High-speed redirection with caching  
- ⏳ URL expiry support  
- 📊 Click tracking & analytics  
- 🚫 Rate limiting  
- 🗑️ Soft delete support  
- 🔁 Scheduled cleanup jobs  

---

## 🛠️ Tech Stack

Backend: Java, Spring Boot  
Database: PostgreSQL  
Cache: Redis  
Security: JWT (Spring Security)  
Containerization: Docker  
Build Tool: Maven  

---

## ⚙️ API Endpoints

POST   /auth/register                   → Register user  
POST   /auth/login                      → Login & get JWT  
POST   /api/urls                        → Create short URL  
GET    /{shortCode}                     → Redirect to original URL  
GET    /api/urls/{shortCode}/stats      → Get statistics  
GET    /api/urls/{shortCode}/analytics  → Get analytics  

---

## ⚡ How It Works

1. User authenticates and receives a JWT token  
2. Submits a long URL  
3. Unique ID is generated  
4. Encoded using Base62  
5. Stored in database & cached in Redis  
6. Short URL is returned  

---

## 🔁 Redirect Flow

- Check Redis cache  
- If cache miss → fetch from database  
- Return original URL with HTTP 302 redirect  

---

## 📊 Performance Optimizations

- Redis caching reduces database load  
- Indexed queries for faster lookups  
- Asynchronous processing for analytics  
- Rate limiting ensures system stability  

---

## 🔑 Test Credentials

{
  "email": "test@example.com",
  "password": "123456"
}

---

## 🚀 Future Improvements
  
- 🌍 Custom domain support  
- 📊 Advanced analytics dashboard  
- ⚡ Distributed caching (Redis Cluster)  

---

## 👨‍💻 Author

Shivam Kumar  
Backend Developer (Java + Spring Boot)

---

## ⭐ Why This Project Stands Out

- Demonstrates real-world backend system design  
- Covers scalability, caching, and performance optimization  
- Includes authentication, rate limiting, and async processing  
- Built with production-level practices  

## 📸 Screenshots

### 🔐 Authentication (JWT)

<img width="1103" height="860" alt="{C94C7808-628F-4C5F-812B-F9F8B92B3578}" src="https://github.com/user-attachments/assets/39bc4282-cfa8-49b2-9e45-acb9f916cc56" />


---

### 🔗 Create Short URL

<img width="1105" height="864" alt="{36946BCE-8BCF-4BDD-AB2A-47279F2B51C2}" src="https://github.com/user-attachments/assets/eba5b9ab-2959-41ab-82c8-6cdb153d7e2e" />


---

### 🔁 Redirect Flow

<img width="1109" height="621" alt="{F4F2408B-5543-4960-8222-44583C10EB65}" src="https://github.com/user-attachments/assets/ed173a3c-027d-4e86-81a5-2b893b96be0b" />


---

### 📊 Analytics

<img width="1107" height="861" alt="{F47FECC5-0E8B-4C77-A0E5-D6635F09101E}" src="https://github.com/user-attachments/assets/429050a9-d708-4eaf-ad5d-52dc351a33f3" />
