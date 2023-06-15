package app.simplecloud.droplet.player.plugin.minestom

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi
import app.simplecloud.droplet.player.plugin.shared.adventure.listener.AdventureListeners
import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import java.util.*

class PlayerMinestomPlugin : Extension() {

    private val playerApi = PlayerInternalApi(
        OnlinePlayerChecker { MinecraftServer.getConnectionManager().getPlayer(UUID.fromString(it)) != null }
    )

    override fun initialize() {
        PlayerApiSingleton.init(playerApi)

        AdventureListeners.allWithClasses(MinestomAudienceRepository()).forEach {
            playerApi.registerRabbitMqListener(it.first, it.second)
        }
    }

    override fun terminate() {
    }
}