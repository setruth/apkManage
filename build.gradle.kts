import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.dependencyManagement)
    alias(libs.plugins.springboot)
}

group = "com.setruth"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.jackson)
    implementation(libs.mysql.connector)
    implementation(libs.knife4j)
    implementation(libs.mybatis.springboot)
    implementation(libs.mybatis.flex.core)
    implementation(libs.mybatis.flex.kotlin)
    implementation(libs.mybatis.actable)
    implementation(libs.springBoot.webmvc)
    implementation(libs.kotlinx.coroutines)
    testImplementation(libs.springBoot.test)

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.named<BootJar>("bootJar") {
    archiveFileName.set("server.jar")
}