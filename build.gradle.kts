
plugins {
    kotlin("jvm") version "1.3.61" apply true
    id("tanvd.kosogor") version "1.0.7" apply true
}

subprojects {
    apply(plugin = "tanvd.kosogor")
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                apiVersion = "1.3"
                languageVersion = "1.3"
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
    }


}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}
