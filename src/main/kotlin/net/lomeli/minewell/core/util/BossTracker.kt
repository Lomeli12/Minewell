package net.lomeli.minewell.core.util

import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class BossTracker(boss: Array<out EntityLiving>) {

    private val mobList = ArrayList<EntityLiving>()
    private val maxNumberOfMobs: Int
    private var hasSpawned = false
    private var updateIDs = ArrayList<UUID>()

    init {
        mobList.addAll(boss)
        maxNumberOfMobs = boss.size
    }

    fun updateTracker(tier: WellTier, tile: TileEndWell) {
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
        spawnBosses(tile)
        if (mobList.size > 0) {
            val it = mobList.iterator()
            while (it.hasNext()) {
                val boss = it.next()
                if (boss.isDead) {
                    it.remove()
                    tier.addKills(1)
                }
            }
        }
    }

    private fun spawnBosses(tier: WellTier, tile: TileEndWell) {
        if (hasSpawned) return
        for (boss in mobList)
            spawnBoss(boss, tile)
    }

    private fun spawnBoss(boss: EntityLiving, tile: TileEndWell) {
        while (true) {
            val position = getSpawnPoint(tile)
            val entitySpawned = spawnMonsterAtLocation(boss, position.x, position.y, position.z)
            if (entitySpawned != null) {
                entitySpawned.forceSpawn = true
                entitySpawned.enablePersistence()
                break
            }
        }
    }

    private fun getSpawnPoint(tile: TileEndWell): Vec3d {
        val d0 = tile.pos.x.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
        val d1 = (tile.pos.y + tile.world.rand.nextInt(3) - 1).toDouble()
        val d2 = tile.pos.z.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
        return Vec3d(d0, d1, d2)
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
        hasSpawned = compound.getBoolean("has_spawned")

    }

    fun writeToNBT(nbt: NBTTagCompound) {
        val compound = NBTTagCompound()
        compound.setBoolean("has_spawned", hasSpawned)
        if (mobList.isNotEmpty()) {
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
        }
        nbt.setTag("boss_spawner_data", compound)
    }
}