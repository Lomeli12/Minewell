package net.lomeli.minewell.client.render.tile

import net.lomeli.minewell.block.ModBlocks
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.client.effects.GLCallList
import net.lomeli.minewell.client.handler.ClientTickHandler
import net.lomeli.minewell.client.helpers.ShaderHelper
import net.lomeli.minewell.client.render.shader.ShaderEndPortal
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
class TileEndWellRenderer : TileEntitySpecialRenderer<TileEndWell>() {
    override fun render(tile: TileEndWell, x: Double, y: Double, z: Double, partialTicks: Float,
                        destroyStage: Int, alpha: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5f, y, z + 0.5f)
        val angle = (ClientTickHandler.ticksInGame % 360f)
        GlStateManager.rotate(angle, 0f, 1f, 0f)

        if (useShaders(tile))
            ShaderHelper.useShader(ShaderEndPortal.endPortalShader, ShaderEndPortal.enderCallback)

        renderCrystal(tile)

        if (useShaders(tile))
            ShaderHelper.releaseShader()
        GlStateManager.popMatrix()

        renderWellEffects(tile, x, y, z, partialTicks, destroyStage, alpha)
    }

    private fun renderCrystal(tile: TileEndWell) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(-0.5f, 0f, -0.5f)
        RenderHelper.disableStandardItemLighting()
        var texture = TextureMap.LOCATION_BLOCKS_TEXTURE
        if (useShaders(tile))
            texture = ShaderEndPortal.END_PORTAL_TEXTURE
        bindTexture(texture)

        if (FMLClientHandler.instance().client.gameSettings.ambientOcclusion != 0)
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        else
            GlStateManager.shadeModel(GL11.GL_FLAT)

        GlStateManager.translate(-tile.pos.x.toFloat(), -tile.pos.y.toFloat(), -tile.pos.z.toFloat())

        val tess = Tessellator.getInstance()
        val buffer = tess.buffer
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)

        val state = ModBlocks.END_WELL.defaultState
        val dispatcher = FMLClientHandler.instance().client.blockRendererDispatcher
        val model = dispatcher.getModelForState(state)
        dispatcher.blockModelRenderer.renderModel(world, model, state, tile.pos, buffer, true)
        tess.draw()

        RenderHelper.enableStandardItemLighting()
        GlStateManager.popMatrix()
    }

    private fun renderWellEffects(tile: TileEndWell, x: Double, y: Double, z: Double, partialTicks: Float,
                                  destroyStage: Int, alpha: Float) {
        if (ClientTickHandler.ticksInGame % 10 == 0 && tile.isWellActivated())
            renderActivatedParticles(tile.pos)
        if (tile.radius > 0f)
            drawSphere(x + 0.5f, y - 1.5f, z + 0.5f, tile.radius)
    }

    private fun renderActivatedParticles(pos: BlockPos) {
        for (i in 0..4) {
            val d0 = pos.x + world.rand.nextDouble()
            val d1 = pos.y + world.rand.nextDouble()
            val d2 = pos.z + world.rand.nextDouble()
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0, d1, d2, 0.0, 0.0, 0.0)
        }
    }

    private fun drawSphere(x: Double, y: Double, z: Double, radius: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.translate(x, y, z)
        GlStateManager.scale(radius, radius, radius)

        GlStateManager.color(1f, 1f, 1f, 0.75f)
        GL11.glCallList(GLCallList.sphereOutID)
        GlStateManager.color(1f, 1f, 1f, 0.4f)
        GL11.glCallList(GLCallList.sphereInID)

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    private fun useShaders(tile: TileEndWell): Boolean = ShaderHelper.useShaders() &&
            ShaderEndPortal.endPortalShader != 0 && tile.isWellActivated()
}