package app.simplecloud.droplet.player.server

import app.simplecloud.droplet.player.server.connection.PlayerConnectionHandler
import app.simplecloud.droplet.player.server.database.DatabaseFactory
import app.simplecloud.droplet.player.server.repository.JooqPlayerRepository
import app.simplecloud.droplet.player.server.service.PlayerAdventureService
import app.simplecloud.droplet.player.server.service.PlayerService
import app.simplecloud.pubsub.PubSubClient
import app.simplecloud.pubsub.PubSubService
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import java.net.InetAddress
import kotlin.concurrent.thread

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    private val database = DatabaseFactory.createDatabase(System.getenv("DATABASE_URL") ?: "jdbc:sqlite:player.db")
    private val jooqPlayerRepository = JooqPlayerRepository(database)
    private val pubSubClient = PubSubClient(System.getenv("GRPC_HOST") ?: "127.0.0.1", System.getenv("GRPC_PUB_SUB_PORT")?.toInt() ?: 5827)
    private val playerConnectionHandler = PlayerConnectionHandler(jooqPlayerRepository)

    private val server = createGrpcServerFromEnv()

    private val pubSubServer = createPubSubGrpcServerFromEnv()

    fun start() {
        logger.info("Starting Player server...")
        startGrpcServer()
        startPubSubGrpcServer()
    }

    private fun startGrpcServer() {
        logger.info("Starting gRPC server on ${InetAddress.getLocalHost().hostAddress} with port ${System.getenv("GRPC_PORT")}")
        thread {
            server.start()
            server.awaitTermination()
        }
    }

    private fun startPubSubGrpcServer() {
        logger.info("Starting PubSub gRPC server on ${InetAddress.getLocalHost().hostAddress} with port ${System.getenv("GRPC_PUB_SUB_PORT")}")
        thread {
            pubSubServer.start()
            pubSubServer.awaitTermination()
        }
    }

    private fun createGrpcServerFromEnv(): Server {
        val port = System.getenv("GRPC_PORT")?.toInt() ?: 5826
        return ServerBuilder.forPort(port)
            .addService(
                PlayerService(
                    pubSubClient,
                    jooqPlayerRepository,
                    playerConnectionHandler
                )
            )
            .addService(PlayerAdventureService(pubSubClient, jooqPlayerRepository))
            .addService(PubSubService())
            .build()
    }

    private fun createPubSubGrpcServerFromEnv(): Server {
        val port = System.getenv("GRPC_PUB_SUB_PORT")?.toInt() ?: 5827
        return ServerBuilder.forPort(port)
            .addService(PubSubService())
            .build()
    }

}