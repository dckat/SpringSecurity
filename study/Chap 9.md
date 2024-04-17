# 9. 필터 구현
 ## 9.1 스프링 시큐리티 아키텍처의 필터 구현
  * 스프링 시큐리티에서 작동하는 필터
    * BasicAuthenticationFilter: HTTP basic 인증 처리
    * CsrfFilter: CSRF(사이트 간 요청 위조) 처리
    * CorsFilter: CORS(교차 출처 리소스 공유) 권한 부여 규칙 처리
  * 필터 생성
    * javax.serlet 패키지의 Filter 인터페이스를 구현 → doFilter 메소드 재정의
    * doFilter의 매개변수
      * ServletRequest: HTTP 요청. 요청에 대한 세부정보를 획득
      * ServletResponse: HTTP 응답. 클라이언트에 응답을 보내기 전 or 필터체인에서 응답 변경
      * FilterChain: 필터 체인. 다음 필터로의 요청 전달
  * 필터의 번호
    * 번호에 따라 필터가 적용되는 순서가 결정
    * 같은 순서의 값을 가질수 있음 → 호출되는 순서는 정해지지 않음
***
 ## 9.2 필터 추가
  * 구현 순서
    1) 구현하려고 하는 필터를 구현
    2) 구성클래스의 configure() 메소드를 재정의하여 필터 체인에 필터 추가
  * 기존 필터 앞에 필터 추가
    * addFilterBefore(): 기존 필터의 앞에 커스터마이징 한 필터를 실행하도록 함
    * 주요 매개변수
      * 커스터마이징한 필터의 인스턴스
      * 새 인스턴스를 추가할 위치: 매개변수로 받은 위치의 앞에 커스터마이징한 필터가 존재
  * 기존 필터 뒤에 필터 추가
    * addFilterAfter(): 기존 필터 뒤에 커스터마이징 한 필터를 실행하도록 함
    * 주요 매개변수: addFilterBefore() 메소드와 동일
  * 필터 체인의 다른 필터 위치에 필터 추가
    * addFilterAt(): 어느 위치에 필터가 실행될지를 지정하는 메소드
    * 주요 매개변수: addFilterBefore() 메소드와 동일
***
 ## 9.3. OncePerRequestFilter
  * 주요 내용
    * 추가된 필터를 동일한 요청에 한해 한 번만 실행되도록 구현한 필터
  * 참고 사항
    * HTTP 필터만 지원하므로 매개변수로 HttpServletRequest.HttpServletResponse 지정 → 요청과 응답의 형 변환 필요 X
    * 필터가 적용될지에 대한 논리 구현 가능: shouldNotFilter(HttpServeltReqeust) 메소드를 재정의
    * 비동기 요청이나 오류 발송 요청에는 적용 X → shouldNotFilterAsyncDispatch(). shouldNotFilterErrorDispatch()를 재정의하여야 함.
***