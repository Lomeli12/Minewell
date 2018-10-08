package net.lomeli.minewell.block

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.core.CreativeMineWell
import net.minecraft.block.Block
import net.minecraft.block.material.Material

open class BlockBase(name: String, material: Material) : Block(material) {

    init {
        this.unlocalizedName = name
        this.setRegistryName(Minewell.MOD_ID, name)
        this.setCreativeTab(CreativeMineWell)
    }

    override fun setUnlocalizedName(name: String): Block = super.setUnlocalizedName("${Minewell.MOD_ID}.$name")
}