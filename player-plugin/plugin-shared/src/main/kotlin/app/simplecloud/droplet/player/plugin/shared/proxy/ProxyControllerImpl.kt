package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.shared.future.toCompletable
import build.buf.gen.simplecloud.droplet.player.v1.*
import java.util.concurrent.CompletableFuture

class ProxyControllerImpl(
    private val playerServiceStub: PlayerServiceGrpc.PlayerServiceFutureStub
) : ProxyController {

    override fun handleLogin(request: CloudPlayerLoginRequest): CompletableFuture<CloudPlayerLoginResponse> {
        return playerServiceStub.loginCloudPlayer(request).toCompletable()
    }

    override fun handleDisconnect(request: CloudPlayerDisconnectRequest): CompletableFuture<CloudPlayerDisconnectResponse> {
        return playerServiceStub.disconnectCloudPlayer(request).toCompletable()
    }

}