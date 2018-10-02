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
    @JvmStatic @SubscribeEvent fun registerModel(event: ModelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(ModItems.END_WELL_ITEM, 0,
                ModelResourceLocation("${Minewell.MOD_ID}:end_well", "inventory"))
    }
}