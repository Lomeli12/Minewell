package net.lomeli.minewell.well

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.tiers.TierOne
import net.minecraft.nbt.NBTTagCompound

object TierRegistry {
    private val tiers = HashMap<String, Class<out WellTier>>()

    init {
        addTierClass("tier.minewell.tier_one", TierOne::class.java)
    }

    fun addTierClass(name: String, tierClass: Class<out WellTier>) {
        if (!tiers.containsKey(name))
            tiers[name] = tierClass
    }

    fun getTierFromName(name: String, nbt: NBTTagCompound?, tile: TileEndWell): WellTier? {
        if (tiers.containsKey(name)) {
            val clazz = tiers[name]!!
            val tier = clazz.newInstance() as WellTier
            tier.initTier(tile)
            if (nbt != null)
                tier.readFromNBT(nbt)
            return tier
        }
        return null
    }
}