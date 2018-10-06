package net.lomeli.minewell.core.util

import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.EntityLiving
import net.minecraft.util.math.Vec3d

class BossTracker(boss: Array<out EntityLiving>) {

    private val mobList = ArrayList<EntityLiving>()
    private val maxNumberOfMobs: Int
    private var hasSpawned = false

    init {
        mobList.addAll(boss)
        maxNumberOfMobs = boss.size
    }

    fun updateTracker(tier: WellTier, tile: TileEndWell) {
        spawnBosses(tier, tile)
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
}