dependencies {
    compileOnly(rootProject.libs.spigo)
    api(project(":player-shared"))
    api(project(":player-plugin:plugin-shared"))
    api(project(":player-api"))
    api(rootProject.libs.adventurePlatformSpigot)
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
        }
    }
}
