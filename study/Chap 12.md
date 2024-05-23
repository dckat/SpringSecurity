# 12. OAuth2가 작동하는 방법
 ## 12.1. OAuth2 프레임워크
   * 기존 HTTP Basic 인증 방식 문제점
     * 네트워크를 통한 자격증명이 공유
     * 클라이언트가 자격증명을 저장하여 인증.권한 부여 요청과 함께 자격증명을 서버에 전송
   * OAuth 개념
     * 웹 애플리케이션 인증 및 권한부여를 위한 개방형 표준 프로토콜
     * 사용자들이 비밀번호를 제공하지 않고 다른 웹 사이트 상의 자신들의 정보를 통해 접근 권한 부여
       * ex) 네이버.카카오.구글 등을 통한 로그인
***
 ## 12.2. OAuth2 인증 아키텍처의 구성요소
   * 리소스 서버: 사용자가 소유한 리소스를 호스팅하는 서버. 클라이언트는 해당 서버로 인증서버에서 발급받은 토큰을 넘겨 개인정보를 받을수 있음
     * ex) 구글. 네이버. 카카오 등
   * 사용자(리소스 소유자): 리소스 서버에서 노출하는 리소스를 소유한 개인. 일반적으로 사용자 이름과 암호로 신원 증명
   * 클라이언트: 사용자를 대신하여 사용자가 소유한 리소스에 접근하는 애플리케이션. 클라이언트는 요청 시 자신을 증명하는 자체 자격증명 필요(클라이언트 ID.PW 이용)
   * 권한부여 서버: 클라이언트가 사용자의 리소스 서버 접근 권한을 부여하는 애플리케이션
***
 ## 12.3. OAuth2 구현방법
   * 승인코드 그랜트 유형
     
     ![image](https://github.com/dckat/SpringSecurity/assets/19167273/fc3899e0-95f7-4915-a05d-46f7d4b8b5b9)
     * 인증을 수행하는 서버로 클라이언트의 접근 자격을 확인하고 액세스 토큰을 발급하여 권한을 부여
     * Refresh Token 사용 가능 방식으로 OAuth2에서 가장 많이 사용
     * 타사의 클라이언트에게 보호된 자원을 제공하기 위한 인증에 사용
     * 작동단계
       1) 인증 요청: 클라이언트는 사용자가 인증해야 하는 권한 부여 서버의 엔드포인트로 사용자를 리디렉션
          * 요청 쿼리 시 포함되는 세부 정보
            * response_type: 권한 부여 서버에 전달할 code
            * client_id: 클라이언트 ID 값
            * redirect_uri: 인증 성공 후 리디렉션할 위치
            * scope: 허가된 권한
            * state: CSRF 보호를 위한 CSRF 토큰 정의
          * 사용자-권한 부여 서버 간의 상호 작용을 수행
          * 인증 성공 시 리디렉션 URI로 클라이언트를 호출하고 승인코드와 상태값 반환
       2) 액세스 토큰 획득: 클라이언트가 토큰을 얻기 위해 1단계에서 받은 승인 코드를 권한부여 서버를 호출
          * 클라이언트-권한 부여 서버 간의 상호 작용 수행
          * 액세스 토큰을 얻기위한 필요한 정보
            * 전단계에서 받은 승인코드
            * 승인코드를 가로채지 않았으며 같은 클라이언트임을 증명하는 자격증명
          * 권한부여 서버는 요청에 대한 응답으로 액세스 토큰 반환
       3) 보호된 리소스 호출
   * 암호 그랜트 유형

     ![image](https://github.com/dckat/SpringSecurity/assets/19167273/ff726c2c-9fe3-4a00-82df-e0f2dfd1357d)
     * 사용자의 username.password으로 액세스 토큰을 얻는 방식
     * 클라이언트에 ID와 비밀번호를 직접 제공하므로 보안상 위험이 존재
     * 권한서버.리소스서버.클라이언트가 같은 시스템에 속해있을 때 사용되어야 하는 방식
     * 작동단계
       1) 액세스 토큰 요청
          * 사용자의 자격증명을 수집하고 권한부여 서버를 호출하여 액세스 토큰 획득
          * 요청 시 필요한 세부 정보
            * grant_type: password 값
            * client_id, client_secret: 클라이언트를 인증하기 위한 자격 증명
            * scope: 허가된 권한
            * username, password: 사용자 자격증명
       2) 리소스 호출
   * 클라이언트 자격 증명 그랜트 유형
     
     ![image](https://github.com/dckat/SpringSecurity/assets/19167273/b2d84ea8-41c8-45f8-b1bf-06fca0533307)
     * 클라이언트의 자격증명 만으로 액세스 토큰을 얻는 방식
     * 권한 부여 방식 중 가장 간단한 방식
     * 자신이 관리하는 리소스 또는 권한 서버에 해당 클라이언트를 위한 제한된 리소스 접근 권한이 설정되어 있는 경우 사용
     * 작동단계
       1) 액세스 토큰 요청
          * 클라이언트의 자격 증명을 권한 부여 서버에 요청하여 액세스 토큰 획득
          * 요청 시 필요한 세부 정보
            * grant_type: client_credentials 값
            * client_id. client_secret: 클라이언트 자격증명
            * scope: 허가된 권한
       2) 리소스 호출
   * 갱신 토큰으로 새 액세스 토큰 획득
     * 토큰의 수명을 두어 만료된 토큰에 대한 접근을 제한
     * 만료 시 클라이언트는 권한부여 서버에 새로운 액세스 토큰과 갱신 토큰을 획득
     * 요청 시 포함되는 세부 정보
       * grant_type: refresh_token 값
       * refresh_token: 갱신 토큰의 값
       * client_type. client_secret: 클라이언트 자격증명
       * scope: 허가 권한
***
 ## 12.4. OAuth2 허점
   * 클라이언트에서 CSRF 이용
     * CSRF 보호 매커니즘 미적용 시 사용자가 로그인할 때 CSRF 가능
     * 클라이언트 자격증명 도용: 보호되지 않는 자격증명 저장.전송 시 공격자가 도용하는 상황 발생
     * 토큰 재생: 네트워크를 통해 보내는 동안 누군가가 가로채 해당 토큰이 재사용되는 문제 발생
     * 토큰 하이재킹: 리소스에 액세스 하기 위한 토큰을 가로채 새 액세스 토큰을 얻는 문제 발생
***