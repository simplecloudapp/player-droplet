package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.api.impl.configuration.CloudPlayerConfigurationWrapper
import app.simplecloud.droplet.player.api.impl.configuration.OfflineCloudPlayerConfigurationWrapper
import app.simplecloud.droplet.player.proto.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.*
import java.util.concurrent.CompletableFuture

class PlayerApiImpl : PlayerApi {

    private val playerServiceStub =
        PlayerServiceGrpc.newBlockingStub(createManagedChannelFromEnv())

    override fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOfflineCloudPlayerByName(
                GetCloudPlayerByNameRequest.newBuilder()
                    .setName(name)
                    .build()
            )

            val configurationWrapper = OfflineCloudPlayerConfigurationWrapper.fromConfiguration(response.offlineCloudPlayer)
            OfflineCloudPlayerImpl(configurationWrapper)
        }
    }

    override fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOfflineCloudPlayerByUniqueId(
                GetCloudPlayerByUniqueIdRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .build()
            )

            val configurationWrapper = OfflineCloudPlayerConfigurationWrapper.fromConfiguration(response.offlineCloudPlayer)
            OfflineCloudPlayerImpl(configurationWrapper)
        }
    }

    override fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getCloudPlayerByName(
                GetCloudPlayerByNameRequest.newBuilder()
                    .setName(name)
                    .build()
            )

            val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(response.cloudPlayer)
            CloudPlayerImpl(playerServiceStub, configurationWrapper)
        }
    }

    override fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getCloudPlayerByUniqueId(
                GetCloudPlayerByUniqueIdRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .build()
            )

            val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(response.cloudPlayer)
            CloudPlayerImpl(playerServiceStub, configurationWrapper)
        }
    }

    override fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())

            response.onlineCloudPlayersList.map {
                val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(it)
                CloudPlayerImpl(playerServiceStub, configurationWrapper)
            }
        }
    }

    override fun getOnlinePlayerCount(): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())

            response.onlineCloudPlayersCount
        }
    }

    override fun connectPlayer(uniqueId: UUID, serverName: String): CompletableFuture<CloudPlayerConnectResult> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.connectCloudPlayer(
                ConnectCloudPlayerRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .setServerName(serverName)
                    .build()
            )

            response.result
        }
    }

    override fun isOnline(uniqueId: UUID): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOnlineStatus(
                GetOnlineStatusRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .build()
            )

            response.online
        }
    }

    private fun createManagedChannelFromEnv(): ManagedChannel {
        val host = System.getenv("PLAYER_DROPLET_HOST") ?: "localhost"
        val port = System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 50051
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    }

}