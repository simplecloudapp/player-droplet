package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.PlayerApi
import app.simplecloud.droplet.player.proto.PlayerServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.*
import java.util.concurrent.CompletableFuture

class PlayerApiImpl : PlayerApi {

    private val playerServiceStub =
        PlayerServiceGrpc.newBlockingStub(createManagedChannelFromEnv())

    override fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer> {
        TODO("Not yet implemented")
    }

    override fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer> {
        TODO("Not yet implemented")
    }

    override fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer> {
        TODO("Not yet implemented")
    }

    override fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer> {
        TODO("Not yet implemented")
    }

    override fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>> {
        TODO("Not yet implemented")
    }

    override fun getOnlinePlayerCount(): CompletableFuture<Int> {
        TODO("Not yet implemented")
    }

    override fun connectPlayer(uniqueId: UUID, serverName: String): CompletableFuture<Boolean> {
        TODO("Not yet implemented")
    }

    override fun isOnline(uniqueId: UUID): CompletableFuture<Boolean> {
        TODO("Not yet implemented")
    }

    private fun createManagedChannelFromEnv(): ManagedChannel {
        val host = System.getenv("PLAYER_DROPLET_HOST") ?: "localhost"
        val port = System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 50051
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    }

}