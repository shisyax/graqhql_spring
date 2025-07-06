import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
	java
	id("org.springframework.boot") version "3.5.4-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	// DGS Codegenプラグインを正しく適用します。バージョンはDGSのバージョンに合わせて調整してください。
	id("com.netflix.dgs.codegen") version "6.2.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

// BOMのバージョンを指定するための変数
val netflixDgsVersion = "10.2.1"
val snippetsDir = file("build/generated-snippets")

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	// Springのスナップショットリポジトリ
	maven { url = uri("https://repo.spring.io/snapshot") }
	// google() や gradlePluginPortal() は特定の依存関係で必要になるまで通常は不要です。
	// 不要なリポジトリは削除しました。
}

// dependencyManagementブロックを一つにまとめます。
dependencyManagement {
	imports {
		// DGSのプラットフォームBOM（Bill of Materials）を指定し、バージョンを一元管理します。
		mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$netflixDgsVersion")
	}
}

dependencies {
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-configuration-processor")

	// DGS Framework Dependencies
	// Spring Boot 3.x と Spring for GraphQL を使用する場合、こちらが推奨されます。
	// BOMによってバージョンが管理されるため、バージョン指定は不要です。
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
//	implementation("com.netflix.graphql.dgs:graphql-dgs-federation-support")
	implementation("com.apollographql.federation:federation-graphql-java-support:5.4.0")
	// 'graphql-dgs-spring-boot-starter' は重複するため削除しました。
	// バージョンが混在していた 'runtimeOnly' の依存関係も削除しました。

	// Database Migration
	implementation("org.flywaydb:flyway-core")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Database Drivers
	implementation("com.h2database:h2")
	implementation("io.r2dbc:r2dbc-h2")

	// Development Tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Test Dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// DGSのテスト用スターターもBOMによってバージョンが管理されます。
	testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.named<Test>("test") {
	outputs.dir(snippetsDir)
	useJUnitPlatform()
}

tasks.named<AsciidoctorTask>("asciidoctor") {
	inputs.dir(snippetsDir)
	dependsOn(tasks.named("test"))
}

//// DGS Codegenのタスク設定例です。
//// スキーマの場所や生成先のパッケージに合わせて適宜変更してください。
//tasks.withType<GenerateJavaTask> {
//	schemaPaths.setFrom(files("${project.projectDir}/src/main/resources/schema"))
//	packageName.set("com.example.generated") // 生成するコードのパッケージ名
//	generateClient.set(true) // 必要に応じてクライアントコードも生成
//}
