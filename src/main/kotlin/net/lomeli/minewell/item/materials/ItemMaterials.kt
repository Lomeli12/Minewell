package net.lomeli.minewell.item.materials

import net.lomeli.minewell.item.ItemBase
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack

class ItemMaterials(name: String): ItemBase(name) {
    private var rarity = EnumRarity.COMMON

    fun setRarity(rarity: EnumRarity): ItemMaterials {
        this.rarity = rarity
        return this
    }

    override fun getRarity(stack: ItemStack): EnumRarity = rarity
}