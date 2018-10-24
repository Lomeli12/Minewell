package net.lomeli.minewell.item.itemblock

import net.lomeli.minewell.block.ModBlocks
import net.lomeli.minewell.item.IItemRarity
import net.lomeli.minewell.item.ILoreInfo
import net.lomeli.minewell.lib.EnumItemRarity
import net.minecraft.item.ItemStack

class ItemEndWell : ItemBaseBlock(ModBlocks.END_WELL), IItemRarity, ILoreInfo {
    override fun getItemRarity(stack: ItemStack): EnumItemRarity = EnumItemRarity.EXOTIC
    override fun getLoreText(stack: ItemStack): String? = "tile.minewell.end_well.lore"
}