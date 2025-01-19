dependencies {
    compileOnly(rootProject.libs.bungeecord)
    api(rootProject.libs.cloud.core)
    api(rootProject.libs.adventure.platform.bungeecord)
    api(rootProject.libs.cloud.bungeecord)
    api("net.kyori:adventure-text-serializer-legacy:4.14.0")
    api(project(":player-shared"))
    api(project(":player-plugin:plugin-shared"))
    api(project(":player-api"))
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            relocate("org.incendo.cloud", "app.simplecloud.droplet.player.external")
        }
    }
}