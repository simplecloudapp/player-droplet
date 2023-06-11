package app.simplecloud.droplet.player.server.mongo

import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia

object MorphiaDatastoreFactory {

    fun createFromEnv(): Datastore {
        val connectionString = System.getenv("MONGO_CONNECTION_STRING") ?: "mongodb://localhost"
        val database = System.getenv("MONGO_DATABASE") ?: "players"
        return Morphia.createDatastore(MongoClients.create(connectionString), database)
    }

}