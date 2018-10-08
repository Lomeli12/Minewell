package net.lomeli.minewell.core.helpers

import com.google.common.base.Strings
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.util.MobUtil
import net.lomeli.minewell.lib.EFFECT_RANGE
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import java.util.*

class BossTracker(private val bossBaes: Array<out EntityLiving>) {
    private val mobList = ArrayList<EntityLiving>()
    private val maxNumberOfMobs = bossBaes.size
    private var hasSpawned = false
    private var wasTracking = false

    fun updateTracker(tier: WellTier, tile: TileEndWell) {
        if (tile.world != null && wasTracking) {
            wasTracking = false
            val entityList = tile.world.getEntitiesWithinAABB(EntityLiving::class.java,
                    AxisAlignedBB(tile.pos.x.toDouble(), tile.pos.y.toDouble() - 2, tile.pos.z.toDouble(),
                            tile.pos.x + 1.0, tile.pos.y - 1.0, tile.pos.z + 1.0).grow(EFFECT_RANGE))
            for (entity in entityList) {
                if (entity.isPotionActive(ModPotions.LIGHT))
                    mobList.add(entity)
            }
        }
        if (!tile.world.isRemote) {
            spawnBosses(tile)
            MobUtil.keepMobsHostile(mobList, tile.pos, tile.world)
        }
        if (mobList.size > 0) {
            val it = mobList.iterator()
            while (it.hasNext()) {
                val boss = it.next()
                if (boss.isDead) {
                    it.remove()
                    tier.addKills(1, tile)
                }
            }
        }
    }

    fun destroyTrackedMobs() {
        for (mob in mobList)
            mob.setDead()
    }

    private fun spawnBosses(tile: TileEndWell) {
        if (hasSpawned) return
        for (entityBase in bossBaes) {
            entityBase.setUniqueId(MathHelper.getRandomUUID(tile.world.rand))
            val id = MobUtil.getEntityName(entityBase)
            if (!Strings.isNullOrEmpty(id)) {
                val nbt = NBTTagCompound()
                entityBase.writeToNBT(nbt)
                nbt.setString("id", id!!)
                val entityAttempt = EntityList.createEntityFromNBT(nbt, tile.world)
                if (entityAttempt is EntityLiving)
                    spawnBoss(entityAttempt, tile)
            }
        }
        hasSpawned = true
    }

    private fun spawnBoss(boss: EntityLiving, tile: TileEndWell) {
        while (true) {
            val position = MobUtil.getSpawnPoint(tile.pos, tile.world.rand)
            val entitySpawned = MobUtil.spawnMonsterAtLocation(boss, position.x, position.y, position.z, mobList.size,
                    maxNumberOfMobs)
            if (entitySpawned != null) {
                entitySpawned.forceSpawn = true
                entitySpawned.enablePersistence()
                entitySpawned.addPotionEffect(PotionEffect(ModPotions.LIGHT, Int.MAX_VALUE))
                mobList.add(entitySpawned)
                break
            }
        }
    }

    fun readFromNBT(nbt: NBTTagCompound) {
        val compound = nbt.getCompoundTag("boss_spawner_data")
        wasTracking = compound.getBoolean("was_tracking")
        hasSpawned = compound.getBoolean("has_spawned")

    }

    fun writeToNBT(nbt: NBTTagCompound) {
        val compound = NBTTagCompound()
        compound.setBoolean("has_spawned", hasSpawned)
        compound.setBoolean("was_tracking", mobList.isNotEmpty())
        nbt.setTag("boss_spawner_data", compound)
    }
}