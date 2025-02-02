package com.rhseung.blueprint.util

object StringUtils {
    fun String.titlecase(): String {
        return this.lowercase().split(Regex(" +")).joinToString(" ") {
            it.split("_").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
        };
    }
}