package com.rhseung.blueprint.util

import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.util.Identifier

object CollectionUtils {
    fun <T> List<T>.subList(fromIndex: Int): List<T> = this.subList(fromIndex, this.size);

    fun Map<TextureKey, Identifier>.toTextureMap(): TextureMap {
        val ret = TextureMap();
        this.forEach(ret::put);
        return ret;
    }
}