package app.simplecloud.droplet.player.runtime.launcher

import app.simplecloud.droplet.api.secret.AuthFileSecretFactory
import app.simplecloud.droplet.player.runtime.PlayerRuntime
import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.clikt.sources.PropertiesValueSource
import com.github.ajalt.clikt.sources.ValueSource
import java.io.File
import java.nio.file.Path


class PlayerDropletStartCommand : SuspendingCliktCommand() {

    init {
        context {
            valueSource = PropertiesValueSource.from(File("players.properties"), false, ValueSource.envvarKey())
        }
    }

    private val defaultDatabaseUrl = "jdbc:sqlite:player.db"

    val grpcHost: String by option(help = "Grpc host (default: localhost)", envvar = "GRPC_HOST").default("localhost")
    val grpcPort: Int by option(help = "Grpc port (default: 5826)", envvar = "GRPC_PORT").int().default(5826)

    val id: String by option(help = "ID", envvar = "ID").default("internal-player")


    val pubSubGrpcHost: String by option(
        help = "PubSub Grpc host (default: localhost)",
        envvar = "GRPC_PUBSUB_HOST"
    ).default("localhost")
    val pubSubGrpcPort: Int by option(
        help = "PubSub Grpc port (default: 5827)",
        envvar = "GRPC_PUB_SUB_PORT"
    ).int().default(5827)

    val databaseUrl: String by option(help = "Database URL (default: ${defaultDatabaseUrl})", envvar = "DATABASE_URL")
        .default(defaultDatabaseUrl)

    private val authSecretPath: Path by option(
        help = "Path to auth secret file (default: .auth.secret)",
        envvar = "AUTH_SECRET_PATH"
    )
        .path()
        .default(Path.of(".secrets", "auth.secret"))

    val authSecret: String by option(help = "Auth secret", envvar = "AUTH_SECRET_KEY")
        .defaultLazy { AuthFileSecretFactory.loadOrCreate(authSecretPath) }

    val controllerHost: String by option(help = "Controller host", envvar = "CONTROLLER_HOST")
        .default("localhost")

    val controllerPort: Int by option(help = "Controller port", envvar = "CONTROLLER_PORT").int().default(5816)

    val authType: AuthType by option(help = "Auth type (default: SECRET)", envvar = "AUTH_TYPE").enum<AuthType>()
        .default(AuthType.SECRET)

    override suspend fun run() {
        val playerRuntime = PlayerRuntime(this)
        playerRuntime.start()

    }
}