package net.lomeli.minewell.client.render.tile

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.lomeli.minewell.client.helpers.ShaderHelper
import net.lomeli.minewell.client.render.shader.ShaderEndPortal
import net.minecraft.client.model.ModelChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class TileEndRewardChestRenderer: TileEntitySpecialRenderer<TileEndRewardChest>() {
    private val CHEST_TEXTURE = ResourceLocation(Minewell.MOD_ID,"textures/models/end_reward_chest.png")
    private val modelChest = ModelChest()

    override fun render(te: TileEndRewardChest, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        var i = 0

        if (te.hasWorld()) {
            i = te.blockMetadata
        }

        if (destroyStage >= 0) {
            bindTexture(TileEntitySpecialRenderer.DESTROY_STAGES[destroyStage])
            GlStateManager.matrixMode(5890)
            GlStateManager.pushMatrix()
            GlStateManager.scale(4.0f, 4.0f, 1.0f)
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f)
            GlStateManager.matrixMode(5888)
        } else {
            bindTexture(CHEST_TEXTURE)
        }

        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha)
        GlStateManager.translate(x.toFloat(), y.toFloat() + 1.0f, z.toFloat() + 1.0f)
        GlStateManager.scale(1.0f, -1.0f, -1.0f)
        GlStateManager.translate(0.5f, 0.5f, 0.5f)
        var j = 0

        if (i == 2) {
            j = 180
        }

        if (i == 3) {
            j = 0
        }

        if (i == 4) {
            j = 90
        }

        if (i == 5) {
            j = -90
        }

        GlStateManager.rotate(j.toFloat(), 0.0f, 1.0f, 0.0f)
        GlStateManager.translate(-0.5f, -0.5f, -0.5f)
        var f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks
        f = 1.0f - f
        f = 1.0f - f * f * f
        modelChest.chestLid.rotateAngleX = -(f * (Math.PI.toFloat() / 2f))

        if (ShaderHelper.useShaders()) {
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.98f, 0.98f, 0.98f)
            GlStateManager.translate(0.01f, 0.01f, 0.01f)
            bindTexture(ShaderEndPortal.END_PORTAL_TEXTURE)
            ShaderHelper.useShader(ShaderEndPortal.endPortalShader, ShaderEndPortal.enderCallback)
            renderMinusKnob()
            ShaderHelper.releaseShader()
            GlStateManager.popMatrix()
        }

        bindTexture(CHEST_TEXTURE)

        modelChest.renderAll()

        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890)
            GlStateManager.popMatrix()
            GlStateManager.matrixMode(5888)
        }
    }

    private fun renderMinusKnob() {
        modelChest.chestLid.render(0.0625f)
        modelChest.chestBelow.render(0.0625f)
    }
}