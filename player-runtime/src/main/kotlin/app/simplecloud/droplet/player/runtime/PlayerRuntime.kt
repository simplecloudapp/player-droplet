package app.simplecloud.droplet.player.runtime

import app.simplecloud.droplet.api.auth.AuthCallCredentials
import app.simplecloud.droplet.api.auth.AuthSecretInterceptor
import app.simplecloud.droplet.api.droplet.Droplet
import app.simplecloud.droplet.player.runtime.connection.PlayerConnectionHandler
import app.simplecloud.droplet.player.runtime.database.DatabaseFactory
import app.simplecloud.droplet.player.runtime.launcher.PlayerDropletStartCommand
import app.simplecloud.droplet.player.runtime.repository.JooqPlayerRepository
import app.simplecloud.droplet.player.runtime.service.PlayerAdventureService
import app.simplecloud.droplet.player.runtime.service.PlayerService
import app.simplecloud.pubsub.PubSubClient
import app.simplecloud.pubsub.PubSubService
import build.buf.gen.simplecloud.controller.v1.ControllerDropletServiceGrpcKt
import build.buf.gen.simplecloud.controller.v1.RegisterDropletRequest
import io.grpc.ManagedChannelBuilder
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import java.net.InetAddress
import kotlin.concurrent.thread

class PlayerRuntime(
    private val startCommand: PlayerDropletStartCommand
) {

    private val logger = LogManager.getLogger(PlayerRuntime::class.java)

    private val database = DatabaseFactory.createDatabase(startCommand.databaseUrl)
    private val jooqPlayerRepository = JooqPlayerRepository(database)
    private val pubSubClient = PubSubClient(startCommand.pubSubGrpcHost, startCommand.pubSubGrpcPort)
    private val playerConnectionHandler = PlayerConnectionHandler(jooqPlayerRepository)
    private val authCallCredentials = AuthCallCredentials(startCommand.authSecret)


    private val server = createGrpcServerFromEnv()

    private val pubSubServer = createPubSubGrpcServerFromEnv()

    fun setupDatabase() {
        logger.info("Setting up database...")
        database.setup()
    }

    suspend fun start() {
        logger.info("Starting Player server...")
        setupDatabase()
        startGrpcServer()
        startPubSubGrpcServer()
        registerDroplet()
    }

    suspend fun registerDroplet() {
        if (startCommand.authSecret != "none") {
            val controllerDropletStub =
                ControllerDropletServiceGrpcKt.ControllerDropletServiceCoroutineStub(
                    ManagedChannelBuilder.forAddress(startCommand.controllerHost, startCommand.controllerPort).usePlaintext()
                    .build())
                    .withCallCredentials(authCallCredentials)

            controllerDropletStub.registerDroplet(
                RegisterDropletRequest.newBuilder().setDefinition(
                    Droplet(
                        type = "players",
                        host = startCommand.grpcHost,
                        id = InetAddress.getLocalHost().hostName,
                        port = startCommand.grpcPort,
                        envoyPort = 8081
                    ).toDefinition()
                ).build()
            )
        }
    }

    private fun startGrpcServer() {
        logger.info("Starting gRPC server on ${startCommand.grpcHost} with port ${startCommand.grpcPort}")
        thread {
            server.start()
            server.awaitTermination()
        }
    }

    private fun startPubSubGrpcServer() {
        logger.info("Starting PubSub gRPC server on ${startCommand.pubSubGrpcHost} with port ${startCommand.pubSubGrpcPort}")
        thread {
            pubSubServer.start()
            pubSubServer.awaitTermination()
        }
    }

    private fun createGrpcServerFromEnv(): Server {
        val port = startCommand.grpcPort
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
        val port = startCommand.pubSubGrpcPort
        return ServerBuilder.forPort(port)
            .addService(PubSubService())
            .build()
    }

}