package com.rhseung.blueprint.util

import net.minecraft.client.data.TextureKey
import net.minecraft.client.data.TextureMap
import net.minecraft.util.Identifier

object CollectionUtils {
    fun <T> List<T>.subList(fromIndex: Int): List<T> = this.subList(fromIndex, this.size);

    fun Map<TextureKey, Identifier>.toTextureMap(): TextureMap {
        val ret = TextureMap();
        this.forEach(ret::put);
        return ret;
    }
}