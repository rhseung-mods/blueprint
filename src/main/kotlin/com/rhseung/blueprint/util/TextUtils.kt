package com.rhseung.blueprint.util

import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text

object TextUtils {
    fun Text.getStyleSmart(): Style? {
        return this.style.takeIf { !it.isEmpty }
            ?: this.siblings.find { !it.style.isEmpty }?.style;
    }

    fun OrderedText.toText(): Text {
        val text = Text.empty();

        this.accept { idx, style, codePoint ->
            text.append(Text.literal(Character.toString(codePoint)).setStyle(style));
            return@accept true;
        }

        return text;
    }
}