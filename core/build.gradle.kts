plugins {
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}
