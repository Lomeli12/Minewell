package net.lomeli.minewell.item

import net.lomeli.minewell.lib.EnumItemRarity
import net.lomeli.minewell.well.WellTier
import net.lomeli.minewell.well.tiers.TierOne
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

class ItemLightCharge : ItemBase("light_charge"), IItemRarity, ILoreInfo {
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

    override fun getItemRarity(stack: ItemStack): EnumItemRarity {
        return when (stack.metadata) {
            0 -> EnumItemRarity.RARE
            1 -> EnumItemRarity.LEGENDARY
            2 -> EnumItemRarity.EXOTIC
            else -> EnumItemRarity.COMMON
        }
    }

    override fun getLoreText(stack: ItemStack): String? {
        return when (stack.metadata) {
            0 -> "item.minewell.light_charge.tier_one.lore"
            1 -> "item.minewell.light_charge.tier_two.lore"
            2 -> "item.minewell.light_charge.tier_three.lore"
            else -> null
        }
    }

    fun getTierFromStack(stack: ItemStack): WellTier {
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