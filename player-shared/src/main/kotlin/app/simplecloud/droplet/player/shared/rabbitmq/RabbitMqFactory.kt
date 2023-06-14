package app.simplecloud.droplet.player.shared.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object RabbitMqFactory {

    fun createPublisher(queues: List<String>): RabbitMqPublisher {
        return RabbitMqPublisher(queues)
    }

    fun createConsumer(queues: List<String>): RabbitMqConsumer {
        return RabbitMqConsumer(queues)
    }

    fun createConnectionFromEnv(): Connection {
        val host = System.getenv("RABBITMQ_HOST")?: "127.0.0.1"
        val username = System.getenv("RABBITMQ_USERNAME")?: "user"
        val password = System.getenv("RABBITMQ_PASSWORD")?: "bitnami"

        return createConnection(host, username, password)
    }

    fun createConnection(host: String, username: String, password: String): Connection {
        val connectionFactory = ConnectionFactory()
        connectionFactory.host = host
        connectionFactory.username = username
        connectionFactory.password = password
        connectionFactory.virtualHost = "/"
        connectionFactory.channelRpcTimeout = 0
        connectionFactory.networkRecoveryInterval = 10000
        connectionFactory.isAutomaticRecoveryEnabled = true

        return connectionFactory.newConnection()
    }

}