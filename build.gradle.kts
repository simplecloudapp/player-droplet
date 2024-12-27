import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    `maven-publish`
}
val baseVersion = "0.0.1"
val commitHash = System.getenv("COMMIT_HASH")
val snapshotversion = "${baseVersion}-dev.$commitHash"


allprojects {
    version = if (commitHash != null) snapshotversion else baseVersion

    repositories {
        mavenCentral()
        maven("https://buf.build/gen/maven")
        maven("https://repo.simplecloud.app/snapshots/")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "maven-publish")

    dependencies {
        testImplementation(rootProject.libs.kotlin.test)
        implementation(rootProject.libs.kotlin.jvm)
    }

    publishing {
        repositories {
            maven {
                name = "simplecloud"
                url = uri("https://repo.simplecloud.app/snapshots/")
                credentials {
                    username = System.getenv("SIMPLECLOUD_USERNAME")?: (project.findProperty("simplecloudUsername") as? String)
                    password = System.getenv("SIMPLECLOUD_PASSWORD")?: (project.findProperty("simplecloudPassword") as? String)
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            // Not publish controller-runtime
            if (project.name == "player-runtime") {
                return@publications
            }

            if (project.name.contains("plugin")) {
                return@publications
            }

            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    kotlin {
        jvmToolchain(21)
        compilerOptions {
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        }
    }

    tasks.named("shadowJar", ShadowJar::class) {
        mergeServiceFiles()
        archiveFileName.set("${project.name}.jar")
    }

    tasks.test {
        useJUnitPlatform()
    }

}