package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell
import net.minecraft.entity.EntityLivingBase
import java.util.ArrayList

class TierOne: WellTier() {

    private val mobList = ArrayList<EntityLivingBase>()

    init {

    }

    override fun stageOne(tile: TileEndWell) {
        val it = mobList.iterator()
        while (it.hasNext()) {
            val entity = it.next()
            if (entity.isDead) {

            }
        }
    }

    override fun getStageOneKills(): Int = 50

    override fun stageTwo(tile: TileEndWell) {
    }

    override fun getStageTwoKills(): Int = 75

    override fun stageThree(tile: TileEndWell) {
    }

    override fun getStageThreeKills(): Int = 100

    override fun bossStage(tile: TileEndWell) {
    }


    override fun getTierName(): String = "event.minewell.tier.one"
}