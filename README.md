## 📖 목차

1. [🔎 프로젝트 소개](#-프로젝트-소개)
2. [🎯 프로젝트 기간](#-프로젝트-기간)
3. [🧑‍💻 팀원 소개](#-팀원-소개)
4. [🧑‍💻 역할 분담](#-역할-분담)
5. [🚀 주요 기능](#-주요-기능)
6. [⚙️ 기술 스택](#️-기술-스택)
7. [📃 API 설계서](#-api-설계서)
8. [🛠 ERD](#-erd)
9. [🛠 아키텍처](#-아키텍처)
10. [❓ 기술적 의사결정](#-기술적-의사결정)
11. [📈 성능 개선](#-성능-개선)
12. [🚨 트러블 슈팅](#-트러블-슈팅)

## 🔎 프로젝트 소개

-
## 🎯 프로젝트 기간
- 1차 MVP 개발 기간 : 2025.04.13 ~ 2025.04.27

<details>
  <summary>🎇 프로젝트 실행 방법</summary>

### 1️⃣ Git Clone
  ```bash
  git clone 
```

### 2️⃣ .env 파일 설정

```
# Docker
MYSQL_ROOT_PASSWORD={데이터베이스 비밀번호}
MYSQL_DATABASE={데이터베이스 이름}

DB_USERNAME={데이터베이스 username}
DB_PASSWORD={username의 비밀번호}

# SMTP
MAIL_USERNAME={SMTP 메일 송신 이메일}
MAIL_PASSWORD={SMTP 메일 송신 비밀번호}

# JWT Secret Key
JWT_SECRET_KEY={JWT KEY값}

# Encryption Secret Key
ENCRYPTION_SECRET_KEY={암호화 KEY값}

# 스프링 데이터베이스 URL
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/{데이터베이스 이름}?useSSL=false&allowPublicKeyRetrieval=true
```

### 3️⃣ Docker 이미지 빌드
``` bash
docker buildx build --platform linux/amd64 -f eureka-server/Dockerfile -t eureka-server:latest . --load
```

### 4️⃣ Docker Compose로 컨테이너 실행
```bash
docker-compose up --build -d
```    
</details>

## 🧑‍💻 팀원 소개

| **이름**    | **역할**        | 
|:-----------:|:---------------:|
| 김민주      | 팀장 & BE       | 
| 오예준      | BE              | 
| 정누리      | BE              | 
| 박지원      | FE              |
| 정하승      | FE              | 


## 🧑‍💻 역할 분담

| **이름**    | **역할**        | 
|:-----------:|:---------------:|
| 김민주      | 기능1              |
| 오예준      | 기능2              | 
| 정누리      | 기능3              | 

## 🚀 주요 기능
### 📌 주 기능 1
- 부 기능 1
- 부 기능 2
- 부 기능 3
### 📌 주 기능 2
- 부 기능 1
- 부 기능 2
- 부 기능 3
### 📌 주 기능 3
- 부 기능 1
- 부 기능 2
- 부 기능 3
-
## ⚙️ 기술 스택
- Backend : Java 17, Spring Boot 3.4.0, Spring Data JPA, Spring Security
- Database : MySQL, Redis, DBeaver
- Test Tool : Postman
- DevOps : Docker, Git
- ETC : InteliJ, Google SMTP

## 📃 API 설계서
-
## 🛠 ERD
-
## 🛠 아키텍처
-

## ❓ 기술적 의사결정

<details>
<summary> 1️⃣ Spring Boot 3.4.0과 JAVA 17 버전 선택</summary>

</details>

<details>
  <summary> 2️⃣ MySQL (RDBMS)</summary>

</details>

<details>

  <summary> 3️⃣ </summary>

</details>

<details>
  <summary> 4️⃣ </summary>

</details>


<details>

  <summary> 5️⃣ </summary>

</details>

<details>
  <summary> 6️⃣ </summary>

</details>

<details>
    <summary> 7️⃣ </summary>

</details>

<details>
      <summary> 8️⃣ </summary>

</details>

## 📈 성능 개선

## 🚨 트러블 슈팅
