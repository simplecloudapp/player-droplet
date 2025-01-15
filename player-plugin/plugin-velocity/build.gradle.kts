dependencies {
    compileOnly(rootProject.libs.velocity)
    api(rootProject.libs.cloud.velocity)
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
