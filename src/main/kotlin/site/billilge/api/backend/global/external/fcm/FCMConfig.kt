package site.billilge.api.backend.global.external.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException


@Configuration
class FCMConfig {
    @Bean
    @Throws(IOException::class)
    fun firebaseApp(): FirebaseApp {
        val serviceAccountStream = ClassPathResource("firebase/firebaseServiceKey.json").inputStream

        val googleCredentials = GoogleCredentials.fromStream(serviceAccountStream)
            .createScoped(
                listOf(
                    "https://www.googleapis.com/auth/firebase.messaging",
                    "https://www.googleapis.com/auth/cloud-platform"
                )
            )

        googleCredentials.refreshIfExpired()

        val options = FirebaseOptions.builder()
            .setCredentials(googleCredentials)
            .build()
        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}