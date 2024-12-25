package app.simplecloud.droplet.player.runtime.launcher

import app.simplecloud.droplet.player.runtime.PlayerRuntime
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.main

suspend fun main(args: Array<String>) {

    PlayerDropletStartCommand().main(args)
}
