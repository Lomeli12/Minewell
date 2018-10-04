package net.lomeli.minewell.core.util

import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

class SimpleMoveHelper {
    private var posX = 0.0
    private var posY = 0.0
    private var posZ = 0.0
    private var speed = 0.0
    private var hasTarget = false
    private val entity: Entity

    constructor(entity: Entity) {
        this.entity = entity
    }

    fun setTargetPos(pos: BlockPos, speed: Double) =
            setTargetPos(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), speed)

    fun setTargetPos(x: Double, y: Double, z: Double, speed: Double) {
        posX = x
        posY = y
        posZ = z
        this.speed = speed
        hasTarget = true
    }

    fun clearTarget() {
        posX = 0.0
        posY = 0.0
        posZ = 0.0
        speed = 0.0
        hasTarget = true
    }

    fun onEntityUpdate() {
        entity.lastTickPosX = entity.posX
        entity.lastTickPosY = entity.posY
        entity.lastTickPosZ = entity.posZ

        entity.posX += entity.motionX
        entity.posY += entity.motionY
        entity.posZ += entity.motionZ

        if (hasTarget) {
            val d0 = posX - entity.posX
            val d1 = posZ - entity.posZ
            val d2 = posY - entity.posY
            val d3 = d0 * d0 + d2 * d2 + d1 * d1

            val f9 = (MathHelper.atan2(d1, d0) * (180.0 / Math.PI)).toFloat() - 90.0f
            entity.rotationYaw = limitAngle(entity.rotationYaw, f9, 90.0f)

            val x = (d0 / d3) * speed
            val y = (d2 / d3) * speed
            val z = (d1 / d3) * speed

            entity.motionX = x
            entity.motionY = y
            entity.motionZ = z
        }
    }

    private fun limitAngle(sourceAngle: Float, targetAngle: Float, maximumChange: Float): Float {
        var f = MathHelper.wrapDegrees(targetAngle - sourceAngle)

        if (f > maximumChange)
            f = maximumChange

        if (f < -maximumChange)
            f = -maximumChange

        var f1 = sourceAngle + f

        if (f1 < 0.0f)
            f1 += 360.0f
        else if (f1 > 360.0f)
            f1 -= 360.0f

        return f1
    }
}