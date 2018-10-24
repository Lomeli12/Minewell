package net.lomeli.minewell.client.model

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.item.ModItems
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModModels {
    @JvmStatic
    @SubscribeEvent
    fun registerModel(event: ModelRegistryEvent) {
        registerBlockModel(event)
        registerItemModel(event)
    }

    fun registerBlockModel(event: ModelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(ModItems.END_WELL_ITEM, 0,
                ModelResourceLocation("${Minewell.MOD_ID}:end_well", "inventory"))
    }

    fun registerItemModel(event: ModelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(ModItems.LIGHT_CHARGE, 0,
                ModelResourceLocation("${Minewell.MOD_ID}:light_charge_tier_one", "inventory"))
        ModelLoader.setCustomModelResourceLocation(ModItems.LIGHT_CHARGE, 1,
                ModelResourceLocation("${Minewell.MOD_ID}:light_charge_tier_two", "inventory"))
        ModelLoader.setCustomModelResourceLocation(ModItems.LIGHT_CHARGE, 2,
                ModelResourceLocation("${Minewell.MOD_ID}:light_charge_tier_three", "inventory"))

        ModelLoader.setCustomModelResourceLocation(ModItems.VOID_INGOT, 0,
                ModelResourceLocation("${Minewell.MOD_ID}:void_ingot", "inventory"))
        ModelLoader.setCustomModelResourceLocation(ModItems.VOID_NUGGET, 0,
                ModelResourceLocation("${Minewell.MOD_ID}:void_nugget", "inventory"))
    }
}