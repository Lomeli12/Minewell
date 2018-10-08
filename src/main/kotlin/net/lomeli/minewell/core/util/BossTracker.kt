package net.lomeli.minewell.core.util

import com.google.common.base.Strings
import net.lomeli.minewell.block.tile.EFFECT_RANGE
import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.MAX_RADIUS
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
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
        if (!tile.world.isRemote)
            spawnBosses(tile)
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
            val id = getEntityName(entityBase)
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

    private fun getEntityName(entity: EntityLiving): String? = EntityList.getKey(entity)?.toString()

    private fun spawnBoss(boss: EntityLiving, tile: TileEndWell) {
        while (true) {
            val position = getSpawnPoint(tile)
            val entitySpawned = spawnMonsterAtLocation(boss, position.x, position.y, position.z)
            if (entitySpawned != null) {
                entitySpawned.forceSpawn = true
                entitySpawned.enablePersistence()
                entitySpawned.addPotionEffect(PotionEffect(ModPotions.LIGHT, Int.MAX_VALUE))
                mobList.add(entitySpawned)
                break
            }
        }
    }

    private fun getSpawnPoint(tile: TileEndWell): Vec3d {
        var spawnPoint: Vec3d? = null
        while (spawnPoint == null) {
            val d0 = tile.pos.x.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
            val d1 = (tile.pos.y + tile.world.rand.nextInt(3) - 1).toDouble()
            val d2 = tile.pos.z.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
            val distance = RangeUtil.get2DDistance(d0, d2, tile.pos.x.toDouble(), tile.pos.z.toDouble())
            if (distance > MAX_RADIUS)
                spawnPoint = Vec3d(d0, d1, d2)
        }
        return spawnPoint
    }

    private fun spawnMonsterAtLocation(entity: EntityLiving, x: Double, y: Double, z: Double): EntityLiving? {
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch)
        if (entity.isNotColliding && !entity.isDead && mobList.size < maxNumberOfMobs) {
            if (entity.world.spawnEntity(entity))
                return entity
        }
        return null
    }

    private fun getEntityByUUID(world: World, id: UUID): EntityLiving? {
        for (entity in world.loadedEntityList) {
            if (entity is EntityLiving && entity.uniqueID.equals(id))
                return entity
        }
        return null
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