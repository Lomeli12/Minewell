package net.lomeli.minewell.client.effects

import net.lomeli.minewell.Minewell
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.FMLClientHandler
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.GLU
import org.lwjgl.util.glu.Sphere

object GLCallList {
    private val mc = FMLClientHandler.instance().client
    var sphereOutID = 0
    var sphereInID = 0

    fun initSpheres() {
        val sphere = Sphere()
        sphere.drawStyle = GLU.GLU_FILL
        sphere.normals = GLU.GLU_SMOOTH

        sphere.orientation = GLU.GLU_OUTSIDE
        sphereOutID = GL11.glGenLists(1)
        GL11.glNewList(sphereOutID, GL11.GL_COMPILE)
        mc.textureManager.bindTexture(ResourceLocation(Minewell.MOD_ID, "textures/effects/sphere.png"))
        sphere.draw(1f, 32, 32)
        GL11.glEndList()

        sphere.orientation = GLU.GLU_INSIDE
        sphereInID = GL11.glGenLists(1)
        GL11.glNewList(sphereInID, GL11.GL_COMPILE)
        mc.textureManager.bindTexture(ResourceLocation(Minewell.MOD_ID, "textures/effects/sphere.png"))
        sphere.draw(1f, 32, 32)
        GL11.glEndList()
    }
}