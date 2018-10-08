package net.lomeli.minewell.core.util

import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

object RangeUtil {
    fun isEntityNearWell(entity: Entity, activeWell: Boolean): BlockPos? {
        val distance = MAX_DISTANCE.toInt()
        for (x in -distance..distance)
            for (y in -distance..distance)
                for (z in -distance..distance) {
                    val newX = entity.posX + x
                    val newY = entity.posY + y
                    val newZ = entity.posZ + z
                    val pos = BlockPos(newX, newY, newZ)
                    val tile = entity.world.getTileEntity(pos)
                    if (tile is TileEndWell) {
                        var playerDistance = -1.0
                        if (activeWell) {
                            if (tile.isWellActivated())
                                playerDistance = entity.getDistance(newX, newY - 2, newZ)
                        } else
                            playerDistance = entity.getDistance(newX, newY, newZ)
                        if (playerDistance != -1.0 && playerDistance <= MAX_DISTANCE)
                            return pos
                    }
                }
        return null
    }

    fun get2DDistance(x: Double, y: Double, targetX: Double, targetY: Double): Double {
        val d0 = targetX - x
        val d1 = targetY - y
        return MathHelper.sqrt(d0 * d0 + d1 * d1).toDouble()
    }

    fun getPlayersInRange(maxRange: Double, pos: BlockPos, world: World): ArrayList<EntityPlayer> {
        val list = ArrayList<EntityPlayer>()
        val range = AxisAlignedBB(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble(), pos.x + 1.0, pos.y - 1.0, pos.z + 1.0)
                .grow(maxRange)
        val playerList = world.getEntitiesWithinAABB(EntityPlayer::class.java, range)
        if (playerList.isNotEmpty()) {
            for (player in playerList) {
                val distance = player.getDistance(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble())
                if (distance <= maxRange) list.add(player)
            }
        }
        return list
    }
}