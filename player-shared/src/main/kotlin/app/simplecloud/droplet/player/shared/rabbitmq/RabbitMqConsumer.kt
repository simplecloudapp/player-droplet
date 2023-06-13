package app.simplecloud.droplet.player.shared.rabbitmq

import app.simplecloud.droplet.player.proto.MessageBody
import com.google.protobuf.Message
import com.rabbitmq.client.Delivery
import kotlin.concurrent.thread

class RabbitMqConsumer(
    private val queues: List<String>
) {

    private val connection = RabbitMqFactory.createConnectionFromEnv()
    private val channel = connection.createChannel()

    private val listeners = mutableMapOf<MessageListenerKey, MessageListenerValue<out Message>>()

    fun start() {
        queues.forEach {queueName ->
            thread {
                val deliverCallback: (String?, Delivery) -> Unit = { consumerTag: String?, delivery: Delivery ->
                    val message = MessageBody.parseFrom(delivery.body)
                    val dataType = message.type
                    val listener = listeners[MessageListenerKey(queueName, dataType)]
                    listener?.let {
                        val messageData = message.messageData.unpack(it.dataType)
                        println(" [x] Data $dataType'")
                        it.handle(messageData)
                    }

                }

                channel.basicConsume(queueName, true, deliverCallback) { _: String? -> }
            }
        }
    }

    fun <T : Message> listen(queueName: String, dataType: Class<T>, listener: RabbitMqListener<T>) {
        val key = MessageListenerKey(queueName, dataType.name)
        listeners[key] = MessageListenerValue(dataType, listener)
        channel.queueDeclare(queueName, false, false, false, null)

    }

    data class MessageListenerKey(
        val queueName: String,
        val dataType: String
    )

    data class MessageListenerValue<T: Message>(
        val dataType: Class<out Message>,
        val listener: RabbitMqListener<T>
    ) {

        fun handle(message: Message) {
            listener.handle(message as T)
        }

    }

}