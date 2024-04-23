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
 ## 10.2 CORS 이용
  * CORS(Cross-Origin Resource Sharing)
    * 웹 페이지 상의 제한된 리소스를 최초 자원이 서비스된 도메인 밖의 다른 도메인으로부터 요청할 수 있게 허용하는 구조
    * 출처 이미지. 스타일시트. 스크립트 등을 자유롭게 임베드 가능
    * 작동방식
      * 비활성화
        ![cors_block](https://github.com/dckat/SpringSecurity/assets/19167273/47bc26f7-8921-44c5-a9c2-3bff5bf2d704)
      * 활성화
        ![cors](https://github.com/dckat/SpringSecurity/assets/19167273/cf7b9463-6ab7-4205-b561-16064f177353)
    * CORS 주요 헤더
      * Access-Control-Allow-Origin: 도메인 리소스에 접근가능한 외부 도메인 지정
      * Access-Control-Allow-Methods: 특정 HTTP Method 방식만 허용할 시 지정
      * Access-Control-Allow-Headers: 특정 요청에 이용가능한 헤더에 제한 추가
  * CORS 적용 방식
    * CrossOrigin 어노테이션 활용
      * 엔드포인트 정의 메소드에 어노테이션을 배치하여 허용된 출처 지정
      * 매개 변수로 여러 출처를 정의하는 배열을 지정
        * ex) @CrossOrigin({"example.com", "example.org"})
      * 장.단점
        * 장점: 직접 규칙을 지정하여 투명한 규칙이 가능
        * 단점: 각 메소드별로 지정해야 하므로 많은 코드 반복이 필요하고 추후 유지보수에 어려움
    * CorsConfigurer 활용
      * CORS 설정을 하나의 구성클래스에 집중하여 설정
      * cors() 메소드를 활용하여 CORS 구성 정의
      * cors 구성 예시 코드 (람다 함수 활용)
        ```
        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                // CORS 설정
                Corsconfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("example.com", "example.org"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                return config;
            }
        });
        ```
      * 직접 어노테이션을 지정하는 방식보다 하나의 클래스에서 지정하여 유지보수 용이하는 장점 존재
***
