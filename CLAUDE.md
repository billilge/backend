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
ItemService            → ItemRepository, S3Service
MemberService          → MemberRepository, TokenProvider, PayerService
NotificationService           → NotificationRepository, NotificationPushOutboxService
NotificationPushOutboxService → NotificationPushOutboxRepository
PushNotificationSender        → FCMService, MemberService, NotificationPushOutboxService
PayerService                  → PayerRepository, MemberRepository, ExcelGenerator
RentalService                 → RentalRepository, ApplicationEventPublisher
```

### 알림 발송 구조

`RentalService`가 이벤트를 발행하면 `NotificationEventHandler`가 커밋 이후 비동기로 받아 처리한다.

```
RentalService → 이벤트 발행
  └ NotificationEventHandler (@Async + AFTER_COMMIT, 트랜잭션 없음)
      ├ NotificationService.createNotification()   # 알림 + 아웃박스 저장 (한 트랜잭션, 즉시 커밋)
      └ PushNotificationSender.dispatch()          # 트랜잭션 밖에서 FCM 호출

PushRetryScheduler (@Scheduled, 30초 간격)
  └ PushNotificationSender.dispatch()              # 미발송 건 재시도 (같은 경로)
```

- **푸시 발송은 트랜잭션 밖에서 수행한다** — 네트워크 I/O가 DB 커넥션을 점유하지 않도록, 그리고 푸시 실패가 저장된 알림을 롤백시키지 않도록 분리
- **`PushNotificationSender`는 예외를 전파하지 않는다** — 한 수신자의 실패가 다른 수신자에게 영향을 주면 안 됨
- **FCM 실패는 `PushResult`로 구분한다** — `InvalidToken`(토큰 제거) / `Retryable`(재시도 대상) / `Permanent`(재시도 무의미)
- **`@Async`는 알림 전용 실행기(`notificationTaskExecutor`)를 사용한다** — `AsyncConfig`에 정의

### 푸시 재시도 (아웃박스)

발송 대상을 `notification_push_outbox`에 **수신자 단위 row**로 남긴다. 알림과 같은 트랜잭션에서 저장되므로 프로세스가 재시작돼도 발송 대상이 남는다.

```
PENDING ─┬─ 발송 성공 ──────────────→ SENT
         ├─ Retryable 실패 ─ 백오프 → PENDING (재시도 횟수 소진 시 FAILED)
         ├─ InvalidToken/Permanent → FAILED
         └─ 생성 후 1시간 경과 ─────→ EXPIRED
```

- **백오프는 `30초 → 2분 → 5분 → 15분`, 최대 4회** — `NotificationPushOutbox`의 상수로 정의
- **유효 시간(TTL)은 알림 종류가 정한다** — `NotificationStatus`가 `PushUrgency`를 선언한다

  | 구분 | TTL | 이유 |
  |---|---|---|
  | `IMMEDIATE` (사용자 알림) | 15분 | 지금 무엇을 할지 결정하는 정보라, 늦게 도착하면 오히려 혼란스럽다 |
  | `DEFERRABLE` (관리자 알림) | 1시간 | 대시보드에 처리할 건이 남아 있어 늦게 도착해도 유효하다 |

  TTL이 지나면 `EXPIRED`로 포기한다. 인앱 알림은 이미 저장돼 있으므로 정보가 사라지는 것은 아니다.
- **다음 재시도 시각이 TTL을 넘기면 예약하지 않고 즉시 포기한다** — 어차피 만료될 시도를 기다리지 않는다
- **즉시 발송과 재시도가 같은 경로를 탄다** — 새 row의 `nextRetryAt`은 60초 뒤로 잡혀, 즉시 시도와 폴러가 겹치지 않는다
- **메시지 본문은 저장하지 않는다** — 연결된 `Notification`의 status와 formatValues로 재구성
- 인스턴스를 여러 대로 늘리면 조회에 잠금(`FOR UPDATE SKIP LOCKED`)이나 ShedLock이 필요하다

**보관 정책** — `PushOutboxPurgeScheduler`가 매일 새벽 4시(KST)에 정리한다.

| 상태 | 보존 기간 |
|---|---|
| `SENT` | 7일 |
| `FAILED`, `EXPIRED` | 30일 (실패 원인 확인용) |
| `PENDING` | 삭제하지 않음 |

- **배치(500건)로 나눠 삭제하고 배치마다 트랜잭션을 끊는다** — 락 구간을 짧게 유지
- **한 회 처리량 상한(20배치)에 도달하면 경고 로그를 남긴다** — 남은 건이 있다는 사실이 묻히지 않도록

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
