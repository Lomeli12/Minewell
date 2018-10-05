package net.lomeli.minewell.client.handler

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.client.helpers.RenderHelper
import net.lomeli.minewell.core.util.RangeUtil
import net.lomeli.minewell.well.WellTier
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID, value = Side.CLIENT)
object BarOverlay {
    private val mc = FMLClientHandler.instance().client
    private val BAR_TEXTURES = ResourceLocation(Minewell.MOD_ID, "textures/gui/bars.png")

    @JvmStatic
    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Post) {
        val player = mc.player
        val profiler = mc.mcProfiler
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
            profiler.startSection("${Minewell.MOD_ID}_progress_bar")
            val pos = RangeUtil.isEntityNearWell(player, true)
            if (pos != null) {
                GlStateManager.pushMatrix()
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO)
                GlStateManager.enableBlend()

                renderStageInfo(player, pos)

                GlStateManager.disableBlend()
                GlStateManager.popMatrix()
            }
            profiler.endSection()
        }
    }

    private fun renderStageInfo(player: EntityPlayer, pos: BlockPos) {
        val sr = ScaledResolution(mc)
        val width = sr.scaledWidth
        val tile = player.world.getTileEntity(pos)
        if (tile is TileEndWell && tile.isWellActivated()) {
            val tier = tile.getTier()
            if (tier != null && tier.getCurrentStage().displayHUDBar()) {
                val xPos = width / 2 - 91
                val yPos = 12

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

                renderBar(xPos, yPos, tier)

                val text = I18n.format("event.minewell.charge")
                mc.fontRenderer.drawStringWithShadow(text,
                        (width / 2 - mc.fontRenderer.getStringWidth(text) / 2).toFloat(),
                        (yPos - 9).toFloat(), 16777215)
            }
        }
    }

    private fun renderBar(xPos: Int, yPos: Int, tier: WellTier) {
        mc.textureManager.bindTexture(BAR_TEXTURES)
        RenderHelper.drawTexturedModalRect(xPos, yPos, 0f, 0, 0, 182, 7)
        //TODO: Make config to display 1/4 markers
        RenderHelper.drawTexturedModalRect(xPos, yPos, 0f, 0, 14, 182, 7)

        val progress = tier.getCurrentKills().toFloat() / tier.getKillsNeeded().toFloat()
        val width = MathHelper.floor(182f * progress)
        //if (width > 0)
        RenderHelper.drawTexturedModalRect(xPos, yPos, 0f, 0, 7, width, 7)
    }
}