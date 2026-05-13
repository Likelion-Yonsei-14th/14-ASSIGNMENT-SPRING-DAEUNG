# 1. 개념 조사 과제

아래 질문에 대해 각 항목당 3~5줄 정도로 정리해주세요.

## 1️⃣ 인증과 인가의 차이

아래 두 개념의 차이를 정리해주세요.

| 개념 | 의미 |
| --- | --- |
| 인증 Authentication | 사용자가 누구인지 확인하는 과정 |
| 인가 Authorization | 해당 사용자가 특정 기능을 사용할 권한이 있는지 확인하는 과정 |

정리할 때 아래 예시를 반드시 포함해주세요.

- 로그인은 인증에 해당한다.
- 상품 수정/삭제 권한 검사는 인가에 해당한다.
- 로그인했다고 해서 모든 상품을 수정할 수 있는 것은 아니다.

## A.

인증(Authentication) 은 사용자가 누구인지 확인하는 과정이고, 인가(Authorization) 는 해당 사용자에게 특정 작업을 수행할 권한이 있는지 확인하는 과정.

로그인은 이메일/비밀번호를 통해 당신이 맞다고(사용자) 확인하므로 인증에 해당한다.

상품 수정/삭제 권한 검사는 로그인한 사용자가 해당 상품의 소유자인지(권한이 있는지) 확인하는 것이므로 인가에 해당한다.

로그인에 성공했더라도(인증 통과), 본인이 등록하지 않은 상품은 수정할 수 없으므로 인증과 인가는 별개의 단계이다.

---

## 2️⃣ 비밀번호를 그대로 저장하면 안 되는 이유

회원가입 기능에서는 사용자의 비밀번호를 DB에 저장하게 됩니다. 이때 비밀번호를 평문으로 저장하면 어떤 문제가 생기는지 조사해주세요.

정리할 키워드:

- 평문 저장
- 비밀번호 암호화
- BCrypt
- PasswordEncoder

## A.

비밀번호를 평문(plain text)으로 DB에 저장하면, DB가 해킹당하거나 내부 관리자가 DB를 조회했을 때 모든 사용자의 비밀번호가 즉시 노출된다. 특히 많은 사용자가 여러 서비스에서 동일한 비밀번호를 사용하므로 피해가 연쇄적으로 발생할 수 있다. 이를 방지하기 위해 비밀번호 암호화를 사용한다.

특히 BCrypt 는 단순 해시(MD5, SHA-1)와 달리 salt를 자동으로 추가하고 연산 비용을 조절할 수 있어 레인보우 테이블 공격과 브루트포스 공격에 강하다. 단방향 해시 알고리즘으로, 암호화 후 복호화 할 수 없다. Salt는 함수 암호화 시 본래 데이터에 추가적으로 랜덤한 데이터를 더하는 방식을 뜻하며, 이를 통해 원래 데이터의 해시값에서 달라진다(위에서 언급한 단순 해시는 해시 함수의 빠른 처리 때문에 공격자에게 유리하게 작용함)

Spring에서는 PasswordEncoder 인터페이스를 사용하며, BCryptPasswordEncoder를 빈으로 등록해 .encode(rawPassword)로 암호화하고 .matches(rawPassword, encodedPassword)로 검증한다. 이렇게 하면 원본 비밀번호를 복원할 수 없어 보안이 강화된다.

예시 코드를 살펴보자.

