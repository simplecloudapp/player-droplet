dependencies {
    compileOnly(rootProject.libs.spigot)
    api(project(":player-shared"))
    api(project(":player-plugin:plugin-shared"))
    api(project(":player-api"))
    api(rootProject.libs.adventure.platform.spigot)
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
        }
    }
}
