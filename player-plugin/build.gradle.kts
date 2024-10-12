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

    dependencies {
        api("app.simplecloud:simplecloud-pubsub:1.0.5")
        api("build.buf.gen:simplecloud_proto-specs_grpc_java:1.4.1.1.20240606064605.c07118735783")
        api("build.buf.gen:simplecloud_proto-specs_protocolbuffers_java:28.2.0.1.20241001163139.58018cb317ed")
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }
        shadowJar {
            dependencies {

                include(project(":player-shared"))
                include(project(":player-api"))
                include(project(":player-plugin:plugin-shared"))

                include(dependency("net.kyori:adventure-api"))
                include(dependency("net.kyori:adventure-text-serializer-gson"))
                include(dependency("net.kyori:adventure-text-serializer-json"))
                include(dependency("net.kyori:adventure-text-serializer-legacy"))
                include(dependency("build.buf.gen:simplecloud_proto-specs_grpc_java"))
                include(dependency("build.buf.gen:simplecloud_proto-specs_protocolbuffers_java"))

                relocate("com.google.protobuf", "app.simplecloud.simplecloud.api.external.protobuf") {
                    include(dependency("com.google.protobuf:protobuf-java"))
                }

                relocate("app.simplecloud.pubsub", "app.simplecloud.simplecloud.api.external.pubsub") {
                    include(dependency("app.simplecloud:simplecloud-pubsub"))
                }

                relocate("io.grpc", "app.simplecloud.simplecloud.api.external.grpc") {
                    include(dependency("io.perfmark:perfmark-api"))
                    include(dependency("io.grpc:grpc-protobuf-lite"))
                    include(dependency("io.grpc:grpc-context"))
                    include(dependency("io.grpc:grpc-api"))
                    include(dependency("io.grpc:grpc-core"))
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