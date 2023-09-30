plugins {
  id("com.diffplug.spotless") version "6.18.0"
  id("org.jetbrains.dokka") version "1.8.10"
  application

  // Serialization.
  kotlin("jvm") version "1.8.21"
  kotlin("plugin.serialization") version "1.8.21"

  kotlin("kapt") version "1.8.21"
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  api("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")
  api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

  api("com.google.flogger:flogger:0.7.4")
  api("com.google.flogger:flogger-system-backend:0.7.4")
  api("com.google.flogger:flogger-log4j2-backend:0.7.4")

  implementation("com.google.dagger:dagger:2.45")
  kapt("com.google.dagger:dagger-compiler:2.45")

  api("org.apache.logging.log4j:log4j:2.20.0")
  api("org.apache.logging.log4j:log4j-core:2.20.0")
  api("org.apache.logging.log4j:log4j-api:2.20.0")

  testImplementation("junit:junit:4.13.2")
  testImplementation("com.google.truth:truth:1.1.3")
  testImplementation(kotlin("test"))
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

group = "com.hopskipnfall"

description = "MidiSearch"

version = "0.0.1"

kotlin { jvmToolchain(17) }

sourceSets {
  main {
    kotlin.srcDir("src/main/java")

    resources { srcDirs("conf") }
  }

  test {
    kotlin.srcDir("src/test/java")

    resources { srcDirs("conf") }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  useJUnit()
}

// Formatting/linting.
spotless {
  kotlin {
    target("**/*.kt", "**/*.kts")
    targetExclude("build/", ".git/", ".idea/", ".mvn")
    ktfmt().googleStyle()
  }

  yaml {
    target("**/*.yml", "**/*.yaml")
    targetExclude("build/", ".git/", ".idea/", ".mvn")
    jackson()
  }
}

application { mainClass.set("com.hopskipnfall.MidiSearchMainKt") }

// "jar" task makes a single jar including all dependencies.
tasks.jar {
  manifest { attributes["Main-Class"] = application.mainClass }

  from(configurations.runtimeClasspath.get().map { zipTree(it) })
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  archiveBaseName.set("midi-search")
}
