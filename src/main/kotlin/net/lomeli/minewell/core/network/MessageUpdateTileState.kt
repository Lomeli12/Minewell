package net.lomeli.minewell.core.network

import io.netty.buffer.ByteBuf
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.STAGE_VALUES
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessageUpdateTileState : IMessage, IMessageHandler<MessageUpdateTileState, IMessage> {
    private var pos = 0L
    private var timer = 0
    private var stage = 0
    private var currentKills = 0

    constructor()

    constructor(timer: Int, currentKills: Int, stage: Int, pos: BlockPos) {
        this.timer = timer
        this.stage = stage
        this.currentKills = currentKills
        this.pos = pos.toLong()
    }

    override fun fromBytes(buf: ByteBuf) {
        this.pos = buf.readLong()
        this.timer = buf.readInt()
        this.stage = buf.readInt()
        this.currentKills = buf.readInt()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeLong(pos)
        buf.writeInt(timer)
        buf.writeInt(stage)
        buf.writeInt(currentKills)
    }

    override fun onMessage(message: MessageUpdateTileState?, ctx: MessageContext?): IMessage? {
        if (message != null && ctx != null) {
            val mc = FMLClientHandler.instance().client
            val player = mc.player
            val pos = BlockPos.fromLong(message.pos)
            val tile = player.world.getTileEntity(pos)
            if (tile is TileEndWell) {
                tile.setTimer(message.timer / 20)
                val tier = tile.getTier()
                if (tier != null) {
                    if (message.currentKills > -1)
                        tier.setKills(message.currentKills)
                    if (message.stage > -1)
                        tier.setStage(STAGE_VALUES[message.stage])
                }
            }
        }
        return null
    }
}