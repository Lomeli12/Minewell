package net.lomeli.minewell.well.tiers

import net.lomeli.minewell.well.Stage
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.monster.EntityZombieVillager
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World

class TierOne : WellTier() {

    override fun getTierMobs(): Array<Class<out EntityLiving>> =
            arrayOf(EntityZombie::class.java, EntitySkeleton::class.java, EntityZombieVillager::class.java)

    override fun getTierPotionEffects(): Array<PotionEffect> = arrayOf(PotionEffect(MobEffects.FIRE_RESISTANCE, Int.MAX_VALUE),
            PotionEffect(MobEffects.RESISTANCE, Int.MAX_VALUE, 1))

    override fun getTierBosses(world: World): Array<EntityLiving> {
        //TODO: Create actual boss monster
        val boss = EntityZombie(world)
        boss.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, Int.MAX_VALUE, 2))
        boss.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE, 2))
        boss.addPotionEffect(PotionEffect(MobEffects.HEALTH_BOOST, Int.MAX_VALUE, 2))
        return arrayOf(boss)
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