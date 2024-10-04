import com.google.protobuf.gradle.*

plugins {
    id("com.google.protobuf") version "0.9.4"
}

val protobufVersion = "3.25.5"
val grpcVersion = "1.68.0"
val amqpVersion = "5.17.0"
val gsonVersion = "2.10.1"

dependencies {
    api("com.google.protobuf:protobuf-java:$protobufVersion")
    api("io.grpc:grpc-stub:$grpcVersion")
    api("io.grpc:grpc-protobuf:$grpcVersion")
    api("io.grpc:grpc-netty-shaded:$grpcVersion")
    api("com.rabbitmq:amqp-client:$amqpVersion")
    api("com.google.code.gson:gson:$gsonVersion")
    api("javax.annotation:javax.annotation-api:1.3.2")
}


protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:3.25.5"
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