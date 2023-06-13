package app.simplecloud.droplet.player.server.service

import app.simplecloud.droplet.player.proto.PlayerAdventureServiceGrpc
import app.simplecloud.droplet.player.proto.SendMessageEvent
import app.simplecloud.droplet.player.proto.SendMessageRequest
import app.simplecloud.droplet.player.proto.SendMessageResponse
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqPublisher
import io.grpc.stub.StreamObserver

class PlayerAdventureService(
    private val publisher: RabbitMqPublisher,
    private val onlinePlayerRepository: OnlinePlayerRepository,
) : PlayerAdventureServiceGrpc.PlayerAdventureServiceImplBase() {

    override fun sendMessage(request: SendMessageRequest, responseObserver: StreamObserver<SendMessageResponse>) {
        val cloudPlayer = onlinePlayerRepository.findByUniqueId(request.uniqueId)
        if (cloudPlayer == null) {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} not found"))
            return
        }

        publisher.publish(RabbitMqChannelNames.ADVENTURE, SendMessageEvent.newBuilder().mergeFrom(request).build())

        responseObserver.onNext(SendMessageResponse.newBuilder().build())
        responseObserver.onCompleted()
    }

}