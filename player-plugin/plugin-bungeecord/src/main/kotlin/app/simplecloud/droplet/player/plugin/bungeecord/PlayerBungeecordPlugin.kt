package app.simplecloud.droplet.player.plugin.bungeecord

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.bungeecord.command.BungeeCordProxyHandler
import app.simplecloud.droplet.player.plugin.bungeecord.command.BungeeCordSender
import app.simplecloud.droplet.player.plugin.bungeecord.connection.CloudPlayerConnectListener
import app.simplecloud.droplet.player.plugin.bungeecord.connection.CloudPlayerKickListener
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.bungeecord.listener.PlayerDisconnectListener
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.command.CloudSender
import app.simplecloud.droplet.player.plugin.shared.command.PlayerCommand
import app.simplecloud.droplet.player.plugin.shared.config.MessageConfig
import app.simplecloud.droplet.player.plugin.shared.config.YamlConfig
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerKickEvent
import build.buf.gen.simplecloud.droplet.player.v1.ConnectCloudPlayerEvent
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Plugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.bungee.BungeeCommandManager
import org.incendo.cloud.execution.ExecutionCoordinator
import java.util.concurrent.TimeUnit
import kotlin.io.path.pathString

class PlayerBungeecordPlugin : Plugin() {

    private lateinit var commandManager: BungeeCommandManager<CloudSender>

    private val playerApi = PlayerProxyApi(
        OnlinePlayerChecker { proxy.getPlayer(it) != null }
    )

    override fun onEnable() {
        PlayerApiSingleton.init(playerApi)
        proxy.pluginManager.registerListener(this, PlayerConnectionListener(playerApi, plugin = this))
        proxy.pluginManager.registerListener(this, PlayerDisconnectListener(playerApi))

        val yamlConfig: YamlConfig = YamlConfig(this.dataFolder.path)
        val messageConfiguration = yamlConfig.load<MessageConfig>("messages")!!
        yamlConfig.save("messages", messageConfiguration)

        val executionCoordinator = ExecutionCoordinator.simpleCoordinator<CloudSender>()

        val senderMapper = SenderMapper.create<CommandSender, CloudSender>(
            { commandSender -> BungeeCordSender(commandSender) },
            { cloudSender -> (cloudSender as BungeeCordSender).getCommandSender() }
        )

        commandManager = BungeeCommandManager(
            this,
            executionCoordinator,
            senderMapper
        )

        val bungeeCordProxyHandler = BungeeCordProxyHandler(proxy)

        val playerCommand = PlayerCommand(commandManager, messageConfiguration, bungeeCordProxyHandler)
        playerCommand.createPlayerCommand()

        playerApi.registerPubSubListener(
            RabbitMqChannelNames.CONNECTION,
            CloudPlayerKickEvent::class.java, CloudPlayerKickListener(
                (this.proxy)
            )
        )
        playerApi.registerPubSubListener(
            RabbitMqChannelNames.CONNECTION,
            ConnectCloudPlayerEvent::class.java, CloudPlayerConnectListener(
                (this.proxy)
            )
        )

    }


}