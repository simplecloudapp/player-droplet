package app.simplecloud.droplet.player.plugin.spigot

import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi
import app.simplecloud.droplet.player.plugin.shared.adventure.listener.AdventureListeners
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class PlayerSpigotPlugin : JavaPlugin() {

    private val playerApi = PlayerInternalApi(
        OnlinePlayerChecker { Bukkit.getPlayer(UUID.fromString(it)) != null }
    )

    private val adventure by lazy {
        BukkitAudiences.create(this)
    }

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        Bukkit.getServicesManager()
            .register(PlayerApi::class.java, playerApi, this, org.bukkit.plugin.ServicePriority.Normal)


        AdventureListeners.allWithClasses(SpigotAudienceRepository(adventure)).forEach {
            playerApi.registerRabbitMqListener(RabbitMqChannelNames.ADVENTURE, it.first, it.second)
        }
    }

    override fun onDisable() {
        adventure.close()
    }

}