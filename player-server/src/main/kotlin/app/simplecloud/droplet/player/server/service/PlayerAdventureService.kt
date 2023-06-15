package app.simplecloud.droplet.player.server.service

import app.simplecloud.droplet.player.proto.*
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

        publisher.publish(
            RabbitMqChannelNames.ADVENTURE,
            SendMessageEvent.newBuilder().mergeFrom(request.toByteArray()).build()
        )
        LOGGER.info("Sent message to ${cloudPlayer.name}: ${request.message}")

        responseObserver.onNext(SendMessageResponse.newBuilder().build())
        responseObserver.onCompleted()
    }

    override fun sendActionbar(
        request: SendActionbarRequest,
        responseObserver: StreamObserver<SendActionbarResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendActionbarEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendActionbarResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }


    override fun sendPlayerListHeaderAndFooter(
        request: SendPlayerListHeaderAndFooterRequest,
        responseObserver: StreamObserver<SendPlayerListHeaderAndFooterResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendPlayerListHeaderAndFooterEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendPlayerListHeaderAndFooterResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendBossBar(request: SendBossBarRequest, responseObserver: StreamObserver<SendBossBarResponse>) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendBossBarEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendBossBarResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendBossBarRemove(
        request: SendBossBarHideRequest,
        responseObserver: StreamObserver<SendBossBarHideResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendBossBarHideEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendBossBarHideResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendClearTitle(
        request: SendClearTitleRequest,
        responseObserver: StreamObserver<SendClearTitleResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendClearTitleEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendClearTitleResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendOpenBook(request: SendOpenBookRequest, responseObserver: StreamObserver<SendOpenBookResponse>) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendOpenBookEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendOpenBookResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendPlaySound(
        request: SendPlaySoundRequest,
        responseObserver: StreamObserver<SendPlaySoundResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendPlaySoundEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendPlaySoundResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendPlaySoundToCoordinates(
        request: SendPlaySoundToCoordinatesRequest,
        responseObserver: StreamObserver<SendPlaySoundToCoordinatesResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendPlaySoundToCoordinatesEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendPlaySoundToCoordinatesResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendResetTitle(
        request: SendResetTitleRequest,
        responseObserver: StreamObserver<SendResetTitleResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendResetTitleEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendResetTitleResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendStopSound(
        request: SendStopSoundRequest,
        responseObserver: StreamObserver<SendStopSoundResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendStopSoundEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )
            responseObserver.onNext(SendStopSoundResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendTitlePartSubTitle(
        request: SendTitlePartSubTitleRequest,
        responseObserver: StreamObserver<SendTitlePartSubTitleResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendTitlePartSubTitleEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )

            LOGGER.info("SendTitlePartSubTitle")

            responseObserver.onNext(SendTitlePartSubTitleResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendTitlePartTitle(
        request: SendTitlePartTitleRequest,
        responseObserver: StreamObserver<SendTitlePartTitleResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendTitlePartTitleEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )

            LOGGER.info("SendTitlePartTitle")

            responseObserver.onNext(SendTitlePartTitleResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }

    override fun sendTitlePartTimes(
        request: SendTitlePartTimesRequest,
        responseObserver: StreamObserver<SendTitlePartTimesResponse>
    ) {
        if (playerIsOnline(request.uniqueId)) {
            publisher.publish(
                RabbitMqChannelNames.ADVENTURE,
                SendTitlePartTimesEvent.newBuilder().mergeFrom(request.toByteArray()).build()
            )

            LOGGER.info("SendTitlePartTimes")

            responseObserver.onNext(SendTitlePartTimesResponse.newBuilder().build())
            responseObserver.onCompleted()
        } else {
            responseObserver.onError(IllegalArgumentException("CloudPlayer with uniqueId ${request.uniqueId} is not online"))
        }
    }


    companion object {
        private val LOGGER = LogManager.getLogger(PlayerAdventureService::class.java)
    }

    private fun playerIsOnline(uniqueId: String): Boolean {
        val cloudPlayer = onlinePlayerRepository.findByUniqueId(uniqueId)
        return if (cloudPlayer == null) {
            LOGGER.warn("CloudPlayer with uniqueId $uniqueId is not online")
            false
        } else {
            true
        }
    }

}