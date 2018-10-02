package net.lomeli.minewell.block

import net.lomeli.minewell.Minewell
import net.minecraft.block.Block
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModBlocks {

    val END_WELL = BlockEndWell()

    @JvmStatic @SubscribeEvent fun registerBlocks(event: RegistryEvent.Register<Block>) {
        Minewell.log.logInfo("Registering blocks")
        event.registry.register(END_WELL)
    }

}