# 4. 암호처리
 # PasswordEncoder
  * PasswordEncoder 정의
    ```
    public interface PasswordEncoder {
  
        String encode(CharSequence rawPassword);
        boolean matches(CharSequence rawPassword, String encodedPassword);
    
        default boolean upgradeEncoding(String encodedPassword) {
            return false;
        }
    }
    ```
    * encode: 주어진 문자열을 인자로 받아 암호를 인코딩
    * matches: 인코딩된 문자열을 원시 암호와 비교
    * upgradeEncoding: 인코딩된 암호를 다시 인코딩하여 보안 향상 (기본값은 false)
  * PasswordEncoder 구현 예시
    * 단순 구현 (암호를 문자열 그대로 저장)
      ```
      public class PlainTextPasswordEncoder
        implements PasswordEncoder {
        
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();  // 변경없이 문자열 그대로 반환
        }
      
        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.equals(encodedPassword);
        }
      }
      ```
      * SHA-512 활용
        ```
        public class Sha512PasswordEncoder
          implements PasswordEncoder {
        
          @Override
          public String encode(CharSequence rawPassword) {
              return hashWith512(rawPssword.toString());  // SHA-512 활용 암호화
          }
      
          @Override
          public boolean matches(CharSequence rawPassword, String encodedPassword) {
              String hashedPassword = encode(rawPassword);      // 비교작업을 위해 원시 암호의 암호화 필요
              return encodedPassword.equals(hashedPassword);
          }
        }      
        ```
  * PasswordEncoder에 제공된 구현
    * NoOpPasswordEncoder
      * 암호 인코딩 없이 일반 텍스트로 유지. 실제 시나리오에서는 채택 X
      * 싱글톤 패턴으로 설계되어 생성자 호출 X, NoOpPasswordEncoder.getInstance()로 인스턴스 획득
    * StandardPasswordEncoder
      * SHA-256을 활용하여 암호 인코딩
      * 해싱 알고리즘의 강도가 약하여 최근 애플리케이션에서는 채택되지 않는 방식
    * Pbkdf2PasswordEncoder
      * PBKDF2를 활용 암호 인코딩
      * 반복 횟수 인수만큼 HMAC를 수행하는 해싱함수
      * 반복횟수와 해시의 크기에 따라 강도에 영향
        * 해시의 길이가 길수록 강도 ↑
        * 반복횟수의 기본값: 185000 / 길이의 기본값: 256
      * 횟수와 크기에 따라 성능과 리소스 소비에 영향 → Trade-off를 고려하여 적절한 횟수와 길이 지정
    * BCryptPasswordEncoder
      * bcrypt 강력 해싱 함수로 암호 인코딩
      * 로그 라운드를 인수로 지정 → 반복 횟수에 영향
        * 로그라운드의 값 범위: 4~31
      * 인코딩에 활용되는 SecureRandom 인스턴스를 변경
    * SCryptPasswordEncoder
      * scrypt 해싱 함수로 암호 인코딩
      * 주요 인자
        * CPU cost
        * 메모리 cost
        * 병렬화 갯수
        * 키의 길이
        * 솔트 길이