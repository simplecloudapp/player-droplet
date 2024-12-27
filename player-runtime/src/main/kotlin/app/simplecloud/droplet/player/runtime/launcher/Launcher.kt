package app.simplecloud.droplet.player.runtime.launcher

import com.github.ajalt.clikt.command.main
import org.apache.logging.log4j.LogManager

suspend fun main(args: Array<String>) {
//    val metricsCollector = try {
//        MetricsCollector.create("metrics-droplet")
//    } catch (e: Exception) {
//        null
//    }
    configureLog4j()
    PlayerDropletStartCommand().main(args)
}



fun configureLog4j() {
    val globalExceptionHandlerLogger = LogManager.getLogger("GlobalExceptionHandler")
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        globalExceptionHandlerLogger.error("Uncaught exception in thread ${thread.name}", throwable)
    }
}