package com.rhseung.blueprint.lang

import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.color.ColorRGB
import com.rhseung.blueprint.util.CollectionUtils.subList
import net.minecraft.text.MutableText
import net.minecraft.text.Text

data class TranslatableText(val name: String) {
    val key: String = Blueprint.modid("translate.$name").toTranslationKey();
    private val stringBuilder = StringBuilder();

    val value: String
        get() = stringBuilder.toString();

    init {
        VALUES.add(this);
    }

    fun add(str: String, color: ColorRGB): TranslatableText {
        stringBuilder.append("§r§{$color}$str");
        return this;
    }

    fun getText(vararg args: Any): MutableText {
        val formatted: String = Text.translatable(key, *args).string;
        val text = Text.empty();

        formatted.split("§r§{").forEachIndexed { i, part ->
            val splited = part.split("}");

            if (splited.size > 1) {
                val color = ColorRGB(splited[0]);
                val value = splited.subList(1).joinToString("}");
                text.append(Text.literal(value).withColor(color.toInt()));
            } else {
                text.append(part);
            }
        };

        return text;
    }

    companion object {
        val VALUES = mutableListOf<TranslatableText>();
    }
}