## 🚀 BackEnd 기술 스택
- BackEnd : Spring Boot 3.3.1, Spring Security, jpa, QueryDSL, ElasticSearch(LogStash), RabbitMQ
- DBMS : Mysql, Redis
- CI/CD : NGIX, GitAction, Docker, EC2, S3,
- ETC : Swagger, InteliJ

## 🚀 역할 분담
### 📌 사용자 - 오예준
- Security를 사용한 사용자 인증
- 팔로우/팔로잉/차단 기능
### 📌 피드 - 정누리
- 피드 CRUD
- 피드 좋아요/댓글
### 📌 알림/검색 - 정누리
- SSE를 이용한 알림
- elastic Search를 이용한 검색 
### 📌 채팅 - 오예준
- WebSocket과 RabbitMQ를 이용한 1:1 채팅
