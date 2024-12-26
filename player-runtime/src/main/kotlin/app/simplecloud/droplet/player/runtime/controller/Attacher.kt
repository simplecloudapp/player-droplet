package app.simplecloud.droplet.player.runtime.controller

import app.simplecloud.controller.shared.host.ServerHost
import app.simplecloud.droplet.api.droplet.Droplet
import app.simplecloud.droplet.player.runtime.PlayerRuntime
import app.simplecloud.droplet.player.runtime.launcher.PlayerDropletStartCommand
import build.buf.gen.simplecloud.controller.v1.ControllerDropletServiceGrpcKt
import build.buf.gen.simplecloud.controller.v1.ControllerServerServiceGrpcKt
import build.buf.gen.simplecloud.controller.v1.RegisterDropletRequest
import io.grpc.ConnectivityState
import io.grpc.ManagedChannel
import kotlinx.coroutines.*
import org.apache.logging.log4j.LogManager

class Attacher(
    private val startCommand: PlayerDropletStartCommand,
    private val channel: ManagedChannel,
    private val stub: ControllerDropletServiceGrpcKt.ControllerDropletServiceCoroutineStub,
) {
    private val logger = LogManager.getLogger(Attacher::class.java)

    private suspend fun attach(): Boolean {
        try {
            stub.registerDroplet(
                RegisterDropletRequest.newBuilder().setDefinition(
                    Droplet(
                        type = "player",
                        host = startCommand.grpcHost,
                        id = startCommand.id,
                        port = startCommand.grpcPort,
                        envoyPort = 8081
                    ).toDefinition()
                ).build()
            )
            logger.info("Successfully attached to Controller.")
            return true
        } catch (e: Exception) {
            logger.error(e)
            return false
        }
    }

    fun enforceAttach(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            var attached = attach()
            while (isActive) {
                if (attached) {
                    if (!channel.getState(true).equals(ConnectivityState.READY)) {
                        attached = false
                    }
                } else {
                    logger.warn("Could not attach to controller, retrying...")
                    attached = attach()
                }
                delay(5000L)
            }
        }
    }

}