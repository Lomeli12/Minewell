package net.lomeli.minewell.client.handler

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.util.RangeUtil
import net.minecraft.client.resources.I18n
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID, value = Side.CLIENT)
object TextHandler {
    private val mc = FMLClientHandler.instance().client

    @JvmStatic @SubscribeEvent fun renderOverlay(event: RenderGameOverlayEvent.Text) {
        val player = mc.player ?: return
        val pos = RangeUtil.isEntityNearWell(player, true)
        if (pos != null) {
            val tile = player.world.getTileEntity(pos) as TileEndWell
            if (tile.getTier() != null) {
                val tier = tile.getTier()!!
                event.left.add(I18n.format(tier.getUnlocalizedName()))

                val stage = tier.getCurrentStage()
                event.left.add(I18n.format(stage.getMessage()))

                event.left.add(I18n.format("event.minewell.timer", createTimer(tile.getTimer())))
                return
            }
        }
    }

    private fun createTimer(time: Int): String {
        val timeInSec = Math.floor(time / 20.0)
        var min = Math.floor(timeInSec / 60.0).toInt()
        var secs = Math.floor(timeInSec % 60.0).toInt()
        if (min < 0) min = 0
        if (secs < 0) secs = 0
        val minSt = if (min > 9) min.toString() else "0$min"
        val secSt = if (secs > 9) secs.toString() else "0$secs"
        return "$minSt:$secSt"
    }
}