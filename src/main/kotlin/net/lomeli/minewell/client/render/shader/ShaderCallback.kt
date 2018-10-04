package net.lomeli.minewell.client.render.shader

abstract class ShaderCallback {
    abstract fun call(shader: Int, newFrame: Boolean)
}