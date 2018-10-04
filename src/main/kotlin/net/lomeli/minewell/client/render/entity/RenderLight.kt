package net.lomeli.minewell.client.render.entity

import net.lomeli.minewell.entity.EntityLight
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.client.registry.IRenderFactory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


@SideOnly(Side.CLIENT)
class RenderLight(renderManager: RenderManager) : Render<EntityLight>(renderManager) {
    val itemRenderer = FMLClientHandler.instance().client.renderItem

    init {
        shadowSize = 0f
        shadowOpaque = 0f
    }

    override fun doRender(entity: EntityLight, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat(), z.toFloat())
        GlStateManager.enableRescaleNormal()
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate((if (this.renderManager.options.thirdPersonView == 2) -1 else 1).toFloat() * this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial()
            GlStateManager.enableOutlineMode(this.getTeamColor(entity))
        }

        this.itemRenderer.renderItem(ItemStack(Items.ENDER_EYE), ItemCameraTransforms.TransformType.GROUND)

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode()
            GlStateManager.disableColorMaterial()
        }

        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    override fun getEntityTexture(entity: EntityLight): ResourceLocation? = TextureMap.LOCATION_BLOCKS_TEXTURE
}


class RenderLightFactory : IRenderFactory<EntityLight> {
    override fun createRenderFor(manager: RenderManager?): Render<in EntityLight> = RenderLight(manager!!)
}