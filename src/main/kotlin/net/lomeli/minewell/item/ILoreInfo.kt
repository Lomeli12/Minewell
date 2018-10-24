package net.lomeli.minewell.item

import net.minecraft.item.ItemStack

interface ILoreInfo {
    fun getLoreText(stack: ItemStack): String?
}