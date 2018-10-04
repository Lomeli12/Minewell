package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell
import net.minecraft.entity.EntityLivingBase

class PotionSkyTouch : PotionBase(false, 0x630063) {

    init {
        setIconIndex(0, 0)
        setPotionName("potion.${Minewell.MOD_ID}.sky_touch")
        setRegName("sky_touch")
    }

    override fun performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int) {
        if (entityLivingBaseIn.isPotionActive(ModPotions.VOID_TOUCH))
            entityLivingBaseIn.removePotionEffect(ModPotions.VOID_TOUCH)
    }

    override fun isReady(duration: Int, amplifier: Int): Boolean = true
}