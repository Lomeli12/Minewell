package net.lomeli.minewell.core.util

import net.lomeli.minewell.lib.MAX_DISTANCE
import net.lomeli.minewell.lib.MAX_RADIUS
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

object MobUtil {
    fun getEntityName(entity: EntityLiving): String? = EntityList.getKey(entity)?.toString()

    fun getSpawnPoint(pos: BlockPos, rand: Random): Vec3d {
        var spawnPoint: Vec3d? = null
        while (spawnPoint == null) {
            val d0 = pos.x.toDouble() + (rand.nextDouble() - rand.nextDouble()) * MAX_DISTANCE
            val d1 = (pos.y + rand.nextInt(3) - 1).toDouble()
            val d2 = pos.z.toDouble() + (rand.nextDouble() - rand.nextDouble()) * MAX_DISTANCE
            val distance = RangeUtil.get2DDistance(d0, d2, pos.x.toDouble(), pos.z.toDouble())
            if (distance > MAX_RADIUS)
                spawnPoint = Vec3d(d0, d1, d2)
        }
        return spawnPoint
    }

    fun spawnMonsterAtLocation(entity: EntityLiving, x: Double, y: Double, z: Double, currentSize: Int, mobCap: Int): EntityLiving? {
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch)
        if (entity.isNotColliding && !entity.isDead && currentSize < mobCap) {
            if (entity.world.spawnEntity(entity))
                return entity
        }
        return null
    }
}