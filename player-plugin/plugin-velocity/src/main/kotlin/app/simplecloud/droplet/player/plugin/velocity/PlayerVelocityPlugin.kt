package app.simplecloud.droplet.player.plugin.velocity

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerDisconnectListener
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import java.util.*

class PlayerVelocityPlugin @Inject constructor(
    private val proxyServer: ProxyServer,
) {

    private val playerApi = PlayerProxyApi(
        OnlinePlayerChecker { proxyServer.getPlayer(UUID.fromString(it)).isPresent }
    )

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        PlayerApiSingleton.init(playerApi)
        proxyServer.eventManager.register(this, PlayerConnectionListener(playerApi))
        proxyServer.eventManager.register(this, PlayerDisconnectListener(playerApi))
    }

}