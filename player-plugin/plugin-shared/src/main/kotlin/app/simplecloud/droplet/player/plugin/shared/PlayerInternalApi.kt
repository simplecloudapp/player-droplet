package app.simplecloud.droplet.player.plugin.shared

import app.simplecloud.droplet.player.api.impl.PlayerApiImpl
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqFactory
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubClient
import com.google.protobuf.Message

open class PlayerInternalApi(
    private val onlinePlayerChecker: OnlinePlayerChecker
) : PlayerApiImpl() {

    val pubSubClient = PubSubClient(System.getenv("PLAYER_DROPLET_HOST") ?: "127.0.0.1", System.getenv("PLAYER_DROPLET_PORT")?.toInt() ?: 5817)

    fun <T: Message> registerRabbitMqListener(queueName: String, clazz: Class<out Message>, listener: RabbitMqListener<T>) {
        /*
        {
            val uniqueId = it.allFields.entries.find { it.key.name == "uniqueId" }?.value?.toString()?: return@listen true
            println("Checking if player with uniqueId $uniqueId is online")
            onlinePlayerChecker.isOnline(uniqueId)
        }
         */
        pubSubClient.subscribe(queueName, clazz) {
            listener.handle(it as T)
        }
    }

}