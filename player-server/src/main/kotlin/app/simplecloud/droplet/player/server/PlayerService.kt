package app.simplecloud.droplet.player.server

import app.simplecloud.droplet.player.proto.*
import io.grpc.stub.StreamObserver

class PlayerService : PlayerServiceGrpc.PlayerServiceImplBase() {

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

    }

    override fun getCloudPlayerByName(
        request: GetCloudPlayerByNameRequest,
        responseObserver: StreamObserver<GetCloudPlayerResponse>
    ) {

    }

    override fun getOnlineCloudPlayers(
        request: GetOnlineCloudPlayersRequest,
        responseObserver: StreamObserver<GetOnlineCloudPlayersResponse>
    ) {

    }

    override fun getOnlineCloudPlayerCount(
        request: GetOnlineCloudPlayerCountRequest,
        responseObserver: StreamObserver<GetOnlineCloudPlayerCountResponse>
    ) {

    }

    override fun getOnlineStatus(
        request: GetOnlineStatusRequest,
        responseObserver: StreamObserver<GetOnlineStatusResponse>
    ) {

    }

}