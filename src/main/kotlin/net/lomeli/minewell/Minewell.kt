package net.lomeli.minewell

import net.lomeli.minewell.core.Proxy
import net.lomeli.minewell.core.util.Logger
import net.lomeli.minewell.lib.ModConfig
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper

@Mod(modid = Minewell.MOD_ID, name = Minewell.MOD_NAME, version = Minewell.VERSION, modLanguage = Minewell.LANGUAGE,
        modLanguageAdapter = Minewell.KOTLIN_ADAPTER)
object Minewell {
    const val MOD_ID = "minewell"
    const val MOD_NAME = "Minewell"
    const val KOTLIN_ADAPTER = "net.lomeli.minewell.KotlinAdapter"
    const val LANGUAGE = "Kotlin"
    const val VERSION = "@VERSION@"

    @Mod.Instance(MOD_ID)
    lateinit var instance: Minewell

    @SidedProxy(clientSide = "net.lomeli.minewell.client.ClientProxy", serverSide = "net.lomeli.minewell.core.Proxy")
    lateinit var proxy: Proxy

    val debug = Launch.blackboard.get(key = "fml.deobfuscatedEnvironment") as Boolean

    lateinit var log: Logger
    lateinit var packetHandler: SimpleNetworkWrapper

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log = Logger()
        if (debug) log.logInfo("Dev environment, enabled logging!")

        ModConfig.setConfig(event.modConfigurationDirectory)
        ModConfig.loadConfig()

        proxy.preInit()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit()
    }
}