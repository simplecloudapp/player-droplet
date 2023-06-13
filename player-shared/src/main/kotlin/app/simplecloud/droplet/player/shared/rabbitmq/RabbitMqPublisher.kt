package app.simplecloud.droplet.player.shared.rabbitmq

import app.simplecloud.droplet.player.proto.MessageBody
import com.google.protobuf.Any
import com.google.protobuf.Message

class RabbitMqPublisher(
    private val queues: List<String>
) {

    private val connection = RabbitMqFactory.createConnectionFromEnv()
    private val channel = connection.createChannel()

    fun start() {
        queues.forEach {queueName ->
            channel.queueDeclare(queueName, false, false, false, null)
        }
    }

    fun send(queueName: String, message: Message) {
        val messageBody = MessageBody.newBuilder()
            .setType(message.descriptorForType.fullName)
            .setMessageData(Any.pack(message))
            .build()
        channel.basicPublish("", queueName, null, messageBody.toByteArray())
    }

}