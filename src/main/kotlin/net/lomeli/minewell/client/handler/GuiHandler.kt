package net.lomeli.minewell.client.handler

import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.lomeli.minewell.client.gui.GuiEndRewardChest
import net.lomeli.minewell.core.inventory.ContainerEndRewardChest
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

object GuiHandler: IGuiHandler {
    override fun getClientGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        when(ID) {
            -1 -> {
                val tile = world!!.getTileEntity(BlockPos(x, y, z))
                if (tile is TileEndRewardChest) {
                    val rewards = tile.getPlayerInventory(player!!)
                    if (rewards != null)
                        return GuiEndRewardChest(player.inventory, rewards, tile)
                }
            }
        }
        return null
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        when(ID) {
            -1 -> {
                val tile = world!!.getTileEntity(BlockPos(x, y, z))
                if (tile is TileEndRewardChest) {
                    val rewards = tile.getPlayerInventory(player!!)
                    if (rewards != null)
                        return ContainerEndRewardChest(player.inventory, rewards, tile)
                }
            }
        }
        return null
    }


}