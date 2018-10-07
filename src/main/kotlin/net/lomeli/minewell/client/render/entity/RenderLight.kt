package net.lomeli.minewell.client.render.entity

import net.lomeli.minewell.client.handler.ClientTickHandler
import net.lomeli.minewell.entity.EntityLight
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.IRenderFactory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*


@SideOnly(Side.CLIENT)
class RenderLight(renderManager: RenderManager) : Render<EntityLight>(renderManager) {
    init {
        shadowSize = 0f
        shadowOpaque = 0f
    }

    override fun doRender(entity: EntityLight, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.rotate(180f, 0f, 0f, 1f)

        GlStateManager.pushMatrix()
        GlStateManager.translate(-x.toFloat(), -y.toFloat(), z.toFloat())
        GlStateManager.scale(0.025f, 0.025f, 0.025f)
        val rotation = (720.0 * (ClientTickHandler.ticksInGame.toLong() and 0x3FFFL) / 0x3FFFL).toFloat()
        GlStateManager.rotate(rotation, 0.0F, 0.5F, 0.0F)
        GlStateManager.rotate(rotation / 2, 0.0F, 0.0F, 0.5F)

        renderLight(180f, 0f, 0f, 0f, 150, 150, 150)

        GlStateManager.popMatrix()

        GlStateManager.popMatrix()
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    // Copied from LayerEnderDragonDeath. I have no real idea what it does other than it's magic
    private fun renderLight(density: Float, x: Float, y: Float, z: Float, r: Int, g: Int, b: Int) {
        val tess = Tessellator.getInstance()
        val buffer = tess.buffer
        RenderHelper.disableStandardItemLighting()
        val f = density / 200.0f
        var f1 = 0.0f

        if (f > 0.8f) f1 = (f - 0.8f) / 0.2f

        val random = Random(432L)
        GlStateManager.disableTexture2D()
        GlStateManager.shadeModel(7425)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        GlStateManager.disableAlpha()
        GlStateManager.enableCull()
        GlStateManager.depthMask(false)
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)

        var i = 0
        while (i.toFloat() < (f + f * f) / 2.0f * 60.0f) {
            GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f)
            GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(random.nextFloat() * 360.0f + f * 90.0f, 0.0f, 0.0f, 1.0f)

            val f2 = random.nextFloat() * 20.0f + 5.0f + f1 * 10.0f
            val f3 = random.nextFloat() * 2.0f + 1.0f + f1 * 2.0f
            buffer.begin(6, DefaultVertexFormats.POSITION_COLOR)
            buffer.pos(0.0, 0.0, 0.0).color(255, 255, 255, (255.0f * (1.0f - f1)).toInt()).endVertex()
            buffer.pos(-0.866 * f3.toDouble(), f2.toDouble(), (-0.5f * f3).toDouble()).color(r, g, b, 0).endVertex()
            buffer.pos(0.866 * f3.toDouble(), f2.toDouble(), (-0.5f * f3).toDouble()).color(r, g, b, 0).endVertex()
            buffer.pos(0.0, f2.toDouble(), (1.0f * f3).toDouble()).color(r, g, b, 0).endVertex()
            buffer.pos(-0.866 * f3.toDouble(), f2.toDouble(), (-0.5f * f3).toDouble()).color(r, g, b, 0).endVertex()
            tess.draw()
            ++i
        }

        GlStateManager.popMatrix()
        GlStateManager.depthMask(true)
        GlStateManager.disableCull()
        GlStateManager.disableBlend()
        GlStateManager.shadeModel(7424)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableTexture2D()
        GlStateManager.enableAlpha()
        RenderHelper.enableStandardItemLighting()
    }

    override fun getEntityTexture(entity: EntityLight): ResourceLocation? = null
}


class RenderLightFactory : IRenderFactory<EntityLight> {
    override fun createRenderFor(manager: RenderManager?): Render<in EntityLight> = RenderLight(manager!!)
}