package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.api.impl.PlayerApiFutureImpl
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.PlayerInternalApi

class PlayerProxyApi(
    onlinePlayerChecker: OnlinePlayerChecker
) : PlayerInternalApi(onlinePlayerChecker) {

    private val futureApi = getFutureApi() as? PlayerApiFutureImpl ?: throw IllegalStateException("PlayerApi is not Future")
    private val playerServiceStub = futureApi.playerServiceStub

    val proxyController: ProxyController = ProxyControllerImpl(playerServiceStub)

}