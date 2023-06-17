package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectResponse
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.CloudPlayerLoginResponse
import java.util.concurrent.CompletableFuture

interface ProxyController {

    fun handleLogin(request: CloudPlayerLoginRequest): CompletableFuture<CloudPlayerLoginResponse>

    fun handleDisconnect(request: CloudPlayerDisconnectRequest): CompletableFuture<CloudPlayerDisconnectResponse>

}