```
// PasswordEncoder bean 등록
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

```
// 회원가입 - 비밀번호 암호화 후 저장
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String email, String rawPassword) {
        // 평문 비밀번호 → BCrypt 해시로 변환
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Member member = new Member(email, encodedPassword);
        memberRepository.save(member);
    }
}
```

```
// 로그인
public String login(String email, String rawPassword) {
    // 1. 이메일로 사용자 조회
    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

    // 2. 입력된 비밀번호 vs DB에 저장된 해시 비교
    if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    // 3. 인증 성공 → JWT 발급 또는 세션 생성
    return "로그인 성공";
}
```

회원가입에서 encode() 호출 시마다 내부적으로 랜덤 salt가 생성되어 같은 비밀번호라도 매번 다른 해시값이 나온다.

DB에는 $2a$10$... 형태의 해시 문자열이 저장된다.

로그인에서는 내부적으로 입력값을 해시한 뒤 salt까지 포함해 비교한다. 복호화가 아니라 재해시 비교이므로 원본 비밀번호를 DB에서 꺼낼 수 없다. 즉 해시값의 비교이다.
---

## 3️⃣ 세션 로그인과 JWT 로그인 차이

7주차에서는 로그인 방식을 다루게 됩니다. 아래 두 방식의 차이를 간단히 조사해주세요.

| 방식 | 특징 |
| --- | --- |
| Session Login | 서버가 로그인 상태를 기억하는 방식 |
| JWT Login | 클라이언트가 토큰을 가지고 요청하는 방식 |

정리할 때 아래 질문에 답해주세요.

- 로그인 후 서버가 사용자를 기억하는 방식인가?
- 요청마다 토큰을 보내야 하는 방식인가?
- 쇼핑몰 API에서 로그인 사용자를 어떻게 구분할 수 있는가?

## A.

| 질문                  | Session Login            | JWT Login                                    |
| ------------------- | ------------------------ | -------------------------------------------- |
| 서버가 사용자를 기억하는가?     | ✅ 서버 메모리/DB에 세션 저장       | ❌ 서버는 상태를 저장하지 않음 (Stateless)                |
| 요청마다 토큰을 보내야 하는가?   | ❌ 쿠키에 세션 ID만 전송          | ✅ 매 요청마다 Authorization: Bearer <token> 헤더 전송 |
| 쇼핑몰 API에서 사용자 구분 방법 | 세션 ID로 서버 세션 조회 → 사용자 식별 | JWT 토큰 서명 검증 후 payload에서 userId 추출           |

세션 방식은 서버에 상태가 남으므로 서버 확장(스케일 아웃) 시 세션 공유 문제가 발생할 수 있다. 즉 서버 A에 로그인했지만, 서버 B에 라우팅 되는 요청을 보내면 서버 B는 로그인이 안 된 것으로 처리되어 로그인이 풀려버리는 것과 같다.

JWT 방식은 토큰을 발급하면, 그 토큰까지 같이 서버 B로 보낸다. 따라서 서버가 무상태(Stateless)를 유지할 수 있어 MSA(기능별 서버 분리), 다중 서버 환경에 적합하다.

---

# 2. 기존 상품 CRUD 코드 분석 과제

6주차 복습 과제에서 작성한 상품 CRUD 코드를 열고, 아래 질문에 답해주세요.

## 1️⃣ 현재 상품 등록 방식 분석

현재 `ProductCreateRequest`에는 `sellerId`가 들어갑니다.

```
privateLongsellerId;
privateStringname;
privateStringdescription;
privateIntegerprice;
```

아래 질문을 같이 생각해주세요!

1. 현재 상품 등록 API에서 `sellerId`는 어디에서 전달되는가?
2. 사용자가 다른 사람의 `sellerId`를 넣으면 어떤 문제가 생길 수 있는가?
3. 로그인 기능이 생기면 `sellerId`를 요청 Body에서 제거해야 하는 이유는 무엇인가?

## A.

1. sellerId는 어디에서 전달되는가?

현재는 클라이언트가 요청 Body에 직접 포함해서 전달한다. 즉, 프론트엔드 혹은 API 호출자가 sellerId 값을 임의로 입력하는 구조다.

2. 다른 사람의 sellerId를 넣으면 어떤 문제가 생기는가?

악의적인 사용자가 타인의 sellerId를 입력해 다른 사람 명의로 상품을 등록할 수 있다. 서버가 요청자의 신원을 검증하지 않으므로 완전한 위조가 가능하다.

3. 로그인 기능이 생기면 sellerId를 Body에서 제거해야 하는 이유는?

로그인 후에는 서버가 세션 또는 JWT 토큰으로 요청자를 신뢰할 수 있게 식별할 수 있다. 따라서 클라이언트가 직접 sellerId를 전달하게 두면 위변조 가능성이 생기므로, 서버가 인증 정보에서 안전하게 sellerId를 추출해야 한다.

---

## 2️⃣ 2주차 이후 변경될 상품 등록 Request 예상하기

1주차 상품 등록 Request는 아래와 같습니다.

```
{
  "sellerId":1,
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

2주차 이후에는 아래처럼 변경됩니다.

```
{
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

아래 질문도 같이 생각해 주세요.

1. `sellerId`가 사라진 이유는 무엇인가?
2. 서버는 상품 등록자를 어디에서 가져와야 하는가?
3. 로그인하지 않은 사용자가 상품 등록을 요청하면 어떤 에러가 발생해야 하는가?

## A.

1. sellerId가 사라진 이유는?

로그인된 사용자의 정보를 서버가 직접 인증 토큰/세션에서 추출할 수 있기 때문에, 클라이언트가 Body로 전달할 필요가 없어진다.

2. 서버는 상품 등록자를 어디에서 가져와야 하는가?

JWT 방식이라면 요청 헤더의 Authorization 토큰을 파싱해 payload에서 userId를 추출하고, 세션 방식이라면 서버 세션에서 로그인된 사용자 정보를 꺼내 사용한다.

3. 로그인하지 않은 사용자가 상품 등록을 요청하면 어떤 에러가 발생해야 하는가?

401 Unauthorized 에러가 반환되어야 한다. 인증이 되지 않은 상태에서 인증이 필요한 리소스에 접근했음을 의미한다.