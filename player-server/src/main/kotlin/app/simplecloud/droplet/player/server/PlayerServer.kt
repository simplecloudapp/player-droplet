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
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqFactory
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import kotlin.concurrent.thread

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    private val jedisPool = RedisFactory.createFromEnv()
    private val datastore = MorphiaDatastoreFactory.createFromEnv()
    private val publisher = RabbitMqFactory.createPublisher()

    private val playerUniqueIdRepository = PlayerUniqueIdRepository(jedisPool)
    private val onlinePlayerRepository = OnlinePlayerRepository(jedisPool, playerUniqueIdRepository)
    private val offlinePlayerRepository = OfflinePlayerRepository(datastore)
    private val playerLoginHandler = PlayerLoginHandler(offlinePlayerRepository, onlinePlayerRepository)
    private val playerLogoutHandler = PlayerLogoutHandler(offlinePlayerRepository, onlinePlayerRepository)

    private val server = createGrpcServerFromEnv()

    fun start() {
        logger.info("Starting Player server...")
        startGrpcServer()
        publisher.start()
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
            .addService(
                PlayerService(
                        publisher,
                    onlinePlayerRepository,
                    offlinePlayerRepository,
                    playerLoginHandler,
                    playerLogoutHandler
                )
            )
            .addService(PlayerAdventureService(publisher, onlinePlayerRepository))
            .build()
    }

}