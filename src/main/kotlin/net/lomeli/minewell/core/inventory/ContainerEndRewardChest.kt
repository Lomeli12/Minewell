package net.lomeli.minewell.core.inventory

import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.lomeli.minewell.core.inventory.slot.SlotReward
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

class ContainerEndRewardChest : Container {
    private val tile: TileEndRewardChest
    private val chestInventory: ItemStackHandler

    constructor(playerInventory: InventoryPlayer, chestInventory: ItemStackHandler, tile: TileEndRewardChest) {
        this.tile = tile
        this.chestInventory = chestInventory
        for (i in 0..2) {
            for (j in 0..2) {
                this.addSlotToContainer(SlotReward(this.chestInventory, j + i * 3, 62 + j * 18, 17 + i * 18))
            }
        }


        for (k in 0..2) {
            for (i1 in 0..8) {
                this.addSlotToContainer(Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18))
            }
        }

        for (l in 0..8) {
            this.addSlotToContainer(Slot(playerInventory, l, 8 + l * 18, 142))
        }
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        super.onContainerClosed(playerIn)
        tile.closeChest()
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var itemstack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()

            if (index < 9) {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                    return ItemStack.EMPTY
            } else if (!mergeItemStack(itemstack1, 0, 9, false))
                return ItemStack.EMPTY

            if (itemstack1.isEmpty)
                slot.putStack(ItemStack.EMPTY)
            else
                slot.onSlotChanged()

            if (itemstack1.count == itemstack.count)
                return ItemStack.EMPTY

            slot.onTake(playerIn, itemstack1)
        }

        return itemstack
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean = true
}