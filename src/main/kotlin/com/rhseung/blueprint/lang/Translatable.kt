package com.rhseung.blueprint.lang

import net.minecraft.text.MutableText
import net.minecraft.text.Text

interface Translatable {
    val translationKey: String;

    fun getText(): MutableText {
        return Text.translatable(translationKey);
    }
}