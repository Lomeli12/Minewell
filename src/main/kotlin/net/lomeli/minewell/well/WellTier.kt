package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.helpers.MobSpawnerHelper
import net.lomeli.minewell.core.helpers.NetworkHelper
import net.lomeli.minewell.core.util.BossTracker
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound

abstract class WellTier {
    private var stage = Stage.STAGE_ONE_CHARGING
    private var currentKills = 0
    private var bossTracker: BossTracker? = null
    private var mobSpawnerHelper: MobSpawnerHelper? = null

    open fun initTier(tile: TileEndWell) {
        mobSpawnerHelper = MobSpawnerHelper(getTierMobs(tile), 30, 5)
        bossTracker = BossTracker(getTierBosses(tile))
    }

    open fun updateTick(tile: TileEndWell) {
        when (stage) {
            Stage.STAGE_ONE_CHARGING -> {
                if (tile.getTimer() <= 0)
                    changeTier(tile, Stage.STAGE_ONE)
            }
            Stage.STAGE_ONE -> {
                mobSpawnerHelper?.spawnMonsters(tile)
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.STAGE_TWO_CHARGING)
            }
            Stage.STAGE_TWO_CHARGING -> {
                mobSpawnerHelper?.destroyTrackedMobs()
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_TWO)
            }
            Stage.STAGE_TWO -> {
                mobSpawnerHelper?.spawnMonsters(tile)
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.STAGE_THREE_CHARGING)
            }
            Stage.STAGE_THREE_CHARGING -> {
                mobSpawnerHelper?.destroyTrackedMobs()
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_THREE)
            }
            Stage.STAGE_THREE -> {
                mobSpawnerHelper?.spawnMonsters(tile)
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.BOSS_CHARGING)
            }
            Stage.BOSS_CHARGING -> {
                mobSpawnerHelper?.destroyTrackedMobs()
                if (tile.getTimer() <= 0) {
                    changeTier(tile, Stage.BOSS)
                }
            }
            Stage.BOSS -> {
                bossTracker?.updateTracker(this, tile)
            }
        }
    }

    fun clearMobs() {
        mobSpawnerHelper?.destroyTrackedMobs()
        bossTracker?.destroyTrackedMobs()
    }

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    fun setKills(kills: Int) {
        currentKills = kills
    }

    private fun changeTier(tile: TileEndWell, stage: Stage) {
        currentKills = 0
        this.stage = stage
        tile.setTimer(this.stage.getMaxTime())
        if (!tile.world.isRemote)
            NetworkHelper.updateClientsWithinRange(currentKills, getKillsNeeded(), tile)
    }

    fun getCurrentKills() = currentKills

    fun addKills(value: Int, tile: TileEndWell) {
        currentKills += value
        if (!tile.world.isRemote)
            NetworkHelper.updateClientsWithinRange(currentKills, getKillsNeeded(), tile)
    }

    abstract fun getTierMobs(tile: TileEndWell): Array<out EntityLiving>
    abstract fun getKillsNeeded(): Int
    abstract fun getUnlocalizedName(): String
    abstract fun getRegistryName(): String
    abstract fun getTierBosses(tile: TileEndWell): Array<out EntityLiving>

    fun getCurrentStage(): Stage = stage

    fun writeToNBT(nbt: NBTTagCompound) {
        nbt.setString("end_well_tier", getRegistryName())
        val wellData = NBTTagCompound()
        wellData.setInteger("kills", getCurrentKills())
        wellData.setInteger("stage", stage.ordinal)
        nbt.setTag("end_well_data", wellData)
        mobSpawnerHelper?.writeToNBT(nbt)
        bossTracker?.writeToNBT(nbt)
    }

    fun readFromNBT(nbt: NBTTagCompound) {
        if (!nbt.hasKey("end_well_data", 10)) return
        val wellData = nbt.getTag("end_well_data") as NBTTagCompound
        currentKills = wellData.getInteger("kills")
        stage = STAGE_VALUES[wellData.getInteger("stage")]
        mobSpawnerHelper?.readFromNBT(nbt)
        bossTracker?.readFromNBT(nbt)
    }
}