package net.lomeli.minewell.potion

import net.lomeli.minewell.Minewell

class PotionLight: PotionBase(false, 0x00ffe9) {

    init {
        setIconIndex(3, 0)
        setPotionName("potion.${Minewell.MOD_ID}.light")
        setRegName("light")
    }
}