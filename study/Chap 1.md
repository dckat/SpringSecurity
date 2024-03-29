# 1. 오늘날의 보안
* 주요 내용
  * 스프링 시큐리티 개념
  * SW 애플리케이션에서의 보안의 의미
  * SW 보안의 중요성
  * 애플리케이션 수준의 일반적인 취약성
***
## 스프링 시큐리티
 * 개념
   * 스프링 애플리케이션에 보안을 적용하는 과정을 간소화 해주는 프레임워크
     * 인증. 권한 부여 및 공격에 대한 방어 구현을 위한 세부적인 맞춤 구성 방법 제공
   * 아파치 2.0 라이선스에 따라 릴리스되는 오픈소스 ([소스코드](https://github.com/spring-projects/spring-security))
   * 스프링 애플리케이션에서 애플리케이션 수준 보안을 구현하기 위한 사실상의 표준
 * 구성 방법
   * '스프링'의 방식으로 애플리케이션 수준 보안을 적용
     * '스프링'의 방식': 어노테이션. 빈. SpEL
 * 스프링 시큐리티의 장점
   * 보안을 위한 기능을 적은 코드로 수행할 수 있도록 함
   * 모든 구성요소를 구성 가능 → 높은 유연성
 * 스프링 시큐리티의 대안 (아파치 시로)
   * 2003년 jSecurity 프로젝트에서 시작된 보안 프레임워크
   * 인증, 권한관리, 암호화, 세션관리 등을 수행
   * 스프링 시큐리티에 비해 애플리케이션 요구 충족에 다소 가벼움
***
## 웹 애플리케이션의 보안 취약성
 * 인증과 권한 부여 취약성
   * 인증: 애플리케이션을 이용하려는 사람을 식별하는 프로세스
     * 사용자가 애플리케이션 이용 시 추가 접근 허용 전에 이들의 ID를 확인
   * 권한 부여: 인증된 호출자가 특정 기능과 데이터에 대한 이용 권리가 있는지 확인하는 프로세스
   * 인증 취약성 존재 → 사용자가 악의를 가지고 다른 사람의 기능이나 데이터에 접근
     * 스프링 시큐리티: 특정 역할이 있는 인증된 사용자만 특정 엔드포인트에 접근하도록 정의
 * 세션 고정 취약성
   * 이미 생성된 세션 ID를 재이용해 유효한 사용자 가장
   * 인증 프로세스 중에 고유한 세션 ID 발급 X → 기존 세션 ID가 재사용될 가능성이 존재
   * ex) 공격자가 세션 ID를 URL에 넣어 악성링크에 접근하도록 유인
 * XSS (교차 사이트 스크립팅)
   * 서버에 노출된 웹 서비스로 클라이언트 측 스크립트를 주입하여 다른 사용자가 이를 실행하도록 하는 공격
   * 악용 시 계정 가장(세션 고정과 결합). DDos 등의 분산 공격 참여를 야기
 * CSRF (사이트 간 요청 위조)
   * 사용자가 자신의 의지와 상관없이 공격자가 의도한 행위를 특정 웹사이트에 요청하도록 하는 공격
 * 주입 취약성
   * 공격자가 특정 데이터를 유입하는 취약성을 이용
   * 시스템의 데이터 변경.삭제.무단 이용 등을 유발
   * ex) SQL 주입. XPath 주입. OS 명령 주입 등
 * 민감한 데이터의 노출
   * 공개 정보가 아닌 것을 로그에 기록하여 민감한 데이터가 노출 (IP, 암호 등)
 * 메소드 접근 제어 부족
   * 특정 계층에만 권한 부여를 적용 → 구현 추가 시 모든 권한 부여 요구사항을 테스트 하지 않고 그대로 노출
   * 다른 계층에서도 권한 부여를 적용하여 무조건적인 권한 체크 필요로 인해 추가 기능 구현시 다른 개발자가 인지
 * 알려진 취약성이 있는 종속성
   * 기능 구현을 위해 사용되는 라이브러리나 프레임워크에 취약성 존재할 수 있음
     * [Swift 라이브러리 cocopods 취약점](https://github.com/advisories/GHSA-7627-mp87-jf6q)
     * [OpenSSL 취약점](https://www.boannews.com/media/view.asp?idx=111041)
***
## 다양한 아키텍처에 적용된 보안
 * 일체형 웹 애플리케이션 설계
 * 백엔드/프론트엔드 분리를 위한 설계
 * OAuth2 흐름
   * 작업 순서
     1) 사용자가 애플리케이션(클라이언트)에 접근
     2) 애플리케이션이 백엔드의 리소스를 호출
     3) 리소스 호출을 위한 액세스 토큰을 권한 부여 서버를 통해 획득 (이 요청을 위해 사용자 자격증명이나 갱신토큰을 보냄)
     4) 자격 증명이나 갱신토큰이 올바르면 권한 부여 서버에서 새로운 액세스 토큰을 클라이언트로 반환
     5) 필요한 리소스 호출 시 리소스 서버에 대한 요청의 헤더는 액세스 토큰 이용
   * 이점
     * 클라이언트는 사용자 자격 증명을 저장할 필요 없이 액세스 토큰과 갱신토큰만 저장
     * 사용자 자격 증명 노출 X
     * 토큰을 가로챌시 토큰을 실격시켜 사용자 자격증명을 무효화할 필요 X
     * 토큰의 수명이 제한적이므로 취약성을 악용할 기간을 제한시킬 수 있음
 * API 키, 암호화 성명, IP 검증을 이용해 요청 보안
   * 요청 및 응답 헤더에 정적 키 이용
   * 암호화 서명으로 요청 및 응답 서명
   * IP 주소에 검증 적용