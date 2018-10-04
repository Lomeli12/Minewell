/**
 * Original class written by Vazkii for Botania.
 */
package net.lomeli.minewell.client.helpers

import gnu.trove.map.hash.TIntFloatHashMap
import net.lomeli.minewell.Minewell
import net.lomeli.minewell.client.handler.ClientTickHandler
import net.lomeli.minewell.client.render.shader.ShaderCallback
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.ARBFragmentShader
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.ARBVertexShader
import org.lwjgl.opengl.GL11
import java.io.BufferedReader
import java.io.InputStreamReader


object ShaderHelper {
    private val VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB
    private val FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB
    private val prevTime = TIntFloatHashMap()

    fun useShader(shader: Int, callback: ShaderCallback?) {
        if (!useShaders()) return
        ARBShaderObjects.glUseProgramObjectARB(shader)

        if (shader != 0) {
            val frameTime = ClientTickHandler.ticksInGame + ClientTickHandler.midGameTick
            val newFrame = frameTime != prevTime[shader]

            if (newFrame) {
                val time = ARBShaderObjects.glGetUniformLocationARB(shader, "time")
                ARBShaderObjects.glUniform1fARB(time, frameTime)
                prevTime.put(shader, frameTime)
            }
            callback?.call(shader, newFrame)
        }
    }

    fun useShader(shader: Int) {
        useShader(shader, null)
    }

    fun releaseShader() {
        useShader(0)
    }

    //TODO: Add config to disable shaders
    fun useShaders(): Boolean = OpenGlHelper.shadersSupported

    // Most of the code taken from the LWJGL wiki
    // http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL
    fun createProgram(vert: String?, frag: String?): Int {
        var vertId = 0
        var fragId = 0
        var program = 0
        if (vert != null)
            vertId = createShader(vert, VERT)
        if (frag != null)
            fragId = createShader(frag, FRAG)
        program = ARBShaderObjects.glCreateProgramObjectARB()
        if (program == 0)
            return 0
        if (vert != null)
            ARBShaderObjects.glAttachObjectARB(program, vertId)
        if (frag != null)
            ARBShaderObjects.glAttachObjectARB(program, fragId)
        ARBShaderObjects.glLinkProgramARB(program)
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            Minewell.log.logError(getLogInfo(program))
            ARBShaderObjects.glDeleteObjectARB(program)
            return 0
        }
        ARBShaderObjects.glValidateProgramARB(program)
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            Minewell.log.logError(getLogInfo(program))
            ARBShaderObjects.glDeleteObjectARB(program)
            return 0
        }
        prevTime.put(program, -1f)
        return program
    }

    private fun createShader(filename: String, shaderType: Int): Int {
        var shader = 0
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType)

            if (shader == 0)
                return 0

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename))
            ARBShaderObjects.glCompileShaderARB(shader)

            if (ARBShaderObjects.glGetObjectParameteriARB(shader,
                            ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw RuntimeException("Error creating shader: " + getLogInfo(shader))

            return shader
        } catch (e: Exception) {
            ARBShaderObjects.glDeleteObjectARB(shader)
            e.printStackTrace()
            return -1
        }

    }

    private fun getLogInfo(obj: Int): String {
        return ARBShaderObjects.glGetInfoLogARB(obj,
                ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB))
    }

    @Throws(Exception::class)
    private fun readFileAsString(filename: String): String {
        val source = StringBuilder()
        val input = ShaderHelper::class.java.getResourceAsStream(filename)
        var exception: Exception? = null
        val reader: BufferedReader

        if (input == null)
            return ""

        try {
            reader = BufferedReader(InputStreamReader(input, "UTF-8"))

            var innerExc: Exception? = null
            try {
                var line = reader.readLine()
                while (line != null) {
                    source.append(line).append('\n')
                    line = reader.readLine()
                }
            } catch (exc: Exception) {
                exception = exc
            } finally {
                try {
                    reader.close()
                } catch (exc: Exception) {
                    if (innerExc == null)
                        innerExc = exc
                    else
                        exc.printStackTrace()
                }

            }

            if (innerExc != null)
                throw innerExc
        } catch (exc: Exception) {
            exception = exc
        } finally {
            try {
                input.close()
            } catch (exc: Exception) {
                if (exception == null)
                    exception = exc
                else
                    exc.printStackTrace()
            }

            if (exception != null)
                throw exception
        }

        return source.toString()
    }
}