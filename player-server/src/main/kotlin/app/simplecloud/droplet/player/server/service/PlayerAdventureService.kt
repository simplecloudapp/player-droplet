package app.simplecloud.droplet.player.server.service

import app.simplecloud.droplet.player.proto.PlayerAdventureServiceGrpc
import app.simplecloud.droplet.player.proto.SendMessageEvent
import app.simplecloud.droplet.player.proto.SendMessageRequest
import app.simplecloud.droplet.player.proto.SendMessageResponse
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqPublisher
import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager

class PlayerAdventureService(
    private val publisher: RabbitMqPublisher,
    private val onlinePlayerRepository: OnlinePlayerRepository,
) : PlayerAdventureServiceGrpc.PlayerAdventureServiceImplBase() {

    override fun sendMessage(request: SendMessageRequest, responseObserver: StreamObserver<SendMessageResponse>) {
        val cloudPlayer = onlinePlayerRepository.findByUniqueId(request.uniqueId)
        if (cloudPlayer == null) {
            LOGGER.warn("CloudPlayer with uniqueId ${request.uniqueId} is not online")
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
            return
        }

        publisher.publish(RabbitMqChannelNames.ADVENTURE, SendMessageEvent.newBuilder().mergeFrom(request.toByteArray()).build())
        LOGGER.info("Sent message to ${cloudPlayer.name}: ${request.message}")

        responseObserver.onNext(SendMessageResponse.newBuilder().build())
        responseObserver.onCompleted()
    }

    companion object {
        private val LOGGER = LogManager.getLogger(PlayerAdventureService::class.java)
    }

}