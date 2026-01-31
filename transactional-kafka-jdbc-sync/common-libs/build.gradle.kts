plugins {
    id("java-library")
}

version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    // BOM Spring Boot
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.2"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:4.0.2"))
    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.2"))

    // Validation
    api("jakarta.validation:jakarta.validation-api")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
