# 🔗 URL Shortener Backend (Spring Boot + Redis)

A scalable URL Shortener backend application built using **Spring Boot**, designed to convert long URLs into short links with features like caching, analytics, rate limiting, and expiry handling.

---

## 🚀 Features

* 🔗 Generate short URLs using Base62 encoding
* ⚡ Fast redirection using HTTP 302
* 🧠 Redis caching for high performance
* ⏳ URL expiry support
* 📊 Click tracking and analytics
* 🚫 Rate limiting using Bucket4j
* 🗑️ Soft delete (inactive URLs)
* 🔁 Scheduled cleanup for expired URLs
* 📄 Swagger API documentation

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Database:** PostgreSQL
* **Cache:** Redis
* **Build Tool:** Maven
* **Containerization:** Docker
* **API Docs:** Swagger (OpenAPI)

---

## 📂 Project Structure

Controller → Service → Repository → Entity → DTO → Config → Util

---

## ⚙️ API Endpoints

### Create Short URL

POST /api/urls

### Resolve Short URL

GET /api/urls/{shortCode}

### Redirect

GET /{shortCode}

### Get Stats

GET /api/urls/{shortCode}/stats

### Get Analytics

GET /api/urls/{shortCode}/analytics

### Delete URL

DELETE /api/urls/{shortCode}

---

## ⚡ How It Works

1. User sends a long URL
2. URL is stored in database
3. Unique ID is generated
4. ID is encoded using Base62
5. Short URL is returned

On redirect:

* Check Redis cache
* If not found, fetch from DB
* Return original URL with 302 redirect

---

## 📊 Key Concepts Used

* Caching with Redis
* Rate Limiting (Token Bucket Algorithm)
* Scheduler for background jobs
* REST API design principles
* Exception handling & validation

---

## 🌐 Live Demo

👉 https://url-shortener-9opq.onrender.com/

---

## 📁 GitHub Repository

👉 https://github.com/Shivaay21/url-shortener

---

## 🧑‍💻 Author

Shivam Kumar
Backend Developer (Java + Spring Boot)

---

## 📌 Future Improvements

* User authentication (JWT)
* Custom domain support
* Advanced analytics dashboard
* Distributed caching (Redis Cluster)
