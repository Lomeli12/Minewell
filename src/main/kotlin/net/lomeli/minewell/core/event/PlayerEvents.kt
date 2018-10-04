package net.lomeli.minewell.core.event

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.core.util.RangeUtil
import net.minecraft.init.Items
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object PlayerEvents {
    @JvmStatic @SubscribeEvent fun playerUsedItem(event: PlayerInteractEvent.RightClickItem) {
        if (!Minewell.debug) return
        val player = event.entityPlayer
        if (player.world.isRemote) return
        val stack = event.itemStack
        if (stack.item == Items.STICK) {
            val pos = RangeUtil.isEntityNearWell(player, false)
            if (pos != null) {
                val playerDistance = player.getDistance(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                player.sendMessage(TextComponentString("Distance: $playerDistance"))
            }
        }
    }
}