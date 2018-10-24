package net.lomeli.minewell.core.inventory.slot

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler

class SlotReward(inventory: IItemHandler, index: Int, x: Int, y: Int): SlotItemHandler(inventory, index, x, y) {
    override fun isItemValid(stack: ItemStack): Boolean = false
}