package net.lomeli.minewell.core.event

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.core.util.RangeUtil
import net.lomeli.minewell.entity.EntityLight
import net.lomeli.minewell.potion.ModPotions
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object EntityEvents {
    @JvmStatic
    @SubscribeEvent
    fun livingDeathEvent(event: LivingDeathEvent) {
        if (event.entityLiving !is EntityPlayer) {
            val entity = event.entityLiving
            if (entity.isPotionActive(ModPotions.LIGHT) && event.source.trueSource is EntityPlayer) {
                val pos = RangeUtil.isEntityNearWell(entity, true)
                if (pos != null) {
                    val orbLight = EntityLight(entity.world, entity.posX, entity.posY + (entity.height / 2), entity.posZ, pos)
                    if (!entity.world.isRemote)
                        entity.world.spawnEntity(orbLight)
                    //TODO: Loot Tables for hi-end drops
                }
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun entityDropEvent(event: LivingDropsEvent) {
        if (event.entityLiving !is EntityPlayer) {
            val entity = event.entityLiving
            if (entity.isPotionActive(ModPotions.LIGHT))
                event.isCanceled = true
        }
    }
}