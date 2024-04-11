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