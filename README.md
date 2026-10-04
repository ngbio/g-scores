# G-Scores

An application to look up THPT 2024 exam scores, view score statistics, and find the top 10 students in Group A (Math, Physics, Chemistry).

- Repository: https://github.com/ngbio/g-scores
- Demo: https://g-scores.pages.dev/top-10
- Tech stack: React, Spring Boot, MySQL.

## Run locally

Requirements: Java 21, Node.js 24, and MySQL 8. Commands below use PowerShell.

### 1. Set up

```powershell
git clone https://github.com/ngbio/g-scores.git
cd g-scores
Copy-Item .env.example .env
Copy-Item frontend/.env.example frontend/.env
```

Create a MySQL database:

```sql
CREATE DATABASE gscores;
```

Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in the root `.env` to your local MySQL connection. The example URL uses the `gscores` database on port `3306`.

### 2. Import data and start the backend

The dataset is included at `dataset/diem_thi_thpt_2024.csv`. Run the import once before starting the app:

```powershell
cd backend
.\mvnw.cmd clean package
.\import.ps1
.\mvnw.cmd spring-boot:run
```

The backend runs at http://localhost:8081. Database tables are created automatically by Flyway.

### 3. Start the frontend

Open another terminal from the project root:

```powershell
cd frontend
npm ci
npm run dev
```

Open http://localhost:5173.
