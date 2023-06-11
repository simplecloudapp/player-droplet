dependencies {
    api(project(":player-shared"))
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("redis.clients:jedis:4.4.1")
    implementation("dev.morphia.morphia:morphia-core:2.3.4")
}