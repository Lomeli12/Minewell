package net.lomeli.minewell.lib

import net.minecraft.client.resources.I18n
import net.minecraft.util.text.TextFormatting

enum class EnumItemRarity {
    COMMON("rarity.minewell.common", TextFormatting.WHITE),
    UNCOMMON("rarity.minewell.uncommon", TextFormatting.GREEN),
    RARE("rarity.minewell.rare", TextFormatting.AQUA),
    LEGENDARY("rarity.minewell.legendary", TextFormatting.LIGHT_PURPLE),
    EXOTIC("rarity.minewell.exotic", TextFormatting.GOLD);

    private val tooltipText: String
    private val textColor: TextFormatting

    constructor(text: String, color: TextFormatting) {
        tooltipText = text
        textColor = color
    }

    fun getTooltipText(): String = I18n.format(tooltipText)

    fun getTextColor(): TextFormatting = textColor
}