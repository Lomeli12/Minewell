package net.lomeli.minewell.well.tiers

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.Stage
import net.lomeli.minewell.well.WellReward
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.monster.EntityZombieVillager
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

class TierOne : WellTier() {

    override fun getTierMobs(tile: TileEndWell): Array<out EntityLiving> {
        val zombie = EntityZombie(tile.world)
        zombie.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.LEATHER_HELMET))
        val zombieSword = EntityZombie(tile.world)
        zombieSword.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        zombieSword.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.LEATHER_HELMET))
        zombieSword.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        val zombieVillager = EntityZombieVillager(tile.world)
        zombieVillager.profession = tile.world.rand.nextInt(6)
        zombieVillager.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        zombieVillager.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.LEATHER_HELMET))
        val zombieVillagerSword = EntityZombieVillager(tile.world)
        zombieVillagerSword.profession = tile.world.rand.nextInt(6)
        zombieVillagerSword.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        zombieVillagerSword.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.LEATHER_HELMET))
        zombieVillagerSword.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))

        val skeleton = EntitySkeleton(tile.world)
        skeleton.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        skeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.LEATHER_HELMET))
        skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack(Items.BOW))

        return arrayOf(zombie, zombieSword, skeleton, zombieVillager, zombieVillagerSword)
    }

    override fun getTierBosses(tile: TileEndWell): Array<out EntityLiving> {
        val boss = EntityZombie(tile.world)
        boss.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
        boss.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack(Items.SHIELD))
        boss.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack(Items.IRON_HELMET))
        boss.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack(Items.IRON_CHESTPLATE))
        boss.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack(Items.IRON_LEGGINGS))
        boss.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack(Items.IRON_BOOTS))
        boss.addPotionEffect(PotionEffect(MobEffects.HEALTH_BOOST, Int.MAX_VALUE, 2))
        boss.addPotionEffect(PotionEffect(MobEffects.STRENGTH, Int.MAX_VALUE))
        boss.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, Int.MAX_VALUE))
        return arrayOf(boss)
    }

    override fun possibleRewards(): Array<WellReward> {
        val rewards = ArrayList<WellReward>()
        rewards.add(WellReward(ItemStack(Items.GOLDEN_APPLE, 1, 0), 1f))
        rewards.add(WellReward(ItemStack(Items.DIAMOND), 0.5f))
        rewards.add(WellReward(ItemStack(Items.DIAMOND), 0.5f))
        rewards.add(WellReward(ItemStack(Items.DIAMOND), 0.5f))
        rewards.add(WellReward(ItemStack(Items.NETHER_STAR), 0.25f))
        return rewards.toTypedArray()
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