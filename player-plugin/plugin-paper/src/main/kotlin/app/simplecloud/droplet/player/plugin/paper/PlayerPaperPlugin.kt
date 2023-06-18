package app.simplecloud.droplet.player.plugin.paper

import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi
import app.simplecloud.droplet.player.plugin.shared.adventure.listener.AdventureListeners
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class PlayerPaperPlugin : JavaPlugin() {

    private val playerApi = PlayerInternalApi(
            OnlinePlayerChecker { Bukkit.getServer().getPlayer(UUID.fromString(it)) != null }
    )

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        Bukkit.getServicesManager()
                .register(PlayerApi::class.java, playerApi, this, org.bukkit.plugin.ServicePriority.Normal)


        AdventureListeners.allWithClasses(PaperAudienceRepository()).forEach {
            playerApi.registerRabbitMqListener(RabbitMqChannelNames.ADVENTURE, it.first, it.second)
        }
    }

}