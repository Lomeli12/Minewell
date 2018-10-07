package net.lomeli.minewell.core.helpers

import com.google.common.base.Strings
import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.util.RangeUtil
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.WellEnemy
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.collections.ArrayList

class MobSpawnerHelper(private val entityList: Array<WellEnemy>,
                       private val maxNumberOfMobs: Int,
                       private val spawnSise: Int) {
    private val rand = Random()
    private val mobList = ArrayList<EntityLiving>()
    private var updateIDs = ArrayList<UUID>()

    fun spawnMonsters(tile: TileEndWell) {
        if (tile.world != null && updateIDs.isNotEmpty()) {
            val it = updateIDs.iterator()
            while (it.hasNext()) {
                val id = it.next()
                val entity = getEntityByUUID(tile.world, id)
                if (entity != null && !entity.isDead)
                    mobList.add(entity)
                it.remove()
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
            val position = getSpawnPoint(tile)
            val mobBase = getRandomEnemy()
            mobBase.setUniqueId(MathHelper.getRandomUUID(rand))
            val id = getEntityName(mobBase)
            if (!Strings.isNullOrEmpty(id)) {
                val nbt = NBTTagCompound()
                mobBase.writeToNBT(nbt)
                nbt.setString("id", id!!)
                val entityAttempt = EntityList.createEntityFromNBT(nbt, tile.world)
                if (entityAttempt is EntityLiving) {
                    val entitySpawned = spawnMonsterAtLocation(tile, entityAttempt, position.x, position.y, position.z)
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

    private fun getEntityName(entity: EntityLiving): String? = EntityList.getKey(entity)?.toString()

    private fun getSpawnPoint(tile: TileEndWell): Vec3d {
        var spawnPoint: Vec3d? = null
        while (spawnPoint == null) {
            val d0 = tile.pos.x.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
            val d1 = (tile.pos.y + tile.world.rand.nextInt(3) - 1).toDouble()
            val d2 = tile.pos.z.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
            val distance = RangeUtil.getDistance(d0, d1, d2, tile.pos.x.toDouble(), tile.pos.y.toDouble(), tile.pos.z.toDouble())
            if (distance > tile.getMaxRadius())
                spawnPoint = Vec3d(d0, d1, d2)
        }
        return spawnPoint
    }

    private fun spawnMonsterAtLocation(tile: TileEndWell, entity: EntityLiving, x: Double, y: Double, z: Double): EntityLiving? {
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch)
        if (entity.isNotColliding && !entity.isDead && mobList.size < maxNumberOfMobs) {
            if (tile.world.spawnEntity(entity))
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
        val compound = nbt.getCompoundTag("mob_spawner_data")
        if (compound.hasKey("id_list", 9)) {
            val list = nbt.getTagList("id_list", 10)
            if (list.hasNoTags()) return
            val it = list.iterator()
            while (it.hasNext()) {
                val tag = it.next() as NBTTagCompound
                val least = tag.getLong("least")
                val most = tag.getLong("most")
                val id = UUID(most, least)
                updateIDs.add(id)
            }
        }
    }

    fun writeToNBT(nbt: NBTTagCompound) {
        val compound = NBTTagCompound()
        if (mobList.isEmpty()) return
        val list = NBTTagList()
        for (entity in mobList) {
            if (!entity.isDead) {
                val id = entity.uniqueID
                val entityNBT = NBTTagCompound()
                entityNBT.setLong("least", id.leastSignificantBits)
                entityNBT.setLong("most", id.mostSignificantBits)
                list.appendTag(entityNBT)
            }
        }
        compound.setTag("id_list", list)
        nbt.setTag("mob_spawner_data", compound)
    }
}