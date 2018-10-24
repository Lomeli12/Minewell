package net.lomeli.minewell.block.tile

import net.lomeli.minewell.block.ModBlocks
import net.lomeli.minewell.core.util.MobUtil
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.ITickable
import net.minecraft.util.SoundCategory
import net.minecraftforge.items.ItemStackHandler
import java.util.*

class TileEndRewardChest : TileEntity(), ITickable {
    private val MAX_LIFE_SPAN = 300
    private val INVENTORY_SIZE = 9
    private var secondsLive = 0
    private val playerRewards = HashMap<UUID, ItemStackHandler>()
    var lidAngle = 0f
    var prevLidAngle = 0f
    var numPlayersUsing = 0

    fun getPlayerInventory(player: EntityPlayer): ItemStackHandler? =
            if (playerRewards.containsKey(player.persistentID)) playerRewards[player.persistentID] else ItemStackHandler(INVENTORY_SIZE)

    fun addPlayerReward(player: EntityPlayer, tier: WellTier) {
        if (MobUtil.isFakePlayer(player)) return
        val id = player.persistentID
        val rewards = tier.getRandomRewards(world.rand)
        val inventory = ItemStackHandler(INVENTORY_SIZE)
        var i = 0
        rewards.forEach { item ->
            if (i < INVENTORY_SIZE)
                inventory.insertItem(i, item, false)
            i++
        }
        playerRewards[id] = inventory
    }

    override fun update() {
        if (world == null) return

        prevLidAngle = lidAngle

        val i = pos.x
        val j = pos.y
        val k = pos.z

        if (numPlayersUsing > 0) {
            if (lidAngle == 0f) {
                val d0 = i.toDouble() + 0.5
                val d1 = k.toDouble() + 0.5
                world.playSound(null as EntityPlayer?, d0, j.toDouble() + 0.5, d1, SoundEvents.BLOCK_CHEST_OPEN,
                        SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1f + 0.9f)
            }
            lidAngle += 0.1f
            if (lidAngle > 1f) lidAngle = 1f
            markDirty()
        } else if (numPlayersUsing < 1) {
            if (lidAngle == 1f) {
                val d3 = i.toDouble() + 0.5
                val d2 = k.toDouble() + 0.5
                world.playSound(null as EntityPlayer?, d3, j.toDouble() + 0.5, d2,
                        SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f,
                        world.rand.nextFloat() * 0.1f + 0.9f)
            }
            lidAngle -= 0.1f
            if (lidAngle < 0f) lidAngle = 0f
            markDirty()
        }

        secondsLive++
        if (secondsLive >= MAX_LIFE_SPAN * 20) {
            spawnParticles()
            world.setBlockToAir(pos)
        }
    }

    private fun spawnParticles() {
        for (i in 0..10) {
            val d0 = pos.x + world.rand.nextDouble()
            val d1 = pos.y + world.rand.nextDouble()
            val d2 = pos.z + world.rand.nextDouble()
            world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, 0.0, 0.0, 0.0)
        }
    }

    override fun receiveClientEvent(id: Int, type: Int): Boolean {
        if (id == 1) {
            this.numPlayersUsing = type
            return true
        } else
            return super.receiveClientEvent(id, type)
    }

    override fun invalidate() {
        updateContainingBlockInfo()
        super.invalidate()
    }

    fun openChest() {
        ++numPlayersUsing
        world.addBlockEvent(pos, ModBlocks.REWARD_CHEST, 1, numPlayersUsing)
    }

    fun closeChest() {
        --numPlayersUsing
        world.addBlockEvent(pos, ModBlocks.REWARD_CHEST, 1, numPlayersUsing)
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        secondsLive = nbt.getInteger("seconds_alive")
        if (nbt.hasKey("player_list", 9)) {
            val list = nbt.getTagList("player_list", 10)
            if (!list.hasNoTags()) {
                for (i in 0..(list.tagCount() - 1)) {
                    val tag = list.getCompoundTagAt(i)

                    val least = tag.getLong("least")
                    val most = tag.getLong("most")
                    val id = UUID(most, least)

                    val items = tag.getCompoundTag("inventory")
                    val inventory = ItemStackHandler(INVENTORY_SIZE)
                    inventory.deserializeNBT(items)

                    playerRewards[id] = inventory
                }
            }
        }
        super.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        nbt.setInteger("seconds_alive", secondsLive)
        if (playerRewards.isNotEmpty()) {
            val itemList = NBTTagList()
            for (playerSet in playerRewards) {
                val tag = NBTTagCompound()
                tag.setLong("least", playerSet.key.leastSignificantBits)
                tag.setLong("most", playerSet.key.mostSignificantBits)
                val items = playerSet.value.serializeNBT()
                tag.setTag("inventory", items)
                itemList.appendTag(tag)
            }
            nbt.setTag("player_list", itemList)
        }
        return super.writeToNBT(nbt)
    }

    override fun getUpdateTag(): NBTTagCompound = writeToNBT(NBTTagCompound())

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        val nbt = NBTTagCompound()
        writeToNBT(nbt)
        return SPacketUpdateTileEntity(pos, 1, nbt)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
    }
}