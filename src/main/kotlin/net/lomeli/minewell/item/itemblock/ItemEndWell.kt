package net.lomeli.minewell.item.itemblock

import net.lomeli.minewell.block.ModBlocks
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack

class ItemEndWell: ItemBlock(ModBlocks.END_WELL) {

    override fun getRarity(stack: ItemStack): EnumRarity = EnumRarity.EPIC
}