package net.lomeli.minewell.core

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.item.ModItems
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object CreativeMineWell: CreativeTabs(Minewell.MOD_ID) {
    override fun getTabIconItem(): ItemStack = ItemStack(ModItems.END_WELL_ITEM)
}