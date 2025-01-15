package app.simplecloud.droplet.player.plugin.bungeecord.command

import app.simplecloud.droplet.player.plugin.shared.command.CloudSender
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.CommandSender

/**
 * @author Fynn Bauer in 2024
 */
class BungeeCordSender(private val commandSender: CommandSender): CloudSender {

    fun getCommandSender(): CommandSender {
        return commandSender
    }

    override fun sendMessage(message: Component) {
        commandSender.sendMessage(MiniMessage.miniMessage().serialize(message))
    }
}