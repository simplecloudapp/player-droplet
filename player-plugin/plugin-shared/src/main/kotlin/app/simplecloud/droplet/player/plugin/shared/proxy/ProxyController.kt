package app.simplecloud.droplet.player.plugin.shared.proxy

import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerDisconnectRequest
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerDisconnectResponse
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerLoginRequest
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerLoginResponse
import java.util.concurrent.CompletableFuture

interface ProxyController {

    fun handleLogin(request: CloudPlayerLoginRequest): CompletableFuture<CloudPlayerLoginResponse>

    fun handleDisconnect(request: CloudPlayerDisconnectRequest): CompletableFuture<CloudPlayerDisconnectResponse>

}