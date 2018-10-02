package net.lomeli.minewell.block

import net.lomeli.minewell.Minewell
import net.minecraft.block.Block
import net.minecraft.block.material.Material

open class BlockBase(name: String, material: Material) : Block(material) {

    init {
        this.unlocalizedName = name
    }

    override fun setUnlocalizedName(name: String): Block = super.setUnlocalizedName("${Minewell.MOD_ID}.$name")
}