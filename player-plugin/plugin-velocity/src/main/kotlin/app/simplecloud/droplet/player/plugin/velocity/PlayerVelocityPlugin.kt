package app.simplecloud.droplet.player.plugin.velocity

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerDisconnectListener
import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import java.util.logging.Logger

class PlayerVelocityPlugin @Inject constructor(
    var proxyServer: ProxyServer,
    var logger: Logger,
    private val injector: Injector,
) {

    private val playerApi = PlayerProxyApi()

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        PlayerApiSingleton.init(playerApi)
        proxyServer.eventManager.register(this, PlayerConnectionListener(playerApi))
        proxyServer.eventManager.register(this, PlayerDisconnectListener(playerApi))
    }


}