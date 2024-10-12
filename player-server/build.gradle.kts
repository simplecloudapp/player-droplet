import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
}

dependencies {
    api(project(":player-shared"))
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("redis.clients:jedis:4.4.1")
    implementation("dev.morphia.morphia:morphia-core:2.3.4")
    implementation("app.simplecloud:simplecloud-pubsub:1.0.5")
    implementation("build.buf.gen:simplecloud_proto-specs_grpc_kotlin:1.4.1.1.20240606064605.c07118735783")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("player-server.jar")
    }
}

application {
    mainClass.set("app.simplecloud.droplet.player.server.launcher.LauncherKt")
}