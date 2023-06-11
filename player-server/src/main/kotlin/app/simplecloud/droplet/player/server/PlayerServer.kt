package app.simplecloud.droplet.player.server

import app.simplecloud.droplet.player.server.mongo.MorphiaDatastoreFactory
import app.simplecloud.droplet.player.server.redis.RedisFactory
import app.simplecloud.droplet.player.server.repository.OfflinePlayerRepository
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import app.simplecloud.droplet.player.server.repository.PlayerUniqueIdRepository
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import kotlin.concurrent.thread

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    private val server = createGrpcServerFromEnv()
    private val jedisPool = RedisFactory.createFromEnv()
    private val datastore = MorphiaDatastoreFactory.createFromEnv()

    private val playerUniqueIdRepository = PlayerUniqueIdRepository(jedisPool)
    private val onlinePlayerRepository = OnlinePlayerRepository(jedisPool, playerUniqueIdRepository)
    private val offlinePlayerRepository = OfflinePlayerRepository(datastore)

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
            .addService(PlayerService(onlinePlayerRepository, offlinePlayerRepository))
            .build()
    }

}