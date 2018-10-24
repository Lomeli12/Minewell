package net.lomeli.minewell.item.materials

import net.lomeli.minewell.item.IItemRarity
import net.lomeli.minewell.item.ItemBase
import net.lomeli.minewell.lib.EnumItemRarity
import net.minecraft.item.ItemStack

class ItemMaterials(name: String): ItemBase(name), IItemRarity {
    override fun getItemRarity(stack: ItemStack): EnumItemRarity = EnumItemRarity.UNCOMMON
}