This repository represents the backend of application, which is foundation of application. In order to run application locally, follow steps below. NOTE: the backend service should be setup and run before going to https://github.com/ArtemMaslii/PrepNesterApp/edit/main/README.md



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
