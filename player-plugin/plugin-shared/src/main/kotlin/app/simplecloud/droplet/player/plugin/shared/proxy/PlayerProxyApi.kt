package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.api.impl.PlayerApiImpl

class PlayerProxyApi : PlayerApiImpl() {

    val proxyController: ProxyController = ProxyControllerImpl(playerServiceStub)

}