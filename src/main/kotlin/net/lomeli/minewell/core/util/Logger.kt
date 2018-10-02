package net.lomeli.minewell.core.util

import net.lomeli.minewell.Minewell
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

class Logger {
    private val logger = LogManager.getLogger(Minewell.MOD_NAME)

    fun log(logLevel: Level, message: Any) = logger.log(logLevel, "[${Minewell.MOD_NAME}]: $message")

    fun logWarning(message: Any) = log(Level.WARN, message)

    fun logInfo(message: Any) = log(Level.INFO, message)

    fun logError(message: Any) = log(Level.ERROR, message)
}