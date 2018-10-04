package net.lomeli.minewell.entity

import net.lomeli.minewell.Minewell
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object ModEntities {
    private val LIGHT_ORB_NAME = ResourceLocation(Minewell.MOD_ID, "light_orb")

    fun registerEntities() {
        EntityRegistry.registerModEntity(LIGHT_ORB_NAME, EntityLight::class.java, "light_orb", 0, Minewell, 32, 1, true)
    }
}