🚀 URL Shortener Backend (Spring Boot + Redis + JWT)

A production-ready scalable URL Shortener API built using Spring Boot that supports authentication, caching, analytics, and rate limiting.

Designed with a focus on performance, scalability, and clean architecture.


🌐 Live API

👉 http://3.106.218.248:8080/

⚠️ Note: This is a backend-only project. Use Postman / Swagger to test APIs.


⚡ Quick Test (2 Minutes)
🔐 1. Login (Get JWT Token)

POST /auth/login

{
  "email": "test@example.com",
  "password": "123456"
}

👉 Copy the token from response

🔗 2. Create Short URL

POST /api/urls

Headers:

Authorization: Bearer <your_token>

Body:

{
  "originalUrl": "https://google.com"
}
🔁 3. Redirect

Open in browser:

http://3.106.218.248:8080/{shortCode}
📊 4. Get Stats

GET /api/urls/{shortCode}/stats

📈 5. Get Analytics

GET /api/urls/{shortCode}/analytics


🔑 Test Credentials
{
  "email": "test@example.com",
  "password": "123456"
}


🧠 Key Highlights
⚡ Improved response time using Redis caching
🔐 JWT-based authentication (1-hour expiry)
🚀 Scalable REST API design
📊 Async analytics tracking
🛡️ Rate limiting for abuse prevention
🐳 Dockerized deployment


🧱 Architecture
Client → Controller → Service → Repository → Database
                     ↓
                   Redis (Cache)

                   
🚀 Features
🔗 Short URL generation (Base62 encoding)
🔐 User authentication & authorization (JWT)
⚡ Redis caching for faster redirects
⏳ URL expiry support
📊 Click tracking & analytics
🚫 Rate limiting
🗑️ Soft delete support
🔁 Scheduled cleanup jobs


🛠️ Tech Stack
Backend: Java, Spring Boot
Database: PostgreSQL
Cache: Redis
Security: JWT
Containerization: Docker
Build Tool: Maven


⚙️ API Endpoints
Method	Endpoint	Description
POST	/auth/register	Register new user
POST	/auth/login	Login & get JWT
POST	/api/urls	Create short URL
GET	/{shortCode}	Redirect
GET	/api/urls/{shortCode}/stats	Stats
GET	/api/urls/{shortCode}/analytics	Analytics


⚡ How It Works
User logs in and gets JWT token
Sends URL to API
Unique ID is generated
Encoded using Base62
Stored in DB + cached in Redis
Short URL returned


🔁 Redirect Flow
Check Redis cache
If miss → fetch from DB
Return original URL with HTTP 302 redirect


📊 Performance Optimizations
Redis caching to reduce DB load
Indexed database queries
Async processing for analytics
Rate limiting for stability


🚀 Future Improvements
🌍 Custom domain support
📊 Advanced analytics dashboard
⚡ Redis cluster (distributed caching)
🧑‍💻 Author

Shivam Kumar
Backend Developer (Java + Spring Boot)
