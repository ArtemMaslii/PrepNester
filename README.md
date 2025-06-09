This repository represents the backend of application, which is foundation of application. In order to run application locally, follow steps below. NOTE: the backend service should be setup and run before going to https://github.com/ArtemMaslii/PrepNesterApp



Create .env file with the following content:

```env
DB_URL=jdbc:h2:./data/prepnester
DB_USERNAME=sa
DB_PASSWORD=password
ISSUE_URL=http://localhost:3000
JWT_SECRET=6hmKOwSW1rxj3Yi_LUpjzPqbEnEoV8Z-PxXkTOerJsA=
# 1 day
JWT_EXPIRATION=86400000
ALLOWED_ORIGINS=http://localhost:3000 
```

Then run the application with:

```bash
./gradlew build

docker-compose up --build -d
```
It should be enough for local setup, if you want to access deployed application via postman: here's root path - 16.171.28.194:8080/api/v1/ then you should get credentials: 
16.171.28.194:8080/api/v1/auth/login
body: {
    "email": "alice@example.com",
    "password": "password_hash1"
}
After that access token is returned which you can use to get requests like:
16.171.28.194:8080/api/v1/questions

For post you can create Cheat Sheet using such link: 
16.171.28.194:8080/api/v1/cheatSheets

and body:

```
{
    "title": "Custom Cheat Sheet",
    "isPublic": "true",
    "categories": [
        {
            "id": "1e52f3b1-b2cf-470d-8338-bdbba6988e7b",
            "questions": [
                {
                    "id": "784bfa2b-6fc4-4288-a8ae-0579c4cc0b18"
                },
                {
                    "id": "784bfa2b-6fc4-4288-a8ae-0579c4cc0b17"
                }
            ]
        },
        {
            "id": "e36fdba0-f12d-4a51-83a9-60e37452a831",
            "questions": [
                {
                    "id": "9345ef2f-cd8f-4eae-97cc-6f4e81ed995d"
                }
            ]
        }
    ],
    "createdBy": "db50f01e-2a8d-4be4-ae60-d9ad89fc4b72"
}
```
