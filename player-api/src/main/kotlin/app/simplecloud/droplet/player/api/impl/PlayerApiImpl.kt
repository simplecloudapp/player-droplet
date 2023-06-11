package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
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

            OfflineCloudPlayerImpl()
        }
    }

    override fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getCloudPlayerByUniqueId(
                GetCloudPlayerByUniqueIdRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .build()
            )

            OfflineCloudPlayerImpl()
        }
    }

    override fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getCloudPlayerByName(
                GetCloudPlayerByNameRequest.newBuilder()
                    .setName(name)
                    .build()
            )

            CloudPlayerImpl(playerServiceStub)
        }
    }

    override fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getCloudPlayerByUniqueId(
                GetCloudPlayerByUniqueIdRequest.newBuilder()
                    .setUniqueId(uniqueId.toString())
                    .build()
            )

            CloudPlayerImpl(playerServiceStub)
        }
    }

    override fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>> {
        return CompletableFuture.supplyAsync {
            val response = playerServiceStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())

            response.onlineCloudPlayersList.map { CloudPlayerImpl(playerServiceStub) }
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