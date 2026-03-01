# 🍔 FoodWagon - Food Ordering Platform

FoodWagon is a full-stack food ordering platform built with **Spring Boot** and **React**.

---

## 🚀 Tech Stack

**Backend:** Java 17, Spring Boot, Spring Security, JWT, PostgreSQL  
**Frontend:** React, TypeScript, Vite, Context API

---

## ✨ Features

- 🔐 **Authentication:** Register/Login with JWT, role-based access (Customer/Partner/Admin)
- 👤 **User Management:** View users, add friends
- 🍽️ **Restaurant Management:** Create/manage restaurants, menu, open/close status
- 🛒 **Order Processing:** Place orders, track status
- 📊 **Partner Dashboard:** Restaurant analytics and order management

---


## ⚙️ Backend Setup

### Prerequisites
- Java 17
- PostgreSQL
- Maven

### Steps
```bash
# Clone and navigate
git clone https://github.com/your-username/FoodWagon.git
cd FoodWagon/backend

# Create database
psql -U postgres -c "CREATE DATABASE foodwagon;"

# Configure environment variables
export DB_URL=jdbc:postgresql://localhost:5432/foodwagon
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export JWT_SECRET=your_super_secret_key
export FRONTEND_URL=http://localhost:5173

# Run
mvn spring-boot:run
Backend runs at: http://localhost:8080

⚙️ Frontend Setup
Prerequisites
Node.js 18+

Steps
bash
cd ../frontend
npm install
echo "VITE_BACKEND_URI=http://localhost:8080" > .env
npm run dev
Frontend runs at: http://localhost:5173

🔐 Environment Variables
Backend
Variable	Description
DB_URL	PostgreSQL connection URL
DB_USERNAME	Database username
DB_PASSWORD	Database password
JWT_SECRET	JWT signing key
FRONTEND_URL	Frontend URL for CORS
Frontend
Variable	Description
VITE_BACKEND_URI	Backend API URL
🔐 Authentication Flow
User logs in → Backend validates credentials

Backend generates JWT token

Token sent to frontend

Frontend includes token in subsequent requests

Spring Security validates token for protected routes

🐳 Docker
bash
cd backend
mvn clean package
docker build -t foodwagon-backend .
docker run -p 8080:8080 --env-file .env foodwagon-backend
🚢 Deployment
Backend: Render, Railway, or AWS

Frontend: Vercel or Netlify

Database: PostgreSQL (AWS RDS or Render PostgreSQL)




