package site.billilge.api.backend.global.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

private val log = KotlinLogging.logger {}

@EnableAsync
@Configuration
class AsyncConfig : AsyncConfigurer {
    /**
     * 알림 발송 전용 실행기.
     *
     * 자동 설정 실행기(applicationTaskExecutor)는 큐가 무제한이라 FCM이 지연되면 태스크가
     * 계속 쌓이고, 종료 대기 설정이 없어 배포 시 큐에 남은 알림이 유실된다.
     */
    @Bean
    fun notificationTaskExecutor(): ThreadPoolTaskExecutor = ThreadPoolTaskExecutor().apply {
        corePoolSize = CORE_POOL_SIZE
        maxPoolSize = MAX_POOL_SIZE
        setQueueCapacity(QUEUE_CAPACITY)
        setThreadNamePrefix("notification-")

        // 큐가 가득 차면 버리는 대신 호출 스레드에서 실행한다 — 유실보다 지연을 택한다
        setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())

        // 종료 시 큐에 남은 알림을 처리할 시간을 준다
        setWaitForTasksToCompleteOnShutdown(true)
        setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS)
    }

    override fun getAsyncExecutor(): Executor = notificationTaskExecutor()

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler =
        AsyncUncaughtExceptionHandler { ex, method, params ->
            log.error(ex) {
                "비동기 작업 실패: ${method.declaringClass.simpleName}.${method.name}(${params.joinToString()})"
            }
        }

    companion object {
        private const val CORE_POOL_SIZE = 4
        private const val MAX_POOL_SIZE = 8
        private const val QUEUE_CAPACITY = 500
        private const val AWAIT_TERMINATION_SECONDS = 20
    }
}
