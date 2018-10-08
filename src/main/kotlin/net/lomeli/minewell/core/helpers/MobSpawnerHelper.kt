package net.lomeli.minewell.core.helpers

import com.google.common.base.Strings
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.util.MobUtil
import net.lomeli.minewell.lib.EFFECT_RANGE
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.WellEnemy
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import java.util.*
import kotlin.collections.ArrayList

class MobSpawnerHelper(private val entityList: Array<WellEnemy>,
                       private val maxNumberOfMobs: Int,
                       private val spawnSise: Int) {
    private val rand = Random()
    private val mobList = ArrayList<EntityLiving>()
    private var wasTracking = false

    fun spawnMonsters(tile: TileEndWell) {
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
        if (tile.getTimer() % 20 == 0) {
            // Clear dead mobs
            if (mobList.isNotEmpty()) {
                val it = mobList.iterator()
                while (it.hasNext()) {
                    val mob = it.next()
                    if (mob.isDead)
                        it.remove()
                }
            }

            // Spawn new mobs in large groups
            if (mobList.size < maxNumberOfMobs && !tile.world.isRemote) {
                for (i in 1..spawnSise) {
                    spawnNewMob(tile)
                    if (mobList.size == maxNumberOfMobs)
                        break
                }
            }
        }
    }

    fun destroyTrackedMobs() {
        for (mob in mobList)
            mob.setDead()
    }

    private fun spawnNewMob(tile: TileEndWell) {
        for (attempt in 0..4) {
            val position = MobUtil.getSpawnPoint(tile.pos, rand)
            val mobBase = getRandomEnemy()
            mobBase.setUniqueId(MathHelper.getRandomUUID(rand))
            val id = MobUtil.getEntityName(mobBase)
            if (!Strings.isNullOrEmpty(id)) {
                val nbt = NBTTagCompound()
                mobBase.writeToNBT(nbt)
                nbt.setString("id", id!!)
                val entityAttempt = EntityList.createEntityFromNBT(nbt, tile.world)
                if (entityAttempt is EntityLiving) {
                    val entitySpawned = MobUtil.spawnMonsterAtLocation(entityAttempt, position.x, position.y,
                            position.z, mobList.size, maxNumberOfMobs)
                    if (entitySpawned != null) {
                        entitySpawned.forceSpawn = true
                        entitySpawned.enablePersistence()

                        // Apply light and other potion effects
                        entitySpawned.addPotionEffect(PotionEffect(ModPotions.LIGHT, Int.MAX_VALUE))

                        mobList.add(entitySpawned)
                        break
                    }
                }
            }
        }
    }

    private fun getRandomEnemy(): EntityLiving {
        var entity: EntityLiving? = null
        while (entity == null) {
            for (entry in entityList) {
                if (entry.getChance() >= rand.nextFloat())
                    entity = entry.getEntityBase()
            }
        }
        return entity
    }

    fun readFromNBT(nbt: NBTTagCompound) {
        val compound = nbt.getCompoundTag("mob_spawner_data")
        wasTracking = compound.getBoolean("was_tracking")
    }

    fun writeToNBT(nbt: NBTTagCompound) {
        val compound = NBTTagCompound()
        compound.setBoolean("was_tracking", mobList.isNotEmpty())
        nbt.setTag("mob_spawner_data", compound)
    }
}