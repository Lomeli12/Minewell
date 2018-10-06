package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.helpers.MobSpawnerHelper
import net.lomeli.minewell.core.util.BossTracker
import net.minecraft.entity.EntityLiving
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World

abstract class WellTier {
    private var stage = Stage.STAGE_ONE_CHARGING
    private var currentKills = 0
    private var mobSpawnerHelper: MobSpawnerHelper? = null
    private var bossTracker: BossTracker? = null

    open fun updateTick(tile: TileEndWell) {
        if (mobSpawnerHelper == null)
            mobSpawnerHelper = MobSpawnerHelper(tile, getTierMobs(), getTierPotionEffects(), 30, 5)
        when (stage) {
            Stage.STAGE_ONE_CHARGING -> {
                if (tile.getTimer() <= 0)
                    changeTier(tile, Stage.STAGE_ONE)
            }
            Stage.STAGE_ONE -> {
                mobSpawnerHelper!!.spawnMonsters()
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.STAGE_TWO_CHARGING)
            }
            Stage.STAGE_TWO_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_TWO)
            }
            Stage.STAGE_TWO -> {
                mobSpawnerHelper!!.spawnMonsters()
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.STAGE_THREE_CHARGING)
            }
            Stage.STAGE_THREE_CHARGING -> {
                if (tile.getTimer() <= 0) changeTier(tile, Stage.STAGE_THREE)
            }
            Stage.STAGE_THREE -> {
                mobSpawnerHelper!!.spawnMonsters()
                if (getCurrentKills() >= getKillsNeeded())
                    changeTier(tile, Stage.BOSS_CHARGING)
            }
            Stage.BOSS_CHARGING -> {
                if (tile.getTimer() <= 0) {
                    bossTracker = BossTracker(getTierBosses(tile.world))
                    changeTier(tile, Stage.BOSS)
                }
            }
            Stage.BOSS -> {
                bossTracker!!.updateTracker(this, tile)
            }
        }
    }

    private fun changeTier(tile: TileEndWell, stage: Stage) {
        this.currentKills = 0
        this.stage = stage
        tile.setTimer(this.stage.getMaxTime())
    }

    fun getCurrentKills() = currentKills

    fun addKills(value: Int) {
        currentKills += value
    }

    abstract fun getTierMobs(): Array<Class<out EntityLiving>>
    abstract fun getTierPotionEffects(): Array<PotionEffect>
    abstract fun getKillsNeeded(): Int
    abstract fun getUnlocalizedName(): String
    abstract fun getRegistryName(): String
    abstract fun getTierBosses(world: World): Array<out EntityLiving>

    fun getCurrentStage(): Stage = stage

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