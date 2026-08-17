package site.billilge.api.backend.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * 스케줄러는 단일 스레드에서 순차 실행된다.
 *
 * 인스턴스를 여러 대로 늘리면 같은 발송 건을 동시에 집어갈 수 있으므로,
 * 그때는 조회에 잠금(FOR UPDATE SKIP LOCKED)이나 ShedLock이 필요하다.
 */
@EnableScheduling
@Configuration
class SchedulingConfig
