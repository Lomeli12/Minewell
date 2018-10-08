package net.lomeli.minewell.item

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.core.CreativeMineWell
import net.minecraft.item.Item

open class ItemBase(name: String): Item() {
    init {
        unlocalizedName = "${Minewell.MOD_ID}.$name"
        setRegistryName(Minewell.MOD_ID, name)
        creativeTab = CreativeMineWell
    }
}