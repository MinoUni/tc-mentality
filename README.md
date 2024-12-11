# Mentality

The Yoga Shop & Wellness Platform project with the code name "Mentality" is being initiated because of the rising
interest in wellness and the popularity of yoga as a way to improve both physical and mental health. As more people
incorporate yoga into their daily routines, there’s a real opportunity to create an online space where they can easily
find quality yoga products and some advice on yoga practices. This project isn’t just about selling items - it’s about
providing a welcoming and convenient shopping experience that helps customers discover new products and learn more about
yoga practices.

[Link to Frontend repository](https://github.com/mentality-prj/mentality-web)

## Project Goal

This project is created to improve skills in software development & team communication.

## Table of Contents

- [How to Install and Run the Project Locally](#how-to-install-and-run-the-project-locally)
- [Database Schema](#database-schema)

## How to Install and Run the Project Locally

Prerequisites

- JDK 21 or higher;
- PostgreSQL 16;

1. Clone Repository

```bash
git clone git@github.com:MinoUni/tc-mentality.git
```

2. Create `.env` file in project root and add environment variables:

```
DB_NAME=<db>
DB_NAME_TEST=<testdb>
DOMAIN=localhost
PORT=5432
DB_USERNAME=<postgres>
DB_PASSWORD=<password>
GOOGLE_CLIENT_ID=<clientID>
```

3. Create `Maven Configuration` with `spring-boot:run` command (if using IDE), or use

```bash
./mvnw spring-boot:run
```

4. Open SwaggerUI: [Swagger URL](http://127.0.0.1:8080/swagger-ui.html)

## Database Schema