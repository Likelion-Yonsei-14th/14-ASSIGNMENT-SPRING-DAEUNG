# 📌 7주차 복습 과제 : shop-app 인증/인가 구현

이번 복습 과제의 목표는 6주차 복습 과제에서 구현한 **쇼핑몰 상품 CRUD API**에 인증/인가 기능을 추가하는 것입니다.

1차시 과제에서는 상품 등록 시 `sellerId`를 직접 전달했습니다.

이번 과제에서는 회원가입과 로그인을 구현한 뒤, 상품 등록자를 `sellerId`로 직접 받지 않고 **로그인한 사용자로 자동 지정**하도록 수정합니다.

이번 과제를 통해 아래 내용을 복습합니다.

- 회원가입
- 로그인
- 로그아웃
- 세션 기반 로그인
- JWT 기반 로그인
- 로그인 사용자 조회
- 상품 등록자 자동 지정
- 상품 수정/삭제 권한 검사
- 401, 403 예외처리

---

# 0. 시작 전 확인

이번 과제는 1차시 복습 과제 코드가 완성되어 있다는 전제로 진행합니다.

현재 프로젝트에는 아래 기능이 구현되어 있어야 합니다.

- 상품 생성
- 상품 전체 조회
- 상품 단건 조회
- 상품 수정
- 상품 삭제
- 예외처리
- Swagger 설정
- 테스트용 Member 생성

현재 상품 생성 Request는 아래 형태입니다.

