# 5. 인증 구현
 ## 스프링 시큐리티 인증 흐름
   ![인증 프로세스](https://github.com/dckat/SpringSecurity/assets/19167273/a6877a9d-dcbb-4d3d-a66d-5e96812bdb60)
   1) 인증 필터가 요청을 가로챔
   2) 인증 책임이 AuthenticationManager에 위임
   3) AuthenticationManager는 인증 논리를 구현한 AuthenticationProvider를 이용
   4) AuthenticationProvider는 UserDetailService로 사용자를 찾고 PasswordEncoder로 암호화
   5) 인증 결과가 인증 필터에 반환
   6) 인증된 엔티티에 대한 정보가 SecurityContext에 저장 
 ## AuthenticationProvider 이해
  * Authentication 인터페이스
    * 인증 프로세스의 인터페이스. 인증 요청 이벤트를 나타냄
    * 애플리케이션에 접근을 요청한 엔티티의 세부정보를 담음
      * 주체(Principal): 애플리케이션 접근을 요청하는 사용자
    * 인터페이스 정의
      ```
      public interface Authentication extends Principal, Serializable {

        Collection<? extends GrantedAuthority> getAuthorities();
        Object getCredentials();
        Object getDetails();
        Object getPrincipal();
        boolean isAuthenticated();
        void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

      }
      ```
      * isAuthenticated(): 인증 프로세스의 종료 여부 (종료 시 true, 진행 중이면 false)
      * getCredentials(): 인증 프로세스에 이용된 암호나 비밀 반호나
      * getAuthorities(): 인증된 요청에 허가된 권한의 컬렉션 반환
  * AuthenticationProvider
    * 인터페이스 정의
      ```
      public interface AuthenticationProvider {
      
        Authentication authenticate(Authentication authentication)
            throws AuthticationException;
      
        boolean supports(Class<?> authentication);
      }
      ```
    * authenticate 메소드 구현 방법
      * 인증 실패시 AuthenticationException을 throw
      * AuthenticationProvider에서 지원되지 않는 객체를 받을 시 null 반환
      * 완전히 인증된 객체를 나타내는 Authentication 인스턴스 반환
  * 인증 논리 적용
    * 구현 과정
      1) AuthenticationProvider를 구현한 클래스 선언
      2) 새 AuthenticationProvider가 어떤 종류의 Authentication 객체를 지원할지 결정
         * AuthenticationProvider가 지원하는 인증 유형을 나타내도록 supports 메소드를 재정의
         * authenticate 메소드를 재정의하여 인증 논리 구현
      3) 구현한 AuthenticationProvider의 인스턴스를 스프링 시큐리티에 등록
    * 인증 흐름도
      ![인증 논리 흐름](https://github.com/dckat/SpringSecurity/assets/19167273/91c509ac-2218-46e8-b475-28dac51d6dac)

      
