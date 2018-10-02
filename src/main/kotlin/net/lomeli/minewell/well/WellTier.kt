package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell

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

    abstract fun getTierName(): String

    fun getCurrentStage(): Stage = stage
}