```
{
  "sellerId":1,
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

이번 과제에서는 위 구조에서 `sellerId`를 제거하고, 로그인한 사용자를 상품 등록자로 지정하도록 수정합니다.

---

# 1. Member Entity 확장

## 1️⃣ Member 필드 추가

기존 `Member` Entity에 아래 필드를 추가해주세요.

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `email` | `String` | 로그인에 사용할 이메일 |
| `password` | `String` | 암호화된 비밀번호 |
| `role` | `String` 또는 `Enum` | 사용자 권한 |

기존 1차시 Member 필드

2차시 이후 Member 필드

| 필드 | 타입 |
| --- | --- |
| `id` | `Long` |
| `nickname` | `String` |
| `createdAt` | `LocalDateTime` |
| `updatedAt` | `LocalDateTime` |

| 필드 | 타입 |
| --- | --- |
| `id` | `Long` |
| `email` | `String` |
| `password` | `String` |
| `nickname` | `String` |
| `role` | `String` 또는 `Enum` |
| `createdAt` | `LocalDateTime` |
| `updatedAt` | `LocalDateTime` |

### 구현 조건

- `email`은 중복될 수 없습니다.
- `password`는 반드시 암호화해서 저장합니다.
- `role`은 기본값으로 `USER`를 사용합니다.
- 1차시에서 사용하던 `nickname`은 그대로 유지합니다.

---

# 2. DTO 설계

## 1️⃣ SignupRequest

회원가입 요청 DTO를 생성해주세요.

요청 예시

| 필드 | 타입 |
| --- | --- |
| `email` | `String` |
| `password` | `String` |
| `nickname` | `String` |

```
{
  "email":"lion@example.com",
  "password":"1234",
  "nickname":"멋쟁이"
}
```

---

## 2️⃣ LoginRequest

로그인 요청 DTO를 생성해주세요.

요청 예시

| 필드 | 타입 |
| --- | --- |
| `email` | `String` |
| `password` | `String` |

```
{
  "email":"lion@example.com",
  "password":"1234"
}
```

---

## 3️⃣ LoginResponse

JWT 로그인 성공 응답 DTO를 생성해주세요.

응답 예시

| 필드 | 타입 |
| --- | --- |
| `accessToken` | `String` |
| `tokenType` | `String` |
| `memberId` | `Long` |
| `nickname` | `String` |

```
{
  "accessToken":"eyJhbGciOiJIUzI1NiJ9...",
  "tokenType":"Bearer",
  "memberId":1,
  "nickname":"멋쟁이"
}
```

---

## 4️⃣ MemberResponse

내 정보 조회 응답 DTO를 생성해주세요.

응답 예시

| 필드 | 타입 |
| --- | --- |
| `id` | `Long` |
| `email` | `String` |
| `nickname` | `String` |

```
{
  "id":1,
  "email":"lion@example.com",
  "nickname":"멋쟁이"
}
```

---

## 5️⃣ ProductCreateRequest 수정

1차시에서는 상품 생성 시 `sellerId`를 직접 받았습니다.

```
{
  "sellerId":1,
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

2차시에서는 `sellerId`를 제거해주세요.

```
{
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

수정 후 `ProductCreateRequest` 필드

| 필드 | 타입 |
| --- | --- |
| `name` | `String` |
| `description` | `String` |
| `price` | `Integer` |

---

# 3. API 목록

이번 과제에서 새로 구현하거나 수정해야 하는 API는 다음과 같습니다.

## 신규 API

## 수정 대상 API

| Method | URL | 설명 |
| --- | --- | --- |
| `POST` | `/api/auth/signup` | 회원가입 |
| `POST` | `/api/auth/session/login` | 세션 로그인 |
| `POST` | `/api/auth/session/logout` | 세션 로그아웃 |
| `POST` | `/api/auth/jwt/login` | JWT 로그인 |
| `GET` | `/api/members/me` | 내 정보 조회 |

| Method | URL | 수정 내용 |
| --- | --- | --- |
| `POST` | `/api/products` | `sellerId` 제거, 로그인 사용자로 등록자 지정 |
| `PATCH` | `/api/products/{productId}` | 등록자 본인만 수정 가능 |
| `DELETE` | `/api/products/{productId}` | 등록자 본인만 삭제 가능 |

---

# 4. 회원가입 구현

## 1️⃣ API

```
POST /api/auth/signup
```

## 2️⃣ Request Body

```
{
  "email":"lion@example.com",
  "password":"1234",
  "nickname":"멋쟁이"
}
```

## 3️⃣ Response Body

```
{
  "id":1,
  "email":"lion@example.com",
  "nickname":"멋쟁이"
}
```

## 4️⃣ 구현 조건

- 이메일이 중복되면 회원가입이 실패해야 합니다.
- 비밀번호는 암호화해서 저장해야 합니다.
- 회원가입 성공 시 MemberResponse를 반환합니다.
- 기본 role은 `USER`로 저장합니다.

---

# 5. 세션 로그인 구현

## 1️⃣ API

```
POST /api/auth/session/login
```

## 2️⃣ Request Body

```
{
  "email":"lion@example.com",
  "password":"1234"
}
```

## 3️⃣ Response Body

```
{
  "id":1,
  "email":"lion@example.com",
  "nickname":"멋쟁이"
}
```

## 4️⃣ 구현 조건

- 이메일과 비밀번호가 일치하면 로그인 성공입니다.
- 로그인 성공 시 세션에 로그인한 회원 id를 저장합니다.
- 세션 key는 자유롭게 정해도 되지만, 예를 들어 `LOGIN_MEMBER_ID`를 사용할 수 있습니다.
- 이메일 또는 비밀번호가 틀리면 로그인 실패 예외를 발생시켜야 합니다.

---

# 6. 세션 로그아웃 구현

## 1️⃣ API

```
POST /api/auth/session/logout
```

## 2️⃣ Response Body

```
{
  "message":"로그아웃되었습니다."
}
```

## 3️⃣ 구현 조건

- 현재 세션을 무효화합니다.
- 로그아웃 후에는 세션 기반 내 정보 조회가 실패해야 합니다.

---

# 7. JWT 로그인 구현

## 1️⃣ API

```
POST /api/auth/jwt/login
```

## 2️⃣ Request Body

```
{
  "email":"lion@example.com",
  "password":"1234"
}
```

## 3️⃣ Response Body

```
{
  "accessToken":"eyJhbGciOiJIUzI1NiJ9...",
  "tokenType":"Bearer",
  "memberId":1,
  "nickname":"멋쟁이"
}
```

## 4️⃣ 구현 조건

- 이메일과 비밀번호가 일치하면 JWT access token을 발급합니다.
- `tokenType`은 `"Bearer"`로 반환합니다.
- JWT에는 최소한 `memberId`가 포함되어야 합니다.
- 잘못된 토큰이면 `INVALID_TOKEN` 예외를 발생시킵니다.
- 만료된 토큰이면 `EXPIRED_TOKEN` 예외를 발생시킵니다.

---

# 8. 내 정보 조회 구현

## 1️⃣ API

```
GET /api/members/me
```

## 2️⃣ 인증 방식

아래 두 방식 중 하나 이상으로 로그인 사용자를 조회할 수 있어야 합니다.

### 세션 방식

세션에 저장된 `memberId`를 사용합니다.

### JWT 방식

Authorization Header의 Bearer Token을 사용합니다.

```
Authorization: Bearer {accessToken}
```

## 3️⃣ Response Body

```
{
  "id":1,
  "email":"lion@example.com",
  "nickname":"멋쟁이"
}
```

## 4️⃣ 구현 조건

- 로그인하지 않은 사용자는 내 정보를 조회할 수 없습니다.
- 로그인하지 않은 경우 `UNAUTHORIZED` 예외를 발생시켜야 합니다.
- 로그인된 사용자라면 MemberResponse를 반환합니다.

---

# 9. 상품 생성 API 수정

## 1️⃣ 기존 방식

1차시에서는 상품 등록자를 직접 전달했습니다.

```
{
  "sellerId":1,
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

## 2️⃣ 수정 방식

2차시에서는 `sellerId`를 받지 않습니다.

```
{
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

## 3️⃣ 구현 조건

- 상품 등록은 로그인한 사용자만 할 수 있습니다.
- 상품 등록자는 request의 `sellerId`가 아니라 로그인한 사용자입니다.
- 로그인하지 않은 사용자가 상품 생성을 요청하면 `UNAUTHORIZED` 예외를 발생시킵니다.

---

# 10. 상품 수정 권한 검사

## 1️⃣ API

```
PATCH /api/products/{productId}
```

## 2️⃣ Request Body

```
{
  "name":"수정된 후드티",
  "description":"수정된 상품 설명입니다.",
  "price":42000
}
```

## 3️⃣ 구현 조건

- 로그인한 사용자만 상품을 수정할 수 있습니다.
- 상품 등록자 본인만 수정할 수 있습니다.
- 상품 등록자가 아닌 사용자가 수정하려고 하면 `FORBIDDEN` 예외를 발생시켜야 합니다.
- 상품이 존재하지 않으면 기존처럼 `PRODUCT_NOT_FOUND` 예외를 발생시킵니다.

---

# 11. 상품 삭제 권한 검사

## 1️⃣ API

```
DELETE /api/products/{productId}
```

## 2️⃣ Response Body

```
{
  "message":"상품이 삭제되었습니다."
}
```

## 3️⃣ 구현 조건

- 로그인한 사용자만 상품을 삭제할 수 있습니다.
- 상품 등록자 본인만 삭제할 수 있습니다.
- 상품 등록자가 아닌 사용자가 삭제하려고 하면 `FORBIDDEN` 예외를 발생시켜야 합니다.
- 상품이 존재하지 않으면 기존처럼 `PRODUCT_NOT_FOUND` 예외를 발생시킵니다.

---

# 12. 예외처리

아래 상황에 대해 예외처리를 구현해주세요.

| 상황 | Error Code | Status |
| --- | --- | --- |
| 로그인 필요 | `UNAUTHORIZED` | 401 |
| 권한 없음 | `FORBIDDEN` | 403 |
| 이메일 중복 | `DUPLICATE_EMAIL` | 400 |
| 로그인 실패 | `LOGIN_FAILED` | 401 |
| 유효하지 않은 토큰 | `INVALID_TOKEN` | 401 |
| 만료된 토큰 | `EXPIRED_TOKEN` | 401 |
| 상품을 찾을 수 없음 | `PRODUCT_NOT_FOUND` | 404 |
| 서버 오류 | `INTERNAL_SERVER_ERROR` | 500 |

에러 응답 형식은 1차시와 동일하게 유지합니다.

```
{
  "code":"UNAUTHORIZED",
  "message":"로그인이 필요합니다."
}
```

---

# 13. 구현 힌트

## 1️⃣ MemberRepository

아래 메서드를 추가하면 회원가입과 로그인 구현에 사용할 수 있습니다.

```
booleanexistsByEmail(Stringemail);
Optional<Member>findByEmail(String email);
```

---

## 2️⃣ 비밀번호 암호화

비밀번호는 평문으로 저장하지 않습니다.

예시로 `BCryptPasswordEncoder`를 사용할 수 있습니다.

```
BCryptPasswordEncoderpasswordEncoder=newBCryptPasswordEncoder();
```

회원가입 시

```
StringencodedPassword=passwordEncoder.encode(request.getPassword());
```

로그인 시

```
passwordEncoder.matches(request.getPassword(),member.getPassword());
```

---

## 3️⃣ 세션 로그인

세션 로그인은 `HttpSession`을 사용할 수 있습니다.

로그인 성공 시

```
session.setAttribute("LOGIN_MEMBER_ID",member.getId());
```

로그아웃 시

```
session.invalidate();
```

---

## 4️⃣ JWT 인증

JWT 로그인은 다음 흐름으로 구현합니다.

```
이메일/비밀번호 확인
→ 로그인 성공
→ accessToken 생성
→ 클라이언트에게 반환
→ 이후 요청에서 Authorization Header로 토큰 전달
→ 서버가 토큰을 검증하고 memberId 추출
```

---

## 5️⃣ 상품 등록자 자동 지정

1차시 코드에서는 아래와 같은 흐름이었습니다.

```
request.getSellerId()
→ memberService.findMemberById()
→ Product.create(member, ...)
```

2차시에서는 아래처럼 바꿔야 합니다.

```
로그인 사용자 조회
→ Product.create(loginMember, ...)
```

---

## 6️⃣ 상품 수정/삭제 권한 검사

상품의 등록자 id와 로그인 사용자 id를 비교합니다.

```
if (!product.getMember().getId().equals(loginMember.getId())) {
thrownewCustomException(ErrorCode.FORBIDDEN);
}
```

---

# 14. 과제 제출

1. 멋사 깃허브에 `15-ASSIGNMENT-SPRING-AUTH-{이름}` 레포를 만든 후 제출해주세요. (→ 1차시 과제 코드를 기반으로 이어서 구현해주세요.)
2. 1차시 과제 코드를 기반으로 이어서 구현해주세요.
3. 아래 항목에 대한 Swagger 테스트 캡처를 첨부해주세요.

---

## 제출 캡처 목록

- 회원가입
- 세션 로그인
- 세션 로그아웃
- JWT 로그인
- 내 정보 조회
- 로그인 사용자로 상품 등록
- 본인 상품 수정 성공
- 본인 상품 삭제 성공
- 로그인하지 않고 상품 등록 시도
- 다른 사용자의 상품 수정 시도
- 다른 사용자의 상품 삭제 시도

---

# 15. 테스트 시나리오 예시

## 1️⃣ 회원가입

첫 번째 사용자

```
{
  "email":"lion1@example.com",
  "password":"1234",
  "nickname":"멋쟁이"
}
```

두 번째 사용자

```
{
  "email":"lion2@example.com",
  "password":"1234",
  "nickname":"사자"
}
```

---

## 2️⃣ 로그인 후 상품 등록

`lion1@example.com`으로 로그인한 뒤 상품을 등록합니다.

```
{
  "name":"멋사 후드티",
  "description":"멋쟁이사자처럼 로고가 들어간 후드티입니다.",
  "price":39000
}
```

응답에서 `sellerId`가 첫 번째 사용자 id인지 확인해주세요.

---

## 3️⃣ 본인 상품 수정 성공

첫 번째 사용자가 자신이 등록한 상품을 수정합니다.

```
{
  "name":"수정된 후드티",
  "description":"수정된 상품 설명입니다.",
  "price":42000
}
```

---

## 4️⃣ 다른 사용자로 상품 수정 실패

두 번째 사용자로 로그인한 뒤, 첫 번째 사용자가 등록한 상품을 수정해보세요.

기대 응답

```
{
  "code":"FORBIDDEN",
  "message":"권한이 없습니다."
}
```

---

## 5️⃣ 로그인하지 않고 상품 등록 실패

로그인하지 않은 상태에서 상품 등록을 요청해보세요.

기대 응답

```
{
  "code":"UNAUTHORIZED",
  "message":"로그인이 필요합니다."
}
```

---

# 16. 완료 기준

아래 조건을 모두 만족하면 과제가 완료된 것으로 봅니다.

- 회원가입이 정상 동작한다.
- 세션 로그인이 정상 동작한다.
- 세션 로그아웃이 정상 동작한다.
- JWT 로그인이 정상 동작한다.
- 로그인 사용자 정보를 조회할 수 있다.
- 상품 생성 시 `sellerId`를 request로 받지 않는다.
- 상품 등록자가 로그인 사용자로 자동 지정된다.
- 상품 수정은 등록자 본인만 가능하다.
- 상품 삭제는 등록자 본인만 가능하다.
- 인증/인가 예외 응답이 `code`, `message` 형식으로 통일되어 있다.
- Swagger에서 모든 API 테스트가 가능하다.


---

# 📌 8주차 예습 과제

이번 예습 과제의 목표는 8주차 세션에서 다루게 될 개념인 트랜잭션에 대한 기본적인 이해를 습득하는 것입니다.

아래에 제시된 과제들을 수행하며, 8주차 세션에서는 어떤 내용을 알아보게 될 지, 미리 체험해보시기 바랍니다.

---

# 1. 개념 조사 과제

다음 세 가지 질문에 대해 자유롭게 조사해오세요.

## 1️⃣ 트랜잭션이란 무엇인가? 그리고 ACID란 무엇인가?

정리할 때 아래 질문에 답해주세요.

- 트랜잭션에서 commit과 rollback은 각각 언제 일어나는가?
- 하나의 트랜잭션 안에 DB 작업이 여러 개 있을 때, 그 중 하나만 실패하면 어떻게 처리되는가?

---

## 2️⃣ `@Transactional`을 붙이면 스프링 내부에서 어떤 일이 일어나는가?

정리할 때 아래 질문에 답해주세요.

- `@Transactional`이 없는 서비스 메서드에서 DB 작업이 3개 일어난다면, 2번째에서 예외가 터졌을 때 어떻게 되는가?
- `@Transactional`이 있다면 같은 상황에서 어떻게 달라지는가?
- `@Transactional`은 어떤 예외가 발생했을 때 rollback을 하는가? (힌트: Checked / Unchecked Exception)

---

## 3️⃣ 트랜잭션 전파(Propagation)와 격리 수준(Isolation Level)은 왜 필요한가?

정리할 때 아래 질문에 답해주세요.

- 트랜잭션 전파(Propagation)와 격리 수준(Isolation Level)이라는 개념이 존재한다. 각각 어떤 문제를 해결하기 위해 생겨난 개념인가?

---

# 2. 나만의 트랜잭션 시나리오 구성하기

주변에서 찾아볼 수 있는 서비스에서 **트랜잭션이 반드시 필요한 상황**을 하나 골라, 아래 형식에 맞춰 정리해오세요.

## 작성 형식

```
1. 시나리오 설명
   - 어떤 서비스인지, 어떤 기능인지 한 줄로 설명

2. 관련된 DB 작업 목록
   - 이 기능에서 DB에 일어나는 작업을 순서대로 나열
   - 예) ① users 테이블에서 잔액 차감 → ② orders 테이블에 주문 생성 → ...

3. 트랜잭션이 없다면?
   - ②번 작업 도중 서버가 죽는다면 어떤 상태가 되는지 구체적으로 설명

4. 트랜잭션이 있다면?
   - 같은 상황에서 어떻게 복구되는지 설명
```

> 정답이 있는 과제가 아닙니다. 본인의 서비스에 맞게 자유롭게 구성해오면 되고, 세션에서 같이 검토할 예정이에요.
> 

---

