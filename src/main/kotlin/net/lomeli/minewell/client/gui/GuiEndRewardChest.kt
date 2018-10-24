package net.lomeli.minewell.client.gui

import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.lomeli.minewell.core.inventory.ContainerEndRewardChest
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.ItemStackHandler

class GuiEndRewardChest(private val playerInventory: InventoryPlayer, private val chestInventory: ItemStackHandler, tile: TileEndRewardChest):
        GuiContainer(ContainerEndRewardChest(playerInventory, chestInventory, tile)) {

    private val GUI_TEXTURES = ResourceLocation("textures/gui/container/dispenser.png")

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val s = I18n.format("tile.minewell.end_reward_chest.name")
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752)
        fontRenderer.drawString(playerInventory.displayName.unformattedText, 8, ySize - 96 + 2, 4210752)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(GUI_TEXTURES)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize)
    }
}