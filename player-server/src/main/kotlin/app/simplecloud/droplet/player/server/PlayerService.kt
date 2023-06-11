package app.simplecloud.droplet.player.server

import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import com.google.rpc.Status
import io.grpc.stub.StreamObserver

class PlayerService(
    private val onlinePlayerRepository: OnlinePlayerRepository
) : PlayerServiceGrpc.PlayerServiceImplBase() {

    override fun getOfflineCloudPlayerByUniqueId(
        request: GetCloudPlayerByUniqueIdRequest,
        responseObserver: StreamObserver<GetOfflineCloudPlayerResponse>
    ) {

    }

    override fun getOfflineCloudPlayerByName(
        request: GetCloudPlayerByNameRequest,
        responseObserver: StreamObserver<GetOfflineCloudPlayerResponse>
    ) {

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

    override fun sendMessage(request: SendMessageRequest?, responseObserver: StreamObserver<SendMessageResponse>?) {

    }

}