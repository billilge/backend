# Billilge Backend

대학교 학생회 물품 대여 관리 시스템 백엔드

## 기술 스택

- **Spring Boot 3.4.1** / **Kotlin 1.9.25** / **JDK 21**
- **JPA + QueryDSL** (MySQL)
- **Spring Security** (JWT + Google OAuth2)
- **Firebase Cloud Messaging** (푸시 알림)
- **AWS S3** (이미지 업로드)
- **Apache POI** (Excel 생성)

## 빌드 & 실행

```bash
./gradlew compileKotlin   # 컴파일 확인
./gradlew build           # 전체 빌드
./gradlew bootRun         # 실행
```

## 아키텍처

```
Controller → Facade → Service → Repository
```

| 레이어 | 역할 | DTO 참조 |
|--------|------|----------|
| **Controller** | HTTP 요청/응답, `@AuthenticationPrincipal`로 인증 정보 추출 | Request/Response DTO |
| **Facade** | Request DTO 분해, 크로스 도메인 조합, Response DTO 생성 | Request/Response DTO |
| **Service** | 비즈니스 로직, 자기 도메인 Repository만 사용 | Entity/primitives만 |
| **Repository** | 데이터 접근 (JPA + QueryDSL) | Entity/Query DTO만 |

### 핵심 규칙

- **Service는 Request/Response DTO를 참조하지 않는다** — Entity, primitives, 글로벌 DTO(`PageableCondition`, `SearchCondition`)만 사용
- **Service는 타 도메인 Repository를 직접 의존하지 않는다** — 타 도메인 Service를 통해 접근 (예외: `PayerService → MemberRepository` 순환 의존 방지)
- **크로스 도메인 조합은 Facade에서 수행한다** — Facade가 여러 Service를 호출해 엔티티를 조합 후 Service에 전달
- **Facade에서 트랜잭션 필요 시 `@Transactional` 명시** — 여러 서비스 호출을 하나의 persistence context로 묶어야 할 때

### 트랜잭션 패턴

- Service 클래스에 `@Transactional(readOnly = true)` 기본 적용
- 쓰기 메서드만 `@Transactional`로 오버라이드
- JPA dirty checking 활용 — `repository.save()` 없이 엔티티 필드 변경으로 자동 반영

## 도메인 구조

```
domain/
├── item/           # 물품 관리
├── member/         # 회원, 인증
├── notification/   # 알림 (FCM 푸시)
├── payer/          # 회비 납부자 관리
└── rental/         # 대여/반납 관리
```

각 도메인 패키지 구조:
```
domain/{name}/
├── controller/     # API 컨트롤러 + Api 인터페이스 (Swagger)
├── facade/         # Facade (DTO 변환, 크로스 도메인 조합)
├── service/        # 비즈니스 로직
├── repository/     # JPA Repository + Custom(QueryDSL)
├── entity/         # JPA Entity
├── dto/            # request/, response/
├── enums/          # 도메인 열거형
└── exception/      # 도메인 에러 코드
```

## 서비스 의존성

```
ItemService        → ItemRepository, S3Service
MemberService      → MemberRepository, TokenProvider, PayerService
NotificationService → NotificationRepository, FCMService, MemberService
PayerService       → PayerRepository, MemberRepository, ExcelGenerator
RentalService      → RentalRepository, NotificationService
```

## 대여 상태 머신

```
[사용자 신청]  PENDING → CONFIRMED → RENTAL → RETURN_PENDING → RETURN_CONFIRMED → RETURNED
                      → REJECTED
               PENDING → CANCEL (사용자 취소)

[관리자 생성]  대여물품: → RENTAL (바로 대여중)
              소모품:   → RETURNED (즉시 반납 처리)
```

- **CONFIRMED**: 재고 차감, 담당자(worker) 배정
- **RETURNED**: 재고 복원 (소모품 제외)
- **소모품(CONSUMPTION)**: RENTAL 상태 요청 시 자동으로 RETURNED 처리

## 대여 비즈니스 규칙

- 회비 납부자(`isFeePaid`)만 대여 가능
- 동일 물품 중복 대여 불가 (`ignoreDuplicate`로 우회 가능)
- 시험 기간 대여 불가 (`exam-period.start-date` / `end-date`)
- 주말 대여 불가
- 과거 시간 대여 불가
- 10시~17시만 대여 가능
- Dev 모드(`/rentals/dev`): 시간 검증 생략, ADMIN 역할 필요

## API 엔드포인트

### 인증 (Public)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/auth/sign-up` | 회원가입 |
| POST | `/auth/admin-login` | 관리자 로그인 |

### 물품 (Public)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/items` | 물품 검색 |

### 회원 (JWT 필요)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/members/me/fcm-token` | FCM 토큰 등록 |

### 알림 (JWT 필요)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/notifications` | 알림 목록 |
| GET | `/notifications/count` | 안읽은 알림 수 |
| PATCH | `/notifications/{id}` | 알림 읽음 |
| PATCH | `/notifications/all` | 전체 읽음 |

### 대여 (JWT 필요)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/rentals` | 대여 신청 |
| POST | `/rentals/dev` | 개발용 대여 (시간 검증 생략) |
| GET | `/rentals` | 대여 이력 조회 |
| PATCH | `/rentals/{id}` | 대여 취소 |
| PATCH | `/rentals/return/{id}` | 반납 신청 |
| GET | `/rentals/return-required` | 반납 필요 목록 |

### 관리자 (JWT + @OnlyAdmin)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/admin/items` | 물품 목록 (대여자 수 포함) |
| POST | `/admin/items` | 물품 추가 |
| PUT | `/admin/items/{id}` | 물품 수정 |
| GET | `/admin/items/{id}` | 물품 상세 |
| DELETE | `/admin/items/{id}` | 물품 삭제 |
| GET | `/admin/members` | 회원 목록 |
| GET | `/admin/members/admins` | 관리자 목록 |
| POST | `/admin/members/admins` | 관리자 등록 |
| DELETE | `/admin/members/admins` | 관리자 해제 |
| GET | `/admin/members/payers` | 납부자 목록 |
| POST | `/admin/members/payers` | 납부자 등록 |
| DELETE | `/admin/members/payers` | 납부자 삭제 |
| GET | `/admin/members/payers/excel` | 납부자 엑셀 다운로드 |
| GET | `/admin/notifications` | 관리자 알림 |
| GET | `/admin/rentals/dashboard` | 대시보드 |
| GET | `/admin/rentals` | 대여 이력 |
| PATCH | `/admin/rentals/{id}` | 대여 상태 변경 |
| POST | `/admin/rentals` | 관리자 대여 생성 |
| DELETE | `/admin/rentals/{id}` | 대여 이력 삭제 |

## Global 패키지

```
global/
├── annotation/     # @OnlyAdmin
├── config/         # SecurityConfig, CorsConfig, SwaggerConfig, QueryDslConfig, AsyncConfig
├── dto/            # PageableCondition, SearchCondition, PageableResponse
├── exception/      # ApiException, ErrorCode, GlobalExceptionHandler
├── external/
│   ├── fcm/        # FCMConfig, FCMService
│   └── s3/         # S3Config, S3Service
├── logging/        # LoggingFilter
├── security/
│   ├── jwt/        # TokenProvider, TokenAuthenticationFilter
│   └── oauth2/     # Google OAuth2 핸들러, UserAuthInfo
└── utils/          # DateUtils(isWeekend), ExcelGenerator
```
