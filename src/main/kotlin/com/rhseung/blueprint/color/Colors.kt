package com.rhseung.blueprint.color

import net.minecraft.util.Formatting

object Colors {
    val EMPTY = ColorRGB(0xFFFFFF).zeroAlpha();
    val BLACK = ColorRGB(Formatting.BLACK.colorValue!!).fullAlpha();
    val DARK_BLUE = ColorRGB(Formatting.DARK_BLUE.colorValue!!).fullAlpha();
    val DARK_GREEN = ColorRGB(Formatting.DARK_GREEN.colorValue!!).fullAlpha();
    val DARK_AQUA = ColorRGB(Formatting.DARK_AQUA.colorValue!!).fullAlpha();
    val DARK_RED = ColorRGB(Formatting.DARK_RED.colorValue!!).fullAlpha();
    val DARK_PURPLE = ColorRGB(Formatting.DARK_PURPLE.colorValue!!).fullAlpha();
    val GOLD = ColorRGB(Formatting.GOLD.colorValue!!).fullAlpha();
    val GRAY = ColorRGB(Formatting.GRAY.colorValue!!).fullAlpha();
    val DARK_GRAY = ColorRGB(Formatting.DARK_GRAY.colorValue!!).fullAlpha();
    val BLUE = ColorRGB(Formatting.BLUE.colorValue!!).fullAlpha();
    val GREEN = ColorRGB(Formatting.GREEN.colorValue!!).fullAlpha();
    val AQUA = ColorRGB(Formatting.AQUA.colorValue!!).fullAlpha();
    val RED = ColorRGB(Formatting.RED.colorValue!!).fullAlpha();
    val LIGHT_PURPLE = ColorRGB(Formatting.LIGHT_PURPLE.colorValue!!).fullAlpha();
    val YELLOW = ColorRGB(Formatting.YELLOW.colorValue!!).fullAlpha();
    val WHITE = ColorRGB(Formatting.WHITE.colorValue!!).fullAlpha();
}