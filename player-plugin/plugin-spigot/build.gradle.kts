dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    api(project(":player-shared"))
    api(project(":player-plugin:plugin-shared"))
    api(project(":player-api"))
    api("net.kyori:adventure-platform-bukkit:4.3.0")
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
        }
    }
}
