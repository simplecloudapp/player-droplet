package app.simplecloud.droplet.player.server.service

import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.server.connection.PlayerLoginHandler
import app.simplecloud.droplet.player.server.connection.PlayerLogoutHandler
import app.simplecloud.droplet.player.server.repository.OfflinePlayerRepository
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import io.grpc.stub.StreamObserver

class PlayerService(
    private val onlinePlayerRepository: OnlinePlayerRepository,
    private val offlinePlayerRepository: OfflinePlayerRepository,
    private val playerLoginHandler: PlayerLoginHandler,
    private val playerLogoutHandler: PlayerLogoutHandler
) : PlayerServiceGrpc.PlayerServiceImplBase() {

    override fun getOfflineCloudPlayerByUniqueId(
        request: GetCloudPlayerByUniqueIdRequest,
        responseObserver: StreamObserver<GetOfflineCloudPlayerResponse>
    ) {
        val offlinePlayer = offlinePlayerRepository.findByUniqueId(request.uniqueId)
        if (offlinePlayer == null) {
            responseObserver.onError(IllegalArgumentException("OfflineCloudPlayer with uniqueId ${request.uniqueId} not found"))
            return
        }

        responseObserver.onNext(
            GetOfflineCloudPlayerResponse.newBuilder()
                .setOfflineCloudPlayer(offlinePlayer.toConfiguration())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getOfflineCloudPlayerByName(
        request: GetCloudPlayerByNameRequest,
        responseObserver: StreamObserver<GetOfflineCloudPlayerResponse>
    ) {
        val offlinePlayer = offlinePlayerRepository.findByName(request.name)
        if (offlinePlayer == null) {
            responseObserver.onError(IllegalArgumentException("OfflineCloudPlayer with name ${request.name} not found"))
            return
        }

        responseObserver.onNext(
            GetOfflineCloudPlayerResponse.newBuilder()
                .setOfflineCloudPlayer(offlinePlayer.toConfiguration())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getCloudPlayerByUniqueId(
        request: GetCloudPlayerByUniqueIdRequest,
        responseObserver: StreamObserver<GetCloudPlayerResponse>
    ) {
        val cloudPlayer = onlinePlayerRepository.findByUniqueId(request.uniqueId)
        if (cloudPlayer == null) {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} not found"))
            return
        }

        responseObserver.onNext(
            GetCloudPlayerResponse.newBuilder()
                .setCloudPlayer(cloudPlayer)
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getCloudPlayerByName(
        request: GetCloudPlayerByNameRequest,
        responseObserver: StreamObserver<GetCloudPlayerResponse>
    ) {
        val cloudPlayer = onlinePlayerRepository.findByName(request.name)
        if (cloudPlayer == null) {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with name ${request.name} not found"))
            return
        }

        responseObserver.onNext(
            GetCloudPlayerResponse.newBuilder()
                .setCloudPlayer(cloudPlayer)
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getOnlineCloudPlayers(
        request: GetOnlineCloudPlayersRequest,
        responseObserver: StreamObserver<GetOnlineCloudPlayersResponse>
    ) {
        responseObserver.onNext(
            GetOnlineCloudPlayersResponse.newBuilder()
                .addAllOnlineCloudPlayers(onlinePlayerRepository.findAll())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getOnlineCloudPlayerCount(
        request: GetOnlineCloudPlayerCountRequest,
        responseObserver: StreamObserver<GetOnlineCloudPlayerCountResponse>
    ) {
        responseObserver.onNext(
            GetOnlineCloudPlayerCountResponse.newBuilder()
                .setCount(onlinePlayerRepository.count())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getOnlineStatus(
        request: GetOnlineStatusRequest,
        responseObserver: StreamObserver<GetOnlineStatusResponse>
    ) {
        val cloudPlayer = onlinePlayerRepository.findByUniqueId(request.uniqueId)

        responseObserver.onNext(
            GetOnlineStatusResponse.newBuilder()
                .setOnline(cloudPlayer != null)
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun loginCloudPlayer(
        request: CloudPlayerLoginRequest,
        responseObserver: StreamObserver<CloudPlayerLoginResponse>
    ) {
        val success = playerLoginHandler.handleLogin(request)

        responseObserver.onNext(
            CloudPlayerLoginResponse.newBuilder()
                .setSuccess(success)
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun disconnectCloudPlayer(
        request: CloudPlayerDisconnectRequest,
        responseObserver: StreamObserver<CloudPlayerDisconnectResponse>
    ) {
        val success = playerLogoutHandler.handleLogout(request)

        responseObserver.onNext(
            CloudPlayerDisconnectResponse.newBuilder()
                .setSuccess(success)
                .build()
        )
        responseObserver.onCompleted()
    }

}