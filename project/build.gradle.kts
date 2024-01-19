plugins {
    java
    idea
    `java-library`
    `java-test-fixtures`
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "at.fhooe.project"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.apache.kafka:kafka-clients:3.6.1")
    implementation("org.apache.kafka:kafka-streams:3.6.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.kafka:spring-kafka")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.ADOPTIUM)
}

project.idea.module {
    project.sourceSets.getByName("testFixtures").allSource.srcDirs.forEach {
        testSources.from(testSources.plus(it))
    }
}

jib {
    from {
        image = "docker://openjdk:17"
    }
    to {
        image = "tiered-storage-application"
        tags = setOf("latest")
    }
    container {
        jvmFlags = listOf("-Xms512m", "-Xdebug")
        mainClass = "at.fhooe.project.KafkaTieredStorageApplication"
    }
}
