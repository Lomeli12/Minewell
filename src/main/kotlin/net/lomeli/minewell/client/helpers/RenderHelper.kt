package net.lomeli.minewell.client.helpers

import net.lomeli.minewell.client.handler.BarOverlay.mc
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

object RenderHelper {
    fun drawTexturedModalRect(x: Int, y: Int, zLevel: Float, textureX: Int, textureY: Int, width: Int, height: Int) {
        drawTexturedModalRect(x, y, zLevel, textureX, textureY, width, height, 0.00390625f, 0.00390625f)
    }

    fun drawTexturedModalRect(x: Int, y: Int, zLevel: Float, textureX: Int, textureY: Int, width: Int, height: Int, f: Float, f1: Float) {
        val tess = Tessellator.getInstance()
        val buffer = tess.buffer
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        buffer.pos((x + 0).toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(((textureX + 0).toFloat() * 0.00390625f).toDouble(), ((textureY + height).toFloat() * 0.00390625f).toDouble()).endVertex()
        buffer.pos((x + width).toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(((textureX + width).toFloat() * 0.00390625f).toDouble(), ((textureY + height).toFloat() * 0.00390625f).toDouble()).endVertex()
        buffer.pos((x + width).toDouble(), (y + 0).toDouble(), zLevel.toDouble()).tex(((textureX + width).toFloat() * 0.00390625f).toDouble(), ((textureY + 0).toFloat() * 0.00390625f).toDouble()).endVertex()
        buffer.pos((x + 0).toDouble(), (y + 0).toDouble(), zLevel.toDouble()).tex(((textureX + 0).toFloat() * 0.00390625f).toDouble(), ((textureY + 0).toFloat() * 0.00390625f).toDouble()).endVertex()
        tess.draw()
    }

    fun startGLScissor(x: Int, y: Int, width: Int, height: Int) {
        val res = ScaledResolution(mc)
        val scaleW = (mc.displayWidth / res.scaledWidth_double)
        val scaleH = (mc.displayHeight / res.scaledHeight_double)

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(Math.floor(x.toDouble() * scaleW).toInt(),
                Math.floor(mc.displayHeight.toDouble() - (y.toDouble() + height) * scaleH).toInt(),
                Math.floor((x.toDouble() + width) * scaleW).toInt() - Math.floor(x.toDouble() * scaleW).toInt(),
                Math.floor(mc.displayHeight.toDouble() - y.toDouble() * scaleH).toInt() -
                        Math.floor(mc.displayHeight.toDouble() - (y.toDouble() + height) * scaleH).toInt())
        //starts from lower left corner (minecraft starts from upper left)
    }

    fun stopGLScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

}