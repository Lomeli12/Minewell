package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.DamageSource

class PotionVoidTouch : PotionBase(true, 0x23463c) {

    val VOID_DAMAGE = DamageSource("${Minewell.MOD_ID}.void_touch").setDamageBypassesArmor()

    init {
        setIconIndex(2, 0)
        setPotionName("potion.${Minewell.MOD_ID}.void_touch")
        setRegName("void_touch")
    }

    override fun performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int) {
        if (entityLivingBaseIn is EntityPlayer)
            entityLivingBaseIn.attackEntityFrom(VOID_DAMAGE, 1f)
    }

    override fun isReady(duration: Int, amplifier: Int): Boolean = true
}