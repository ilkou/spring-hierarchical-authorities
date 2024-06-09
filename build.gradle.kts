plugins {
	java
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "hierarchical.authorities"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// https://mvnrepository.com/artifact/org.casbin/casbin-spring-boot-starter
	implementation("org.casbin:casbin-spring-boot-starter:1.5.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations.all {
	resolutionStrategy {
		eachDependency {
			if (requested.group == "org.casbin" && requested.name == "jcasbin") {
				useVersion("1.55.0") // Specify the version you want to use
			}
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
