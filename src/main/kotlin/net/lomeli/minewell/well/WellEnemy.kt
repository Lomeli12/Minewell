package net.lomeli.minewell.well

import net.minecraft.entity.EntityLiving

class WellEnemy(private val entity: EntityLiving, private val chance: Float) {
    fun getEntityBase(): EntityLiving = entity
    fun getChance(): Float = chance
}