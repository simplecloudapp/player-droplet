package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi

class PlayerProxyApi(
    onlinePlayerChecker: OnlinePlayerChecker
) : PlayerInternalApi(onlinePlayerChecker) {

    val proxyController: ProxyController = ProxyControllerImpl(playerServiceStub)

}