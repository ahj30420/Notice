 # <공지사항 관리 REST API   *Notice*> 
<hr>

### 핵심 로직
- **로그인 인증,인가** 구현(Session / Interceptor 사용)
- 대용량 트래픽에 대비해 **Redis Session Clustering** 활용
- **다중 첨부 파일** 저장 및 다운로드
- 공지 사항 **페이징(Paging) 처리**
- 등록 및 수정에 대한 **Validation / Exception**
- JdbcTemplate을 통해 불필요한 쿼리 개선(**N+1 문제 최적화**)
<hr>
