package net.lomeli.minewell.client.handler

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.minecraft.client.resources.I18n
import net.minecraft.util.math.BlockPos
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID, value = Side.CLIENT)
object TextHandler {
    val mc = FMLClientHandler.instance().client

    @JvmStatic
    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Text) {
        val player = mc.player ?: return
        for (x in -5..5)
            for (y in -5..5)
                for (z in -5..5) {
                    val newX = player.posX + x
                    val newY = player.posY + y
                    val newZ = player.posZ + z
                    val pos = BlockPos(newX, newY, newZ)
                    val distance = player.getDistance(newX, newY, newZ)
                    if (distance <= 6f) {
                        val tile = player.world.getTileEntity(pos)
                        if (tile is TileEndWell && tile.isWellActivated()) {
                            val tier = tile.getTier()!!
                            event.left.add(I18n.format(tier.getTierName()))

                            val stage = tier.getCurrentStage()
                            event.left.add(I18n.format(stage.getMessage()))

                            event.left.add(I18n.format("event.minewell.timer", createTimer(tile.getTimer())))
                        }
                    }
                }
    }

    private fun createTimer(time: Int): String {
        val ticksInSec = Math.ceil(time / 20.0)
        var min = Math.floor(ticksInSec / 60.0).toInt()
        var secs = Math.floor(ticksInSec % 60.0).toInt()
        if (min < 0) min = 0
        if (secs < 0) secs = 0
        val minSt = if (min > 9) min.toString() else "0$min"
        val secSt = if (secs > 9) secs.toString() else "0$secs"
        return "$minSt:$secSt"
    }
}