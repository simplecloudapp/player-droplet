package app.simplecloud.droplet.player.server

import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import kotlin.concurrent.thread

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    private val server = createGrpcServerFromEnv()

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

    private fun createGrpcServerFromEnv(): Server {
        val port = System.getenv("GRPC_PORT")?.toInt() ?: 5816
        return ServerBuilder.forPort(port)
            .addService(PlayerService())
            .build()
    }

}