package net.lomeli.minewell.client.handler

import net.lomeli.minewell.Minewell
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID, value = [Side.CLIENT])
object ClientTickHandler {
    var ticksInGame = 0
    var renderTick = 0f

    @JvmStatic
    @SubscribeEvent
    fun clickEndTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return
        val gui = FMLClientHandler.instance().client.currentScreen
        if (gui == null || !gui.doesGuiPauseGame())
            ticksInGame++

    }

    @JvmStatic
    @SubscribeEvent
    fun renderTick(event: TickEvent.RenderTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return
        renderTick = event.renderTickTime
    }
}