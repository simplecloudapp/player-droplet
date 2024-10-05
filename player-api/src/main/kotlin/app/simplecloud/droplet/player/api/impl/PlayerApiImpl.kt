package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.shared.future.toCompletable
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.*
import java.util.concurrent.CompletableFuture

open class PlayerApiImpl : PlayerApi {

    private val managedChannel = createManagedChannelFromEnv()

    protected val playerServiceStub: PlayerServiceGrpc.PlayerServiceFutureStub =
        PlayerServiceGrpc.newFutureStub(managedChannel)

    protected val playerServiceAdventureStub: PlayerAdventureServiceGrpc.PlayerAdventureServiceFutureStub =
        PlayerAdventureServiceGrpc.newFutureStub(managedChannel)

    override fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer> {
        return playerServiceStub.getOfflineCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        ).toCompletable().thenApply {
            OfflineCloudPlayerImpl(it.offlineCloudPlayer)
        }
    }

    override fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer> {
        return playerServiceStub.getOfflineCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        ).toCompletable().thenApply {
            OfflineCloudPlayerImpl(it.offlineCloudPlayer)
        }
    }

    override fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer> {
        return playerServiceStub.getCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        ).toCompletable().thenApply {
            CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, it.cloudPlayer)
        }
    }

    override fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer> {
        return playerServiceStub.getCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        ).toCompletable().thenApply {
            CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, it.cloudPlayer)
        }
    }

    override fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>> {
        return playerServiceStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())
            .toCompletable().thenApply {
                it.onlineCloudPlayersList.map { cloudPlayer ->
                    CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, cloudPlayer)
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
        return playerServiceStub.connectCloudPlayerToServer(
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
        val host = System.getenv("PLAYER_DROPLET_HOST") ?: "127.0.0.1"
        val port = System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 5817
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    }

}