package net.lomeli.minewell.core.util

import net.lomeli.minewell.lib.EFFECT_RANGE
import net.lomeli.minewell.lib.MAX_DISTANCE
import net.lomeli.minewell.lib.MAX_RADIUS
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.collections.ArrayList

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

    fun keepMobsHostile(list: ArrayList<EntityLiving>, pos: BlockPos, world: World) {
        if (list.isEmpty()) return
        for (entity in list) {
            if (entity.attackTarget !is EntityPlayer)
                entity.attackTarget = getRandomPlayerNearCrystal(pos, world)
        }
    }

    private fun getRandomPlayerNearCrystal(pos: BlockPos, world: World): EntityPlayer? {
        val playerList = RangeUtil.getPlayersInRange(EFFECT_RANGE, pos, world)
        if (playerList.isEmpty()) return null
        return if (playerList.size == 1) playerList[0] else playerList[world.rand.nextInt(playerList.size)]
    }
}