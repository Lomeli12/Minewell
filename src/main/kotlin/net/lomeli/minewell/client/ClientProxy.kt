package net.lomeli.minewell.client

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.client.effects.GLCallList
import net.lomeli.minewell.client.render.entity.RenderLightFactory
import net.lomeli.minewell.client.render.tile.TileEndWellRenderer
import net.lomeli.minewell.core.Proxy
import net.lomeli.minewell.entity.EntityLight
import net.minecraftforge.client.model.obj.OBJLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry

class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
        OBJLoader.INSTANCE.addDomain(Minewell.MOD_ID)
        RenderingRegistry.registerEntityRenderingHandler(EntityLight::class.java, RenderLightFactory())
    }

    override fun init() {
        super.init()
        GLCallList.initSpheres()
        ClientRegistry.bindTileEntitySpecialRenderer(TileEndWell::class.java, TileEndWellRenderer())
    }

    override fun postInit() {
        super.postInit()
    }
}