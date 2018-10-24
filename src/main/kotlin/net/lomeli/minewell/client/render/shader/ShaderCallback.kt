package net.lomeli.minewell.client.render.shader

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
abstract class ShaderCallback {
    abstract fun call(shader: Int, newFrame: Boolean)
}