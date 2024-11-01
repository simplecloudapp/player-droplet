package app.simplecloud.droplet.player.plugin.shared.proxy

import build.buf.gen.simplecloud.droplet.player.v1.*
import java.util.concurrent.CompletableFuture

interface ProxyController {

    fun handleLogin(request: CloudPlayerLoginRequest): CompletableFuture<CloudPlayerLoginResponse>

    fun handleDisconnect(request: CloudPlayerDisconnectRequest): CompletableFuture<CloudPlayerDisconnectResponse>

}