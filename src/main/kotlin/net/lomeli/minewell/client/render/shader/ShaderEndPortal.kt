package net.lomeli.minewell.client.render.shader

import net.lomeli.minewell.client.helpers.ShaderHelper
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.ARBShaderObjects
import net.minecraft.client.Minecraft



object ShaderEndPortal {
    val END_PORTAL_TEXTURE = ResourceLocation("textures/entity/end_portal.png")

    val enderCallback = EnderCallback()
    var endPortalShader = 0

    init {
        if (ShaderHelper.useShaders())
            endPortalShader = ShaderHelper.createProgram("/assets/minewell/shaders/end_portal.vert",
                    "/assets/minewell/shaders/end_portal.frag")
    }
}

class EnderCallback: ShaderCallback() {
    override fun call(shader: Int, newFrame: Boolean) {
        val mc = Minecraft.getMinecraft()

        val x = ARBShaderObjects.glGetUniformLocationARB(shader, "yaw")
        ARBShaderObjects.glUniform1fARB(x, (mc.player.rotationYaw * 2 * Math.PI / 360.0).toFloat())

        val z = ARBShaderObjects.glGetUniformLocationARB(shader, "pitch")
        ARBShaderObjects.glUniform1fARB(z, -((mc.player.rotationPitch * 2 * Math.PI / 360.0).toFloat()))
    }
}