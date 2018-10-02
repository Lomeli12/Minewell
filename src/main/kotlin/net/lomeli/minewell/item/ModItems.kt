package net.lomeli.minewell.item

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.ModBlocks
import net.lomeli.minewell.item.itemblock.ItemEndWell
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModItems {
    val END_WELL_ITEM = ItemEndWell()

    @JvmStatic @SubscribeEvent fun registerModItems(event: RegistryEvent.Register<Item>) {
        registerBlockItems(event)
        registerItems(event)
    }

    fun registerBlockItems(event: RegistryEvent.Register<Item>) {
        Minewell.log.logInfo("Registering Block Items")
        event.registry.register(END_WELL_ITEM.setRegistryName(ModBlocks.END_WELL.registryName))
    }

    fun registerItems(event: RegistryEvent.Register<Item>) {
        Minewell.log.logInfo("Registering Items")
    }
}