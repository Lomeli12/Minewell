package net.lomeli.minewell.well

import net.minecraft.item.ItemStack

/**
 * @param stack - ItemStack to possibly give player.
 * @param chance - Change of ItemStack dropping. 1f == 100% chance.
 * Try to also give at least one guaranteed reward.
 */
class WellReward(private val stack: ItemStack, private val chance: Float) {
    fun getStack(): ItemStack = stack
    fun getChance(): Float = chance
}