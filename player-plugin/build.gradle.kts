subprojects {
    repositories {
        mavenCentral()
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }

    tasks {
        shadowJar {
            dependencies {
                relocate("com.google.protobuf", "app.simplecloud.simplecloud.api.external.protobuf") {
                    include(dependency("com.google.protobuf:protobuf-java"))
                }

                relocate("io.grpc", "app.simplecloud.simplecloud.api.external.grpc") {
                    include(dependency("io.grpc:grpc-stub"))
                    include(dependency("io.grpc:grpc-protobuf"))
                    include(dependency("io.grpc:grpc-netty-shaded"))
                }

                relocate("kotlin", "app.simplecloud.simplecloud.api.external.kotlin") {
                    include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
                }

                relocate("com.rabbitmq", "app.simplecloud.simplecloud.api.external.rabbitmq") {
                    include(dependency("com.rabbitmq:amqp-client"))
                }

                relocate("com.google.gson", "app.simplecloud.simplecloud.api.external.gson") {
                    include(dependency("com.google.code.gson:gson"))
                }
            }
        }
    }

}