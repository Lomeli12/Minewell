package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.MobEffects
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PotionHarmony : PotionBase(false, 0x46c8d7) {
    init {
        setIconIndex(1, 0)
        setPotionName("potion.${Minewell.MOD_ID}.harmony")
        setRegName("harmony")
    }

    override fun performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int) {
        if (entityLivingBaseIn.isPotionActive(MobEffects.WITHER))
            entityLivingBaseIn.removePotionEffect(MobEffects.WITHER)
    }

    @SideOnly(Side.CLIENT)
    override fun getStatusIconIndex(): Int {
        FMLClientHandler.instance().client.textureManager.bindTexture(ModPotions.POTION_TEXTURE)
        return super.getStatusIconIndex()
    }

    override fun isReady(duration: Int, amplifier: Int): Boolean = true
}