package app.simplecloud.droplet.player.plugin.minestom

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi
import app.simplecloud.droplet.player.plugin.shared.adventure.listener.AdventureListeners
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import java.util.*

class PlayerMinestomPlugin : Extension() {

    private val playerApi = PlayerInternalApi(
        OnlinePlayerChecker { MinecraftServer.getConnectionManager().getPlayer(UUID.fromString(it)) != null }
    )

    override fun initialize() {
        PlayerApiSingleton.init(playerApi)

        AdventureListeners.allWithClasses(MinestomAudienceRepository()).forEach {
            playerApi.registerRabbitMqListener(RabbitMqChannelNames.ADVENTURE, it.first, it.second)
        }
    }

    override fun terminate() {
    }
}