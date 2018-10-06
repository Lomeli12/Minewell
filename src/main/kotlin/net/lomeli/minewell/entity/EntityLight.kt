package net.lomeli.minewell.entity

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.helpers.SimpleMoveHelper
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class EntityLight(world: World) : Entity(world) {
    private val SPEED = 1.0
    private var targetPos: BlockPos? = null
    private val moveHelper = SimpleMoveHelper(this)

    init {
        setSize(0.25f, 0.25f)
        setNoGravity(true)
        noClip = true
    }

    constructor(world: World, x: Double, y: Double, z: Double, targetPos: BlockPos) : this(world) {
        setPosition(x, y, z)
        this.targetPos = targetPos
        moveHelper.setTargetPos(targetPos.x + 0.5, targetPos.y + 0.5, targetPos.z + 0.5, SPEED)
    }

    override fun entityInit() {
    }

    override fun onUpdate() {
        moveHelper.onEntityUpdate()
        if (targetPos == null || world.getTileEntity(targetPos!!) !is TileEndWell) setDead()

        if (targetPos != null) {
            val vec3d = Vec3d(posX, posY, posZ)
            val vec3d1 = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)
            val result = world.rayTraceBlocks(vec3d, vec3d1)
            if (result != null) {
                if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    val pos = result.blockPos
                    val tile = world.getTileEntity(pos)
                    if (tile is TileEndWell && tile.getTier() != null) {
                        tile.getTier()!!.addKills(1)
                        targetPos = null
                        moveHelper.clearTarget()
                        stopEntity()
                        setDead()
                    }
                }
            }
        }
    }

    fun stopEntity() {
        motionX = 0.0
        motionY = 0.0
        motionZ = 0.0
    }

    @SideOnly(Side.CLIENT)
    override fun getBrightnessForRender(): Int = 15728880

    override fun getBrightness(): Float = 1f

    override fun canBeAttackedWithItem(): Boolean = false

    override fun isBurning(): Boolean = false

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        if (targetPos != null)
            compound.setLong("target_pos", targetPos!!.toLong())
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        if (compound.hasKey("target_pos", 4)) {
            targetPos = BlockPos.fromLong(compound.getLong("target_pos"))
            moveHelper.setTargetPos(targetPos!!.x + 0.5, targetPos!!.y + 0.5, targetPos!!.z + 0.5, SPEED)
        }
    }
}