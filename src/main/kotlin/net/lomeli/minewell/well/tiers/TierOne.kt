package net.lomeli.minewell.well.tiers

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.Stage
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.monster.EntityZombieVillager
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect

class TierOne(tile: TileEndWell) : WellTier() {

    override fun getTierMobs(): Array<Class<out EntityLiving>> =
            arrayOf(EntityZombie::class.java, EntitySkeleton::class.java, EntityZombieVillager::class.java)

    override fun getTierPotionEffects(): Array<PotionEffect> = arrayOf(PotionEffect(MobEffects.FIRE_RESISTANCE, Int.MAX_VALUE),
            PotionEffect(MobEffects.RESISTANCE, Int.MAX_VALUE, 1))

    override fun getTierBoss(): Array<EntityLiving> {

    }

    override fun getKillsNeeded(): Int {
        return when {
            getCurrentStage() == Stage.STAGE_ONE -> 50
            getCurrentStage() == Stage.STAGE_TWO -> 75
            getCurrentStage() == Stage.STAGE_THREE -> 100
            getCurrentStage() == Stage.BOSS -> 1
            else -> 0
        }
    }

    override fun getUnlocalizedName(): String = "event.minewell.tier.one"

    override fun getRegistryName(): String = "tier.minewell.tier_one"
}