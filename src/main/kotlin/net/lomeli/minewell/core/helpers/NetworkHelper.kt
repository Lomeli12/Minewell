package net.lomeli.minewell.core.helpers

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.MAX_DISTANCE
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.network.MessageUpdateTileState
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.network.NetworkRegistry

object NetworkHelper {
    fun updateClientsWithinRange(currentKills: Int, killsNeeded: Int, tile: TileEndWell) {
        Minewell.packetHandler.sendToAllAround(createUpdatePacket(tile),
                NetworkRegistry.TargetPoint(tile.world.provider.dimension,
                        tile.pos.x + 0.5, tile.pos.y + 0.5, tile.pos.z + 0.5, MAX_DISTANCE + 0.5))
    }

    fun updateClient(tile: TileEndWell, player: EntityPlayerMP) {
        Minewell.packetHandler.sendTo(createUpdatePacket(tile), player)
    }

    private fun createUpdatePacket(tile: TileEndWell): MessageUpdateTileState {
        val pos = tile.pos
        val timer = tile.getTimer()
        var stage = -1
        var kills = -1
        if (tile.getTier() != null) {
            stage = tile.getTier()!!.getCurrentStage().ordinal
            kills = tile.getTier()!!.getCurrentKills()
        }
        return MessageUpdateTileState(timer, kills, stage, pos)
    }
}