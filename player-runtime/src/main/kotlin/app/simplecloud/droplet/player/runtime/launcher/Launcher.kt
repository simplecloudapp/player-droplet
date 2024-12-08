package app.simplecloud.droplet.player.runtime.launcher

import app.simplecloud.droplet.player.runtime.PlayerRuntime

fun main() {
    val server = PlayerRuntime()
    server.start()
}