package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Minewell.MOD_ID)
object ModPotions {
    val POTION_TEXTURE = ResourceLocation(Minewell.MOD_ID, "textures/effects/potion.png")

    val SKY_TOUCH = PotionSkyTouch()
    val HARMONY = PotionHarmony()
    val VOID_TOUCH = PotionVoidTouch()
    val LIGHT = PotionLight()

    @JvmStatic @SubscribeEvent fun registerPotions(event: RegistryEvent.Register<Potion>){
        event.registry.register(SKY_TOUCH)
        event.registry.register(HARMONY)
        event.registry.register(VOID_TOUCH)
        event.registry.register(LIGHT)
    }
}