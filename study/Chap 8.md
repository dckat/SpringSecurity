# 8. 권한 부여 구성: 제한 적용
 * 경로별 요청 선택을 위한 선택기 메소드
   * MVC 선택기: 경로에 MVC 식을 이용해 엔드포인트 선택
   * 앤트 선택기: 경로에 앤트 식을 이용해 엔드포인트 선택
   * 정규식 선택기: 경로에 정규식을 이용해 엔트포인트 선택
***
 ## 8.1 선택기 메소드로 엔드포인트 선택
   * 역할별 엔드포인트 접근 제한
     ```
     ...
     
     http.authorizeRequests()
            .mvcMatchers("/hello").hasRole("ADMIN")    // ADMIN 역할만 /hello 호출
            .mvcMatchers("ciao").hasRole("MANAGER");    // MANAGER 역할만 /ciao 호출
     ```
   * 나머지 엔드포인트에 접근 요청 허용
     ```
     ...
     
     http.authorizeRequests()
            .mvcMatchers("/hello").hasRole("ADMIN")
            .mvcMatchers("ciao").hasRole("MANAGER")
            .anyRequest().permitAll();       // 나머지 모든 엔드포인트에 대해 요청 허용
     ```
   * 인증된 사용자에 대해서만 요청 허용
     ```
     ...
     
     http.authorizeRequests()
            .mvcMatchers("/hello").hasRole("ADMIN")
            .mvcMatchers("ciao").hasRole("MANAGER")
            .anyRequest().authenticated();    // 인증된 사용자만 나머지 모든 요청 허용
     ```
***
 ## 8.2 MVC 선택기로 권한을 부여할 요청 선택
   * mvcMatchers 메소드
     * MVC 구문을 활용하여 권한 부여 구성을 적용
     * 두 가지 메소드로 MVC 선택기를 선언
       * mvcMatchers(HttpMethod, String): 제한을 적용할 HTTP 방식과 경로 지정
       * mvcMatchers(String): 경로만을 기준으로 권한 부여 제한 적용
   * MVC 선택기로 경로 일치에 적용되는 식 예시
     * /a: /a 경로만
     * /a/*: 한 경로의 이름만 대체. /a/b 또는 /a/c는 일치하지만 /a/b/c는 일치하지 않음
     * /a/**: 여러 경로의 이름을 대체. /a, /a/b, /a/b/c 모두 일치
     * /a/{param}: 주어진 경로 매개 변수를 포함한 /a에 적용
     * /a/{param:regex}: 매개변수 값과 정규식이 일치할 때만 매개변수를 포함한 /a에 적용
***
 ## 8.3 앤트 선택기로 권한을 부여할 요청 선택
   * antMatchers 메소드
     * antMatchers(HttpMethod, String): 제한을 적용할 HTTP 방식과 경로를 참조할 앤트 패턴 지정
     * antMatchers(String): 경로만을 기준으로 권한 부여 제한 적용
     * antMatchers(HttpMethod): 경로에 관계없이 특정 HTTP 방식에 대한 제한 적용
   * MVC 선택기와 차이점
     * MVC: 여러 경로가 동일한 작업과 일치한다고 해석
       * ex) /hello. /hello/를 같은 규칙으로 모두 보호가 됨
     * 앤트: 위의 예시를 의도치 않게 보호하지 않도록 지정 가능
***
 ## 8.4 정규식 선택기로 권한을 부여할 요청 선택
   * regexMatchers 메소드
     * regexMatchers(HttpMethod, String): 제한을 적용할 HTTP 방식과 경로를 참조할 정규식 지정
     * regexMatchers(String): 경로만을 기준으로 권한 부여 제한 적용
   * 사용 예시
     * 전화번호 또는 이메일 주소 등이 포함된 경로의 특정한 구성
     * 특정 형식이 포함된 모든 경로의 특정한 구성