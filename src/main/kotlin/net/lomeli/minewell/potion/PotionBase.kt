package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell
import net.minecraft.potion.Potion
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class PotionBase(isBadEffect: Boolean, color: Int): Potion(isBadEffect, color) {
    @SideOnly(Side.CLIENT) override fun getStatusIconIndex(): Int {
        FMLClientHandler.instance().client.textureManager.bindTexture(ModPotions.POTION_TEXTURE)
        return super.getStatusIconIndex()
    }

    fun setRegName(name: String) {
        this.setRegistryName(Minewell.MOD_ID, name)
    }
}