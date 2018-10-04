package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell
import net.minecraft.nbt.NBTTagCompound

abstract class WellTier {
    private var stage = Stage.STAGE_ONE_CHARGING
    private var currentKills = 0

    open fun updateTick(tile: TileEndWell) {
        when (stage) {
            Stage.STAGE_ONE_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_ONE)
            }
            Stage.STAGE_ONE -> {
                stageOne(tile)
                if (getCurrentKills() >= getStageOneKills())
                    changeTier(tile, Stage.STAGE_TWO_CHARGING)
            }
            Stage.STAGE_TWO_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_TWO)
            }
            Stage.STAGE_TWO -> {
                stageTwo(tile)
                if (getCurrentKills() >= getStageTwoKills())
                    changeTier(tile, Stage.STAGE_THREE_CHARGING)
            }
            Stage.STAGE_THREE_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_THREE)
            }
            Stage.STAGE_THREE -> {
                stageThree(tile)
                if (getCurrentKills() >= getStageThreeKills())
                    changeTier(tile, Stage.BOSS_CHARGING)
            }
            Stage.BOSS_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.BOSS)
            }
            Stage.BOSS -> {
                bossStage(tile)
            }
        }
    }

    fun changeTier(tile: TileEndWell, stage: Stage) {
        this.currentKills = 0
        this.stage = stage
        tile.setTimer(this.stage.getMaxTime())
    }

    abstract fun stageOne(tile: TileEndWell)

    abstract fun getStageOneKills(): Int

    abstract fun stageTwo(tile: TileEndWell)

    abstract fun getStageTwoKills(): Int

    abstract fun stageThree(tile: TileEndWell)

    abstract fun getStageThreeKills(): Int

    abstract fun bossStage(tile: TileEndWell)

    fun getCurrentKills() = currentKills

    fun addKills(value: Int) {
        currentKills += value
    }

    abstract fun getUnlocalizedName(): String

    fun getCurrentStage(): Stage = stage

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    abstract fun getRegistryName(): String

    fun writeToNBT(nbt: NBTTagCompound) {
        nbt.setString("end_well_tier", getRegistryName())
        val wellData = NBTTagCompound()
        wellData.setInteger("kills", getCurrentKills())
        wellData.setInteger("stage", stage.ordinal)
        nbt.setTag("end_well_data", wellData)
    }

    fun readFromNBT(nbt: NBTTagCompound) {
        if (!nbt.hasKey("end_well_data", 10)) return
        val wellData = nbt.getTag("end_well_data") as NBTTagCompound
        currentKills = wellData.getInteger("kills")
        stage = STAGE_VALUES[wellData.getInteger("stage")]
    }
}