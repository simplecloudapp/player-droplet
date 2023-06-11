package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.api.impl.configuration.CloudPlayerConfigurationWrapper
import app.simplecloud.droplet.player.api.impl.configuration.OfflineCloudPlayerConfigurationWrapper
import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.shared.future.toCompletable
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.*
import java.util.concurrent.CompletableFuture

class PlayerApiImpl : PlayerApi {

    private val playerServiceStub =
        PlayerServiceGrpc.newFutureStub(createManagedChannelFromEnv())

    override fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer> {
        return playerServiceStub.getOfflineCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        ).toCompletable().thenApply {
            val configurationWrapper = OfflineCloudPlayerConfigurationWrapper.fromConfiguration(it.offlineCloudPlayer)
            OfflineCloudPlayerImpl(configurationWrapper)
        }
    }

    override fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer> {
        return playerServiceStub.getOfflineCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        ).toCompletable().thenApply {
            val configurationWrapper = OfflineCloudPlayerConfigurationWrapper.fromConfiguration(it.offlineCloudPlayer)
            OfflineCloudPlayerImpl(configurationWrapper)
        }
    }

    override fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer> {
        return playerServiceStub.getCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        ).toCompletable().thenApply {
            val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(it.cloudPlayer)
            CloudPlayerImpl(playerServiceStub, configurationWrapper)
        }
    }

    override fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer> {
        return playerServiceStub.getCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        ).toCompletable().thenApply {
            val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(it.cloudPlayer)
            CloudPlayerImpl(playerServiceStub, configurationWrapper)
        }
    }

    override fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>> {
        return playerServiceStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())
            .toCompletable().thenApply {
                it.onlineCloudPlayersList.map { cloudPlayer ->
                    val configurationWrapper = CloudPlayerConfigurationWrapper.fromConfiguration(cloudPlayer)
                    CloudPlayerImpl(playerServiceStub, configurationWrapper)
                }
            }
    }

    override fun getOnlinePlayerCount(): CompletableFuture<Int> {
        return playerServiceStub.getOnlineCloudPlayerCount(GetOnlineCloudPlayerCountRequest.getDefaultInstance())
            .toCompletable().thenApply {
                it.count
            }
    }

    override fun connectPlayer(uniqueId: UUID, serverName: String): CompletableFuture<CloudPlayerConnectResult> {
        return playerServiceStub.connectCloudPlayer(
            ConnectCloudPlayerRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .setServerName(serverName)
                .build()
        ).toCompletable().thenApply {
            it.result
        }
    }

    override fun isOnline(uniqueId: UUID): CompletableFuture<Boolean> {
        return playerServiceStub.getOnlineStatus(
            GetOnlineStatusRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        ).toCompletable().thenApply {
            it.online
        }
    }

    private fun createManagedChannelFromEnv(): ManagedChannel {
        val host = System.getenv("PLAYER_DROPLET_HOST") ?: "localhost"
        val port = System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 50051
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    }

}