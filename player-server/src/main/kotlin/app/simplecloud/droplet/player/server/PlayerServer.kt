package app.simplecloud.droplet.player.server

import app.simplecloud.droplet.player.server.connection.PlayerLoginHandler
import app.simplecloud.droplet.player.server.connection.PlayerLogoutHandler
import app.simplecloud.droplet.player.server.mongo.MorphiaDatastoreFactory
import app.simplecloud.droplet.player.server.redis.RedisFactory
import app.simplecloud.droplet.player.server.repository.OfflinePlayerRepository
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import app.simplecloud.droplet.player.server.repository.PlayerUniqueIdRepository
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

    private val jedisPool = RedisFactory.createFromEnv()
    private val datastore = MorphiaDatastoreFactory.createFromEnv()
    private val pubSubClient = PubSubClient(System.getenv("GRPC_HOST") ?: "127.0.0.1", System.getenv("GRPC_PORT")?.toInt() ?: 5826)
    private val playerUniqueIdRepository = PlayerUniqueIdRepository(jedisPool)
    private val onlinePlayerRepository = OnlinePlayerRepository(jedisPool, playerUniqueIdRepository)
    private val offlinePlayerRepository = OfflinePlayerRepository(datastore)
    private val playerLoginHandler = PlayerLoginHandler(offlinePlayerRepository, onlinePlayerRepository)
    private val playerLogoutHandler = PlayerLogoutHandler(offlinePlayerRepository, onlinePlayerRepository)

    private val server = createGrpcServerFromEnv()

    fun start() {
        logger.info("Starting Player server...")
        startGrpcServer()
    }

    private fun startGrpcServer() {
        logger.info("Starting gRPC server on ${InetAddress.getLocalHost().hostAddress} with port ${System.getenv("GRPC_PORT")}")
        thread {
            server.start()
            server.awaitTermination()
        }
    }

    private fun createGrpcServerFromEnv(): Server {
        val port = System.getenv("GRPC_PORT")?.toInt() ?: 5826
        return ServerBuilder.forPort(port)
            .addService(
                PlayerService(
                    pubSubClient,
                    onlinePlayerRepository,
                    offlinePlayerRepository,
                    playerLoginHandler,
                    playerLogoutHandler
                )
            )
            .addService(PlayerAdventureService(pubSubClient, onlinePlayerRepository))
            .addService(PubSubService())
            .build()
    }

}