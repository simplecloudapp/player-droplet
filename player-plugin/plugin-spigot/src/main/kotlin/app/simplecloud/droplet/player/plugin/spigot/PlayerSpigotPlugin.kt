package app.simplecloud.droplet.player.plugin.spigot

import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi
import app.simplecloud.droplet.player.plugin.shared.adventure.listener.AdventureListeners
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class PlayerSpigotPlugin : JavaPlugin() {

    private val playerApi = PlayerInternalApi(
        OnlinePlayerChecker { Bukkit.getPlayer(UUID.fromString(it)) != null }
    )

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        Bukkit.getServicesManager().register(PlayerApi::class.java, playerApi, this, org.bukkit.plugin.ServicePriority.Normal)

//        AdventureListeners.allWithClasses(VelocityAudienceRepository(proxyServer)).forEach {
//            playerApi.registerRabbitMqListener(it.first, it.second)
//        }
    }

}