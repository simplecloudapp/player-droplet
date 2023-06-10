package app.simplecloud.droplet.player.server

import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import kotlin.concurrent.thread

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    private val server = ServerBuilder.forPort(5816)
        .build()

    fun start() {
        logger.info("Starting Player server...")
        startGrpcServer()
    }

    private fun startGrpcServer() {
        logger.info("Starting gRPC server...")
        thread {
            server.start()
            server.awaitTermination()
        }
    }

}