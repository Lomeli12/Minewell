package net.lomeli.minewell.item

import net.lomeli.minewell.lib.EXOTIC
import net.lomeli.minewell.well.WellTier
import net.lomeli.minewell.well.tiers.TierOne
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

class ItemLightCharge : ItemBase("light_charge") {
    private val tierName = arrayOf("tier_one", "tier_two", "tier_three")

    init {
        hasSubtypes = true
        maxDamage = 0
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val meta = stack.metadata
        val name = if (meta in 0..(tierName.size - 1)) tierName[meta] else tierName[0]
        return super.getUnlocalizedName(stack) + ".$name"
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return when (stack.metadata) {
            0 -> EnumRarity.RARE
            1 -> EnumRarity.EPIC
            2 -> EXOTIC ?: super.getRarity(stack)
            else -> super.getRarity(stack)
        }
    }

    fun getTierFromStack(stack: ItemStack): WellTier {
        //TODO: Create more tiers!
        when (stack.metadata) {
            0 -> return TierOne()
        }
        return TierOne()
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        for (i in 0..(tierName.size - 1))
            items.add(ItemStack(this, 1, i))
    }
}