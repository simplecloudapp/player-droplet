package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.PlayerServiceGrpc
import app.simplecloud.droplet.player.shared.future.toCompletable

class ProxyControllerImpl(
    private val playerServiceStub: PlayerServiceGrpc.PlayerServiceFutureStub
) : ProxyController {

    override fun handleLogin(request: CloudPlayerLoginRequest) {
        playerServiceStub.loginCloudPlayer(request).toCompletable().thenAccept {
            println("handleLogin success: ${it.success}")
        }.exceptionally {
            println("handleLogin error")
            it.printStackTrace()
            null
        }
    }

    override fun handleDisconnect(request: CloudPlayerDisconnectRequest) {
        playerServiceStub.disconnectCloudPlayer(request)
    }

}