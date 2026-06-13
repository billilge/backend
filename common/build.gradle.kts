plugins {
    kotlin("plugin.spring")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.1")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
}
