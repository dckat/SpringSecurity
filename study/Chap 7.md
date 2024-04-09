# 7. 권한 부여 구성: 액세스 제한
  * GrantedAuthority
    ```
    public interface GrantedAuthority extends Serializable {
        String getAuthority();  // 권한의 이름을 문자열로 반환
    }
    ```
  * UserDetails에서의 GrantedAuthority
    ```
    public interface UserDetails extends Serializable {
        Collection<? extends GrantedAuthority> getAuthorities();
    }
    ```
    * 각 사용자별로 하나 이상의 권한을 가짐
    * 사용자별로 가지고 있는 모든 권한을 반환하도록 구현 → 애플리케이션의 이용권한 부여에도 활용
***
 ## 7.1. 사용자 권한을 기준으로 엔드포인트 접근 제한
  * 활용 메소드
    * hasAuthority(): 애플리케이션 제한을 구성하는 하나의 권한만을 설정. 해당 권한이 존재하는 사용자만 접근
    * hasAnyAuthority(): 애플리케이션 제한을 구성하는 하나 이상의 권한을 설정. 해당 권한이 하나라도 있으면 접근 가능
    * access(): SpEL기반 권한 부여 규칙을 정의함. 코드 가독이나 디버그에 어렵다는 단점 존재
  * 엔드포인트 접근 예시
    * hasAuthority()
      ```
      public class ProjectConfig extends WebSecurityConfigurerAdapter {
          ...
    
          @Override
          protected void configure(HttpSecurity http) throws Exception {
              http.httpBasic();
    
              // WRITE 권한이 있는 사용자만 엔드포인트에 접근
              http.authorizeRequests().anyRequest().hasAuthority("WRITE");
          }
      }
      ```
    * hasAnyAuthority()
      ```
      public class ProjectConfig extends WebSecurityConfigurerAdapter {
          ...
    
          @Override
          protected void configure(HttpSecurity http) throws Exception {
              http.httpBasic();
    
              // READ 또는 WRITE 권한이 있는 사용자만 엔드포인트에 접근
              http.authorizeRequests().anyRequest()
                   .hasAuthority("READ", "WRITE");
          }
      }
      ```
    * access()
      ```
      public class ProjectConfig extends WebSecurityConfigurerAdapter {
          ...
    
          @Override
          protected void configure(HttpSecurity http) throws Exception {
              http.httpBasic();
    
              // WRITE 권한이 있는 사용자만 엔드포인트에 접근 (access 메소드 활용)
              http.authorizeRequests().anyRequest()
                   .access("hasAuthority('WRITE')");
          }
      }      
      ```
***
 ## 7.2 사용자 역할을 기준으로 엔드포인트 접근 제한
  * 역할의 정의 
    * authorities() 메소드 활용 시:  ROLE_ 접두사로 시작. 실질적인 역할은 언더바 이후로 설정
      * ex) ROLE_ADMIN → ADMIN, ROLE_MANAGER → MANAGER
    * roles() 메소드 활용 시: ROLE_ 접두사 없이 역할의 이름만을 지정
      * ex) roles("ADMIN");
  * 활용 메소드
    * hasRole(): 요청을 승인할 하나의 역할을 설정. 해당 역할과 동일한 사용자만 접근 가능
    * hasAnyRole(): 요청을 승인할 여러 역할을 설정. 해당 역할 중 하나만을 만족하는 사용자 접근
    * access(): 요청 승인을 SPeL에 따라 지정
  * 엔드포인트 접근 예시
    ```
    public class ProjectConfig extends WebSecurityConfigurerAdapter {
        ...
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic();
    
            // ADMIN 역할을 가진 사용자만 접근 가능하도록 제한
            http.authorizeRequests()
                    .anyRequests().hasRole("ADMIN");
        }
    }
    ```
  * 모든 엔드포인트 접근 제한
    * 메소드: denyAll()을 활용하여 모든 사용자의 접근을 제한
    * 예시
     ```
     public class ProjectConfig extends WebSecurityConfigurerAdapter {
         ...
    
         @Override
         protected void configure(HttpSecurity http) throws Exception {
             http.httpBasic();
    
             // 모든 사용자 접근 제한
             http.authorizeRequests()
                     .anyRequests().denyAll();
         }
     }    
     ```
***