import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("org.jooq.jooq-codegen-gradle") version "3.19.3"
}

dependencies {
    api(project(":player-shared"))
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

    implementation("org.jooq:jooq-kotlin:3.19.3")
    implementation("org.jooq:jooq-meta:3.19.3")
    implementation("org.jooq:jooq-meta-extensions:3.19.3")
    implementation("org.jooq:jooq-postgres-extensions:3.19.14")
    jooqCodegen("org.jooq:jooq-meta-extensions:3.19.3")


    implementation("redis.clients:jedis:4.4.1")
    implementation("dev.morphia.morphia:morphia-core:2.3.4")
    implementation("app.simplecloud:simplecloud-pubsub:1.0.5")
    implementation("build.buf.gen:simplecloud_proto-specs_grpc_kotlin:1.4.1.1.20240606064605.c07118735783")
}

tasks.named("compileKotlin") {
    dependsOn(tasks.jooqCodegen)
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("player-server.jar")
    }
}

application {
    mainClass.set("app.simplecloud.droplet.player.server.launcher.LauncherKt")
}

sourceSets {
    main {
        java {
            srcDirs(
                "build/generated/source/db/main/java",
            )
        }
        resources {
            srcDirs(
                "src/main/db"
            )
        }
    }
}


jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            target {
                directory = "build/generated/source/db/main/java"
                packageName = "app.simplecloud.droplet.player.shared.db"
            }
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    // Specify the location of your SQL script.
                    // You may use ant-style file matching, e.g. /path/**/to/*.sql
                    //
                    // Where:
                    // - ** matches any directory subtree
                    // - * matches any number of characters in a directory / file name
                    // - ? matches a single character in a directory / file name
                    property {
                        key = "scripts"
                        value = "src/main/db/schema.sql"
                    }

                    // The sort order of the scripts within a directory, where:
                    //
                    // - semantic: sorts versions, e.g. v-3.10.0 is after v-3.9.0 (default)
                    // - alphanumeric: sorts strings, e.g. v-3.10.0 is before v-3.9.0
                    // - flyway: sorts files the same way as flyway does
                    // - none: doesn't sort directory contents after fetching them from the directory
                    property {
                        key = "sort"
                        value = "semantic"
                    }

                    // The default schema for unqualified objects:
                    //
                    // - public: all unqualified objects are located in the PUBLIC (upper case) schema
                    // - none: all unqualified objects are located in the default schema (default)
                    //
                    // This configuration can be overridden with the schema mapping feature
                    property {
                        key = "unqualifiedSchema"
                        value = "none"
                    }

                    // The default name case for unquoted objects:
                    //
                    // - as_is: unquoted object names are kept unquoted
                    // - upper: unquoted object names are turned into upper case (most databases)
                    // - lower: unquoted object names are turned into lower case (e.g. PostgreSQL)
                    property {
                        key = "defaultNameCase"
                        value = "lower"
                    }
                }
            }
        }
    }
}