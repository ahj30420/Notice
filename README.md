 # <공지사항 관리 REST API   *Notice*> 
<hr>

### 개발 Stack
- Language & Framwork: **Java 17, Spring Boot 3.2.4**
- DB 접근: **Spring Data Jpa, QueryDSL, JdbcTemplate**
- DB & 저장소: **MySQL, Spring Data Redis**
- Front: **Thymeleaf**
- IDE: **IntelliJ 2023.2.5, MySQL Workbanch 6.3**
<hr>

### 핵심 로직
- **로그인 인증,인가** 구현(Session / Interceptor 사용)
- 대용량 트래픽에 대비해 **Redis Session Storage** 활용
- **다중 첨부 파일** 저장 및 다운로드
- 공지 사항 **페이징(Paging) 처리**
- 등록 및 수정에 대한 **Validation / Exception**
- JdbcTemplate을 통해 불필요한 쿼리 개선(**쿼리 최적화**)
<hr>

## 💻핵심 기능 소개
### 회원 가입
- 이름: **입력값 검증** / **이름 중복 체크**
- 패스워드: **입력값 검증** / **BCryptPasswordEncoder 암호화**
  
![회원가입](https://github.com/ahj30420/Notice/assets/79964990/85851984-695d-4fd0-a4a8-c169219e2c26)
<hr>

### 공지사항 등록
- 제목, 내용, 시작 일시, 마감 일시, 다중 첨부 파일 입력
![공지사항 등록](https://github.com/ahj30420/Notice/assets/79964990/f29803a8-8448-4674-bcd3-f02488fb344b)
<hr>

### 전체 공지사항
- 한 페이지 당 5개의 공지사항 출력
![전체 공지 사항](https://github.com/ahj30420/Notice/assets/79964990/70372978-21f9-446f-86b5-8d7c930307fc)
<hr>

### 공지사항 상세 보기
- 첨부 파일 클릭 시 다운로드 가능
- 공지사항 수정 및 삭제 가능
![공지사항 상세 정보](https://github.com/ahj30420/Notice/assets/79964990/2bd4297e-724a-41ec-86da-ba449427fc1c)
<hr>

### 공지사항 수정 
- **제목, 내용, 시작일시, 종료일시**만 수정 가능
- 첨부 파일 추가 등록 및 삭제 가능
![공지사항 수정](https://github.com/ahj30420/Notice/assets/79964990/ddab8efe-ecee-4d5b-9d1f-64dc01c54d0b)
<hr>

## 👨‍💻핵심 문제 해결 전략
### Inmemory DB를 이용한 세션 관리
대용량 트래픽에 대비하기 위해 **Redis를 활용하여 세션을 관리하는 전략**을 세웠습니다. 초기 개발 단계에서는 일반적으로 로컬 서버를 활용하나, 프로젝트를 실제 운영 환경으로 배포할 때는 **대용량 트래픽을 대비하여 여러 대의 서버를 고려**해야 합니다. 이 과정에서 세션 관리 방법이 성능에 큰 영향을 미칠 수 있습니다. 저는 기존 서버의 세션 저장소를 사용하는 대신, **별도의 세션 저장소로 In-memory DB를 선택하여 세션 스토리지를 분리**했습니다. 이렇게 함으로써 **서버의 확장과 축소에 더욱 유연하게 대응할 수 있으며, 대용량 트래픽 분산에 용이**합니다.<br>

[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/config/RedisConfig.java)
![Redis](https://github.com/ahj30420/Notice/assets/79964990/e8660292-c445-4e9c-9d6c-bb37cbc76b31)
<hr>

### 파일 저장 시 실제 파일 이름과 저장할 때 사용할 파일 이름 분리
서버 내부에는 사용자가 업로드한 파일명과는 별개로 **각 파일을 식별할 수 있는 고유한 이름을 사용하여 저장**해야 합니다. 이를 통해 **서로 다른 사용자가 같은 파일명을 업로드할 때 파일명 충돌을 방지**할 수 있습니다. 저장 할 시 사용할 파일 이름은 **고유성을 보장 하기 위해 UUID**로 생성하였습니다.<br>

[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/util/FileStore.java)
![파일 이름 분리](https://github.com/ahj30420/Notice/assets/79964990/a3cf73a8-796e-4fd9-8e98-bcb5dc634b6b)
<hr>

### 공지사항 페이징 처리
- **대용량 트래픽을 고려하여 공지사항을 전체 조회하는 대신, 페이징 처리하여 필요한 양만큼만 조회**하였습니다.
- 현 시간을 기준으로 공지사항 시작 일시와 마감 일시에 부합하는 공지사항을 조회하였습니다.
- **가장 최근에 등록 및 수정된 공지사항순으로 정렬**하여 5개씩 조회하였습니다.<br>

[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/repository/noticeRepository/NoticeRepositoryImpl.java)
![페이징 처리](https://github.com/ahj30420/Notice/assets/79964990/320bec8f-4c74-4dda-86e9-46cef9422a6e)
<hr>

### 다중 첨부 파일 Batch Insert
- **Notice 엔티티와 AttachFile 엔티티가 일대다 연관 관계**이기 때문에 하나의 공지사항에 여러 첨부 파일을 포함하여 등록하게 되면 첨부 파일 수만큼 Attach 테이블에 여러 개의Insert 쿼리문이 적용되게 됩니다. 이를 해결하기위해 **JdbcTemplate의 batchUpdate()** 를 활용하였습니다.
- **엔티티의 IDENTITY 전략은 ID값을 데이터베이스에서 관리하도록 하기 때문에 INSERT문이 실행되기 전에는 새롭게 할당된 ID값을 알 수 없습니다.** 따라서 transactional write-behind을 수행 할 수 없으며 이러한 이유 때문에, **하이버네이트는 IDENTITY 전략을 사용하는 엔티티에 대해서 Batce Insert를 지원하지 않습니다.** <br>


[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/repository/FileRepository/FileRepositoryImpl.java)
![Batch Insert](https://github.com/ahj30420/Notice/assets/79964990/06b03db4-4417-4b7e-9130-b5d3285306aa)
<hr>

### 관리자만 공지사항 등록, 수정, 삭제 가능
- **Interceptor**를 통해 관리자만 공지사항에 대한 관리 권한을 갖도록 설계하였습니다.<br>

[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/interceptor/AdminCheckInterceptor.java)
![Interceptor](https://github.com/ahj30420/Notice/assets/79964990/1e8ab35a-6423-452a-a334-ddedae57f39d)
<hr>

### RESTfull API 설계
- 웹 브라우저는 일반적으로 GET과 POST 메서드만 지원합니다. 그러나 RESTful API 설계를 위해 HTTP POST 메서드만 허용되는 웹 애플리케이션에서 **HTTP PUT, DELETE, PATCH와 같은 다른 HTTP 메서드를 사용할 수 있도록 HiddenHttpMethodFilter를 사용**하였습니다.<br>

[코드확인](https://github.com/ahj30420/Notice/blob/master/src/main/java/project/notice/config/SpringConfig.java)<br>
![HiddenHttpMethodFilter](https://github.com/ahj30420/Notice/assets/79964990/d0b42def-37ba-4805-9cae-f678767325af)
