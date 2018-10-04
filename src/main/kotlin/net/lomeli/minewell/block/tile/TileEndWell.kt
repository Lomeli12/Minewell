package net.lomeli.minewell.block.tile

import net.lomeli.minewell.well.WellTier
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.world.EnumDifficulty

const val MAX_DISTANCE = 20f

class TileEndWell : TileEntity(), ITickable {
    private val MAX_RADIUS = 5.5f
    private val RADIUS_RATE = 0.05f
    private var eventTier: WellTier? = null
    var radius = 0f
    private var timer = 0

    override fun update() {
        if (world.difficulty == EnumDifficulty.PEACEFUL) {
            eventTier = null
            timer = 0
        }
        if (eventTier != null && timer > 0) {
            timer--

            if (radius < MAX_RADIUS) {
                radius += RADIUS_RATE
                if (radius > MAX_RADIUS)
                    radius = MAX_RADIUS
            }

            eventTier!!.updateTick(this)

            if (timer <= 0) {
                eventTier = null
            }
            this.markDirty()
        } else {
            radius = 0f
        }
    }

    fun setTier(tier: WellTier) {
        if (!isWellActivated() && world.difficulty != EnumDifficulty.PEACEFUL) {
            eventTier = tier
            setTimer(tier.getCurrentStage().getMaxTime())
        }
    }

    fun getTier(): WellTier? = eventTier

    fun isWellActivated(): Boolean = eventTier != null

    fun setTimer(timeInSec: Int) {
        timer = timeInSec * 20
    }

    fun getTimer(): Int = timer
}