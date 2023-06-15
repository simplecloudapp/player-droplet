package app.simplecloud.droplet.player.plugin.bungeecord

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.bungeecord.connection.CloudPlayerConnectListener
import app.simplecloud.droplet.player.plugin.bungeecord.connection.CloudPlayerKickListener
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerDisconnectListener
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerKickEvent
import app.simplecloud.droplet.player.proto.ConnectCloudPlayerEvent
import net.md_5.bungee.api.plugin.Plugin

class PlayerBungeecordPlugin : Plugin() {

    private val playerApi = PlayerProxyApi(
        OnlinePlayerChecker { proxy.getPlayer(it) != null }
    )

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        proxy.pluginManager.registerListener(this, PlayerConnectionListener(playerApi))
        proxy.pluginManager.registerListener(this, PlayerDisconnectListener(playerApi))

        playerApi.registerRabbitMqListener(
                CloudPlayerKickEvent::class.java, CloudPlayerKickListener((this.proxy)
        ))
        playerApi.registerRabbitMqListener(
                ConnectCloudPlayerEvent::class.java, CloudPlayerConnectListener((this.proxy)
        ))

    }


}