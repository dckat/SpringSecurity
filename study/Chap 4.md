# 4. 암호처리
 ## PasswordEncoder
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
  * DelegatingPasswordEncoder
    * 암호의 접두사를 기준으로 PasswordEncoder 구현에 작업 위임
      ![delegatingpasswordencoder](https://github.com/dckat/SpringSecurity/assets/19167273/4cdc577f-2c0d-45f4-9828-7c70e80d2fa0)
      * BCryptPasswordEncoder, SCryptPasswordEncoder는 다른 접두사 인코더를 위한 참조로 가짐
      * matches 메소드 호출 시 지정된 PasswordEncoder에 위임
***
 ## 스프링 시큐리티 암호화 모듈
   * 키 생성기
     * 특정한 종류의 키를 생성하는 객체. 암호화나 해싱 알고리즘에 필요
     * 종류
       1) StringKeyGenerator
          * 문자열 키 생성기를 생성
          * 일반적으로 해싱 또는 암호화 알고리즘의 솔트값으로 이용
          * 8바이트의 키를 생성하여 16진수의 문자열로 인코딩
          * 정의
          ```
          public interface StringKeyGenerator {
          
            String generateKey();
          
          }
          ```
       2) BytesKeyGenerator
          * 정의
          ```
          public interface BytesKeyGenerator {
          
            int getKeyLength();    // 키 길이를 반환하는 메소드가 추가 존재
            byte[] generateKey();
          
          }          
          ```
          * 기본적으로 8바이트 길이의 키를 생성
            * KeyGenerators.secureRandom() 메소드에 원하는 값을 넣어 키 길이를 지정
          * KeyGenerators.shared() 메소드를 통해 같은 키 반환
            ```
            BytesKeyGenerator keyGenerator = KeyGenerators.shared(16);
            
            // key1과 key2가 같은 키 값을 가짐
            byte[] key1 = keyGenerator.generateKey();
            byte[] key2 = keyGenerator.geterateKey();
            ```
   * 암호화.복호화 작업에 암호기 이용
     1) TextEncryptor
        * 데이터를 문자열로 관리. 문자열을 입력으로 받고 출력으로 반환
        * 정의
          ```
          public interface TextEncryptor {
          
            String encrypt(String text);
            String decrypt(String encryptedText);
          
          }          
          ```
     2) BytesEncryptor
        * 데이터를 바이트 배열로 관리
        * 내부적으로 256바이트 AES를 이용하여 입력을 암호화
        * 정의
        ```
        public interface BytesEncryptor {
            byte[] encrypt(byte[] byteArray);
            byte[] decrypt(byte[] encryptedArray);
        }          
        ```
     * TextEncryptors의 주요 형식
       * Encryptors.text(): standard() 메소드로 암호화 작업 관리
       * Encryptors.delux(): stronger() 인스턴스를 활용
       * Encryptors.queryableText(): 순차 암호화 작업에서 입력이 같으면 같은 출력이 나오도록 보장
       * Encryptors.noOpText(): 암호화하지 않는 더미 암호기 반환
***
