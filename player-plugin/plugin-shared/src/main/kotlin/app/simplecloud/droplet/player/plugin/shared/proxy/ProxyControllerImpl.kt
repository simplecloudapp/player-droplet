package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.PlayerServiceGrpc

class ProxyControllerImpl(
    private val playerServiceStub: PlayerServiceGrpc.PlayerServiceFutureStub
) : ProxyController {

    override fun handleLogin(request: CloudPlayerLoginRequest) {
        playerServiceStub.loginCloudPlayer(request)
    }

    override fun handleDisconnect(request: CloudPlayerDisconnectRequest) {
        playerServiceStub.disconnectCloudPlayer(request)
    }

}