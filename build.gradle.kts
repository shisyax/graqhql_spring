import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
	java
	id("org.springframework.boot") version "3.5.4-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

val snippetsDir = file("build/generated-snippets")
val netflixDgsVersion = "10.2.1"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://repo.spring.io/snapshot")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.r2dbc:r2dbc-h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$netflixDgsVersion")
	}
}

tasks.named<Test>("test") {
	outputs.dir(snippetsDir)
	useJUnitPlatform()
}

tasks.named<AsciidoctorTask>("asciidoctor") {
	inputs.dir(snippetsDir)
	dependsOn(tasks.named("test"))
}
