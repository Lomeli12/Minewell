package net.lomeli.minewell.lib

import net.lomeli.minewell.Minewell
import net.minecraft.client.resources.I18n
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.config.IConfigElement
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModConfig {
    private const val GENERAL_CATEGORY = "general"
    private const val CLIENT_CATEGORY = "client"
    private lateinit var config: Configuration

    var enableShaders = true

    fun setConfig(configDir: File) {
        config = Configuration(File(configDir, "${Minewell.MOD_NAME}.cfg"))
    }

    fun loadConfig() {

        // Client Configs
        config.addCustomCategoryComment(CLIENT_CATEGORY, I18n.format("config.minewell.client"))

        enableShaders = config.getBoolean("enableShaders", CLIENT_CATEGORY, true,
                I18n.format("config.minewell.client.enable_shaders"))

        if (config.hasChanged()) config.save()
    }

    fun getConfigElements(): List<IConfigElement> {
        val elements = ArrayList<IConfigElement>()
        elements.add(ConfigElement(config.getCategory(CLIENT_CATEGORY)))
        return elements
    }

    fun getTitle(): String = config.toString()

    @JvmStatic
    @SubscribeEvent
    fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID == Minewell.MOD_ID) loadConfig()
    }
}