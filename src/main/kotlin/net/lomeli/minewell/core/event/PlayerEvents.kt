package net.lomeli.minewell.core.event

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.core.helpers.NetworkHelper
import net.lomeli.minewell.core.util.RangeUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.living.LivingSpawnEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object PlayerEvents {
    @JvmStatic
    @SubscribeEvent
    fun playerUsedItem(event: PlayerInteractEvent.RightClickItem) {
        if (!Minewell.debug) return
        val player = event.entityPlayer
        val stack = event.itemStack
        if (stack.item == Items.STICK) {
            val pos = RangeUtil.isEntityNearWell(player, false)
            if (pos != null) {
                if (!player.world.isRemote) {
                    val playerDistance = player.getDistance(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                    player.sendMessage(TextComponentString("Distance: $playerDistance"))
                }
                val tile = player.world.getTileEntity(pos) as TileEndWell
                val tier = tile.getTier()
                if (player.world.isRemote) {
                    Minewell.log.logInfo("Client Timer: ${tile.getTimer()}")
                    Minewell.log.logInfo("Client Radius: ${tile.radius}")
                    if (tier != null) {
                        Minewell.log.logInfo("Client Stage: ${tier.getCurrentStage()}")
                        Minewell.log.logInfo("Client Current Kills: ${tier.getCurrentKills()}")
                        Minewell.log.logInfo("Client Required Kills: ${tier.getKillsNeeded()}")
                    }
                } else {
                    Minewell.log.logInfo("Server Timer: ${tile.getTimer()}")
                    Minewell.log.logInfo("Server Radius: ${tile.radius}")
                    if (tier != null) {
                        Minewell.log.logInfo("Server Stage: ${tier.getCurrentStage()}")
                        Minewell.log.logInfo("Server Current Kills: ${tier.getCurrentKills()}")
                        Minewell.log.logInfo("Server Required Kills: ${tier.getKillsNeeded()}")
                    }
                }
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun playerSpawned(event: LivingSpawnEvent) {
        val player = event.entityLiving
        if (player is EntityPlayerMP) {
            val pos = RangeUtil.isEntityNearWell(player, false)
            if (pos != null) {
                val tile = player.world.getTileEntity(pos) as TileEndWell
                NetworkHelper.updateClient(tile, player)
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun playerTick(event: LivingEvent.LivingUpdateEvent) {
        val player = event.entityLiving
        if (player is EntityPlayer) {

        }
    }
}