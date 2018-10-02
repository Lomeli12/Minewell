package net.lomeli.minewell.client

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.client.effects.GLCallList
import net.lomeli.minewell.client.render.tile.TileEndWellRenderer
import net.lomeli.minewell.core.Proxy
import net.minecraftforge.client.model.obj.OBJLoader
import net.minecraftforge.fml.client.registry.ClientRegistry

class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
        OBJLoader.INSTANCE.addDomain(Minewell.MOD_ID)
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