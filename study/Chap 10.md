# 10. CSRF 보호와 CORS 적용
 ## 10.1 CSRF 보호 적용
  * CSRF(Cross-Site Request Forgey)
     * 웹사이트에 악의적인 코드를 삽입하고 이를 폼으로 제출하여 사용자가 원치않는 작업을 하도록 하는 공격
 
       ex) SNS 계정에서 발생되는 피싱사이트 광고: 피싱사이트에 글쓰기 폼 삽입 후 특정사용자의 작성글에 등록
  * CSRF 보호가 작동하는 방식
    1) 데이터 변경 전에 HTTP GET 웹페이지 요청을 수행
    2) 요청 수행 시 애플리케이션에서 고유한 토큰을 생성
    3) 해당 토큰을 활용하여 POST.PUT.DELETE 등의 데이터 변경 작업이 가능
       
       ※ 토큰이 없거나 잘못된 토큰 값일 경우 애플리케이션 요청을 거부
  * CSRF 구성요소
    * CsrfTokenRepository: UUID 형태로 CSRF 토큰을 생성하고 이를 관리
    * CsrfFilter: 요청을 받아서 CSRF 보호 논리를 적용
      * ex) 생성된 토큰을 HTTP 요청의 _csrf 특성에 추가
    * CsrfTokenLogger: CSRF 토큰을 출력하는 맞춤형 필터
***