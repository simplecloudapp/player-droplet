dependencies {
    compileOnly(rootProject.libs.simplecloud.controller)
    api(project(":player-shared"))
    api(project(":player-api"))
    api(rootProject.libs.cloud.core)
    api(rootProject.libs.adventure)
    api(rootProject.libs.adventure.text.minimessage)
}