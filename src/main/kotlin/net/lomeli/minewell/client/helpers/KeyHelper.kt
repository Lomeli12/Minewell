package net.lomeli.minewell.client.helpers

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

@SideOnly(Side.CLIENT)
object KeyHelper {

    fun isKeyDown(keyCode: Int): Boolean {
        try {
            return Keyboard.isKeyDown(keyCode)
        } catch (e: Exception) {
        }
        return false
    }

    fun isShiftDown(): Boolean = isKeyDown(Keyboard.KEY_LSHIFT) || isKeyDown(Keyboard.KEY_RSHIFT)
}