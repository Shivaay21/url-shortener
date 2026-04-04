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

- 🔐 Role-based access control  
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
