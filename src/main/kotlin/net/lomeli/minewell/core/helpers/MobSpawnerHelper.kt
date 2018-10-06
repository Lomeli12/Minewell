package net.lomeli.minewell.core.helpers

import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.potion.ModPotions
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.Vec3d
import java.util.*

class MobSpawnerHelper(private val tile: TileEndWell, private val mobClassList: Array<Class<out EntityLiving>>,
                       private val potionEffects: Array<PotionEffect>, private val maxNumberOfMobs: Int,
                       private val spawnSise: Int) {
    private val rand = Random()
    private val mobList = ArrayList<EntityLiving>()

    fun spawnMonsters() {
        if (tile.getTimer() % 30 == 0) {
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
                    spawnNewMob()
                    if (mobList.size == maxNumberOfMobs)
                        break
                }
            }
        }
    }

    private fun spawnNewMob() {
        for (attempt in 0..4) {
            val position = getSpawnPoint()
            val mobClass = mobClassList[rand.nextInt(mobClassList.size)]
            val entityAttempt = EntityList.newEntity(mobClass, tile.world) as EntityLiving
            val entitySpawned = spawnMonsterAtLocation(entityAttempt, position.x, position.y, position.z)
            if (entitySpawned != null) {
                entitySpawned.forceSpawn = true
                entitySpawned.enablePersistence()

                // Apply light and other potion effects
                entitySpawned.addPotionEffect(PotionEffect(ModPotions.LIGHT, Int.MAX_VALUE))
                for (effect in potionEffects)
                    entitySpawned.addPotionEffect(effect)

                mobList.add(entitySpawned)
                break
            }
        }
    }

    private fun getSpawnPoint(): Vec3d {
        val d0 = tile.pos.x.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
        val d1 = (tile.pos.y + tile.world.rand.nextInt(3) - 1).toDouble()
        val d2 = tile.pos.z.toDouble() + (tile.world.rand.nextDouble() - tile.world.rand.nextDouble()) * MAX_DISTANCE
        return Vec3d(d0, d1, d2)
    }

    private fun spawnMonsterAtLocation(entity: EntityLiving, x: Double, y: Double, z: Double): EntityLiving? {
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch)
        if (entity.isNotColliding && !entity.isDead && mobList.size < maxNumberOfMobs) {
            if (tile.world.spawnEntity(entity))
                return entity
        }
        return null
    }

}