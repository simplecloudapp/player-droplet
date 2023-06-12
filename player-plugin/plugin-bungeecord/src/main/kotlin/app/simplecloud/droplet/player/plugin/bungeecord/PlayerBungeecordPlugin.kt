package app.simplecloud.droplet.player.plugin.bungeecord

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerDisconnectListener
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import net.md_5.bungee.api.plugin.Plugin

class PlayerBungeecordPlugin : Plugin() {

    private val playerApi = PlayerProxyApi()

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        proxy.pluginManager.registerListener(this, PlayerConnectionListener(playerApi))
        proxy.pluginManager.registerListener(this, PlayerDisconnectListener(playerApi))
    }


}