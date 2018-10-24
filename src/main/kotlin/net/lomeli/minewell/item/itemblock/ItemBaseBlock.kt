package net.lomeli.minewell.item.itemblock

import com.google.common.base.Strings
import net.lomeli.minewell.client.helpers.KeyHelper
import net.lomeli.minewell.item.IItemRarity
import net.lomeli.minewell.item.ILoreInfo
import net.minecraft.block.Block
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemBaseBlock(block: Block) : ItemBlock(block) {
    private val INFO_TEXT = "item.minewell.info"

    init {
        registryName = block.registryName
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        var hasShiftInfo = false
        if (stack.item is IItemRarity) {
            val rarity = (stack.item as IItemRarity).getItemRarity(stack)
            tooltip[0] = "${rarity.getTextColor()}${tooltip[0]}${TextFormatting.RESET}"
            if (KeyHelper.isShiftDown())
                tooltip.add("${rarity.getTextColor()}${rarity.getTooltipText()}${TextFormatting.RESET}")
            else {
                hasShiftInfo = true
                tooltip.add(I18n.format(INFO_TEXT))
            }
        }

        if (stack.item is ILoreInfo) {
            val infoText = (stack.item as ILoreInfo).getLoreText(stack)
            if (!Strings.isNullOrEmpty(infoText)) {
                if (KeyHelper.isShiftDown())
                    tooltip.add("${TextFormatting.GRAY}${TextFormatting.ITALIC}${I18n.format(infoText!!)}${TextFormatting.RESET}")
                else if (!hasShiftInfo)
                    tooltip.add(I18n.format(INFO_TEXT))
            }
        }
        super.addInformation(stack, world, tooltip, flag)
    }
}