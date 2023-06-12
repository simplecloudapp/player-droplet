package app.simplecloud.droplet.player.plugin.shared.proxy

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest

interface ProxyController {

    fun handleLogin(request: CloudPlayerLoginRequest)

    fun handleDisconnect(request: CloudPlayerDisconnectRequest)

}