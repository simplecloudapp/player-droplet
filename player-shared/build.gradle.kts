import com.google.protobuf.gradle.*

plugins {
    id("com.google.protobuf") version "0.8.19"
}

dependencies {
    api("com.google.protobuf:protobuf-java:3.16.3")
    api("io.grpc:grpc-stub:1.55.1")
    api("io.grpc:grpc-protobuf:1.55.1")
    api("io.grpc:grpc-netty-shaded:1.55.1")
    api("com.rabbitmq:amqp-client:5.17.0")
    api("com.google.code.gson:gson:2.10.1")
    api("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:3.23.1"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.55.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without
                // options. Note the braces cannot be omitted, otherwise the
                // plugin will not be added. This is because of the implicit way
                // NamedDomainObjectContainer binds the methods.
                id("grpc") { }
            }
        }
    }
}