rootProject.name = "player-droplet"
include("player-api")
include("player-server")
include("player-shared")
include("player-plugin")
include("player-plugin:plugin-shared")
findProject(":player-plugin:plugin-shared")?.name = "plugin-shared"
include("player-plugin:plugin-velocity")
findProject(":player-plugin:plugin-velocity")?.name = "plugin-velocity"
include("player-plugin:plugin-bungeecord")
findProject(":player-plugin:plugin-bungeecord")?.name = "plugin-bungeecord"
include("player-plugin:plugin-spigot")
findProject(":player-plugin:plugin-spigot")?.name = "plugin-spigot"
include("player-plugin:plugin-minestom")
findProject(":player-plugin:plugin-minestom")?.name = "plugin-minestom"
