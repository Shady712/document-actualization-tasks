import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	application
	id("org.springframework.boot") version "2.7.10"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.10"
	kotlin("plugin.spring") version "1.8.10"
}

group = "com.actualization.document"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

application {
	mainClass.set("com.actualization.document.tasks.TasksApplicationKt")
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework:spring-context")
	implementation("org.springframework:spring-context-indexer")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

	// Camunda
	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.18.0")
	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.18.0")
	implementation("com.sun.xml.bind:jaxb-impl:2.3.5")
	testImplementation("org.camunda.bpm:camunda-bpm-assert:7.18.0")
	testImplementation("org.camunda.bpm:camunda-engine-plugin-spin:7.18.0")
	testImplementation("org.camunda.community.mockito:camunda-platform-7-mockito:6.18.0")
	testImplementation("org.camunda.bpm:camunda-bpm-junit5:7.18.0")

	// Database
	implementation("org.postgresql:postgresql:42.2.27")
	implementation("com.zaxxer:HikariCP")

	// Utils
	implementation("org.apache.commons:commons-lang3:3.12.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
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
