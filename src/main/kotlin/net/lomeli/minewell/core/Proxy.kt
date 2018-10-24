package net.lomeli.minewell.core

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.lomeli.minewell.client.handler.GuiHandler
import net.lomeli.minewell.core.network.MessageUpdateTileState
import net.lomeli.minewell.entity.ModEntities
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side

open class Proxy {
    open fun preInit() {
        Minewell.log.logInfo("Pre-Init Event")

        Minewell.packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Minewell.MOD_ID)
        Minewell.packetHandler.registerMessage(MessageUpdateTileState::class.java, MessageUpdateTileState::class.java, 0, Side.CLIENT)

        ModEntities.registerEntities()
        GameRegistry.registerTileEntity(TileEndWell::class.java, ResourceLocation(Minewell.MOD_ID, "tile_end_well"))
        GameRegistry.registerTileEntity(TileEndRewardChest::class.java, ResourceLocation(Minewell.MOD_ID, "tile_end_well_chest"))
    }

    open fun init() {
        Minewell.log.logInfo("Init Event")
        NetworkRegistry.INSTANCE.registerGuiHandler(Minewell.instance, GuiHandler)
    }

    open fun postInit() {
        Minewell.log.logInfo("Post-Init Event")
    }
}