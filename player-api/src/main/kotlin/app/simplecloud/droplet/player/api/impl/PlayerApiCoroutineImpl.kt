package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.api.auth.AuthCallCredentials
import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.pubsub.PubSubClient
import build.buf.gen.simplecloud.droplet.player.v1.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.*

class PlayerApiCoroutineImpl(authSecret: String) : PlayerApi.Coroutine {

    private val authCallCredentials = AuthCallCredentials(authSecret)
    private val managedChannel = createManagedChannelFromEnv()

    private val playerServiceCoroutineStub: PlayerServiceGrpcKt.PlayerServiceCoroutineStub =
        PlayerServiceGrpcKt.PlayerServiceCoroutineStub(managedChannel)
            .withCallCredentials(authCallCredentials)

    private val playerServiceStub: PlayerServiceGrpc.PlayerServiceFutureStub =
        PlayerServiceGrpc.newFutureStub(managedChannel)
            .withCallCredentials(authCallCredentials)

    private val playerServiceAdventureStub: PlayerAdventureServiceGrpc.PlayerAdventureServiceFutureStub =
        PlayerAdventureServiceGrpc.newFutureStub(managedChannel)
            .withCallCredentials(authCallCredentials)

    private val pubSubClient = PubSubClient(
        System.getenv("PLAYER_DROPLET_PUBSUB_HOST") ?: "localhost",
        System.getenv("PLAYER_DROPLET_PUBSUB_PORT")?.toInt() ?: 5827,
        authCallCredentials,
    )

    override suspend fun getOfflinePlayer(name: String): OfflineCloudPlayer {
        val response = playerServiceCoroutineStub.getOfflineCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        )
        return OfflineCloudPlayerImpl(response.offlineCloudPlayer)
    }

    override suspend fun getOfflinePlayer(uniqueId: UUID): OfflineCloudPlayer {
        val response = playerServiceCoroutineStub.getOfflineCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        )
        return OfflineCloudPlayerImpl(response.offlineCloudPlayer)
    }

    override suspend fun getOnlinePlayer(name: String): CloudPlayer {
        val response = playerServiceCoroutineStub.getCloudPlayerByName(
            GetCloudPlayerByNameRequest.newBuilder()
                .setName(name)
                .build()
        )
        return CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, response.cloudPlayer)
    }

    override suspend fun getOnlinePlayer(uniqueId: UUID): CloudPlayer {
        val response = playerServiceCoroutineStub.getCloudPlayerByUniqueId(
            GetCloudPlayerByUniqueIdRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        )
        return CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, response.cloudPlayer)
    }

    override suspend fun getOnlinePlayers(): List<CloudPlayer> {
        val response = playerServiceCoroutineStub.getOnlineCloudPlayers(GetOnlineCloudPlayersRequest.getDefaultInstance())
        return response.onlineCloudPlayersList.map { cloudPlayer ->
            CloudPlayerImpl(playerServiceStub, playerServiceAdventureStub, cloudPlayer)
        }
    }

    override suspend fun getOnlinePlayerCount(): Int {
        val response = playerServiceCoroutineStub.getOnlineCloudPlayerCount(GetOnlineCloudPlayerCountRequest.getDefaultInstance())
        return response.count
    }

    override suspend fun connectPlayer(uniqueId: UUID, serverName: String): CloudPlayerConnectResult {
        val response = playerServiceCoroutineStub.connectCloudPlayerToServer(
            ConnectCloudPlayerRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .setServerName(serverName)
                .build()
        )
        return response.result
    }

    override suspend fun isOnline(uniqueId: UUID): Boolean {
        val response = playerServiceCoroutineStub.getOnlineStatus(
            GetOnlineStatusRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .build()
        )
        return response.online
    }

    override suspend fun updateServer(uniqueId: UUID, serverName: String): Boolean {
        val response = playerServiceCoroutineStub.updateCloudPlayerServer(
            UpdateCloudPlayerServerRequest.newBuilder()
                .setUniqueId(uniqueId.toString())
                .setServerName(serverName)
                .build()
        )
        return response.success
    }

    override fun getPubSubClient(): PubSubClient = pubSubClient

    private fun createManagedChannelFromEnv(): ManagedChannel {
        val host = System.getenv("PLAYER_DROPLET_HOST") ?: "127.0.0.1"
        val port = System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 5826
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    }
}
