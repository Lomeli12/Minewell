package net.lomeli.minewell.item

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.item.itemblock.ItemEndWell
import net.lomeli.minewell.item.materials.ItemMaterials
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModItems {
    //ItemBlocks
    val END_WELL_ITEM = ItemEndWell()

    //Items
    val LIGHT_CHARGE = ItemLightCharge()
    val VOID_INGOT = ItemMaterials("void_ingot").setRarity(EnumRarity.UNCOMMON)
    val VOID_NUGGET = ItemMaterials("void_nugget").setRarity(EnumRarity.UNCOMMON)

    @JvmStatic @SubscribeEvent fun registerModItems(event: RegistryEvent.Register<Item>) {
        registerBlockItems(event)
        registerItems(event)
    }

    fun registerBlockItems(event: RegistryEvent.Register<Item>) {
        Minewell.log.logInfo("Registering Block Items")
        event.registry.register(END_WELL_ITEM)
    }

    fun registerItems(event: RegistryEvent.Register<Item>) {
        Minewell.log.logInfo("Registering Items")
        event.registry.register(LIGHT_CHARGE)
        event.registry.register(VOID_INGOT)
        event.registry.register(VOID_NUGGET)
    }
}