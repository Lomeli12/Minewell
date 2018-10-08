package net.lomeli.minewell.item.itemblock

import net.lomeli.minewell.block.ModBlocks
import net.lomeli.minewell.lib.EXOTIC
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack

class ItemEndWell : ItemBlock(ModBlocks.END_WELL) {
    init {
        registryName = block.registryName
    }

    override fun getRarity(stack: ItemStack): EnumRarity = EXOTIC ?: super.getRarity(stack)
}