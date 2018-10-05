package net.lomeli.minewell.core.util

import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos

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
                        var playerDistance = -1f
                        if (activeWell) {
                            if (tile.isWellActivated())
                                entity.getDistance(newX, newY - 2, newZ)
                        } else
                            entity.getDistance(newX, newY, newZ)
                        if (playerDistance != -1f && playerDistance <= MAX_DISTANCE)
                            return pos
                    }
                }
        return null
    }
}