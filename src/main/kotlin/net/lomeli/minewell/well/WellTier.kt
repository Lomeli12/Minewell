package net.lomeli.minewell.well

import com.sun.scenario.effect.impl.state.GaussianRenderState.MAX_RADIUS
import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.helpers.MobSpawnerHelper
import net.lomeli.minewell.core.helpers.NetworkHelper
import net.lomeli.minewell.core.util.BossTracker
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentTranslation
import java.util.*
import kotlin.collections.ArrayList

abstract class WellTier {
    private var stage = Stage.STAGE_ONE_CHARGING
    private var currentKills = 0
    private var bossTracker: BossTracker? = null
    private var mobSpawnerHelper: MobSpawnerHelper? = null
    private var failure = true

    open fun initTier(tile: TileEndWell) {
        mobSpawnerHelper = MobSpawnerHelper(getTierMobs(tile), 15, 5)
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
                if (getCurrentKills() >= getKillsNeeded()) {
                    tile.world.playBroadcastSound(1028, tile.pos, 0)
                    failure = false
                    giverPlayersRewards(tile)
                    tile.setTimer(0)
                }
            }
        }
    }

    fun giverPlayersRewards(tile: TileEndWell) {
        val world = tile.world
        val pos = tile.pos
        val playerList = world.getEntitiesWithinAABB(EntityPlayer::class.java,
                AxisAlignedBB(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble(), pos.x + 1.0, pos.y - 1.0, pos.z + 1.0)
                        .grow(MAX_DISTANCE.toDouble()))
        if (playerList.isEmpty()) return
        for (player in playerList) {
            val distance = player.getDistance(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble())
            if (distance <= MAX_RADIUS) {
                if (!world.isRemote)
                    player.sendMessage(TextComponentTranslation("event.minewell.success"))
                val rewards = getRandomRewards(world.rand)
                if (rewards.isNotEmpty()) {
                    for (item in rewards) {
                        if (!world.isRemote) {
                            val entity = EntityItem(world, player.posX, player.posY, player.posZ, item)
                            world.spawnEntity(entity)
                        }
                    }
                }
            }
        }
    }

    fun getRandomRewards(rand: Random): Array<ItemStack> {
        val rewards = ArrayList<ItemStack>()
        for (reward in possibleRewards()) {
            if (reward.getChance() <= rand.nextFloat())
                rewards.add(reward.getStack())
        }
        return rewards.toTypedArray()
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

    abstract fun getTierMobs(tile: TileEndWell): Array<WellEnemy>
    abstract fun getKillsNeeded(): Int
    abstract fun getUnlocalizedName(): String
    abstract fun getRegistryName(): String
    abstract fun getTierBosses(tile: TileEndWell): Array<out EntityLiving>
    abstract fun possibleRewards(): Array<WellReward>

    fun isFailure(): Boolean = failure

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