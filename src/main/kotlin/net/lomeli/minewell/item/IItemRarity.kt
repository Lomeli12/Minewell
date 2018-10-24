package net.lomeli.minewell.item

import net.lomeli.minewell.lib.EnumItemRarity
import net.minecraft.item.ItemStack

interface IItemRarity {
    fun getItemRarity(stack: ItemStack): EnumItemRarity
}