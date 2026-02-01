plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

val versions = mapOf(
	"mapstructVersion" to "1.6.3",
)

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
	implementation(project(":common-libs"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//implementation("org.springframework.boot:spring-boot-starter-kafka")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	//testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// additional libs
	compileOnly("org.mapstruct:mapstruct:${versions["mapstructVersion"]}")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	annotationProcessor("org.mapstruct:mapstruct-processor:${versions["mapstructVersion"]}")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
