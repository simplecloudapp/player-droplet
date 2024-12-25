import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compileOnly("net.minestom:minestom-snapshots:59406d5b54")
    compileOnly("dev.hollowcube:minestom-ce-extensions:1.2.0")
    api(project(":player-shared"))
    api(project(":player-plugin:plugin-shared"))
    api(project(":player-api"))
}