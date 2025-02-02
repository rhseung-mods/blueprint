package com.rhseung.blueprint.render

import com.rhseung.blueprint.color.ColorARGB
import com.rhseung.blueprint.util.DrawUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier

data class Texture(val id: Identifier, val textureWidth: Int, val textureHeight: Int) {
    var u: Int = 0;
    var v: Int = 0;
    var width: Int = textureWidth;
    var height: Int = textureHeight;

    fun uv(u: Int, v: Int): Texture {
        val ret = Texture(id, textureWidth, textureHeight);
        ret.u = u;
        ret.v = v;
        ret.width = textureWidth - u;
        ret.height = textureHeight - v;

        return ret;
    }

    fun uv(u: Int, v: Int, width: Int, height: Int): Texture {
        val ret = Texture(id, textureWidth, textureHeight);
        ret.u = u;
        ret.v = v;
        ret.width = width;
        ret.height = height;

        return ret;
    }

    fun draw(context: DrawContext, x: Float, y: Float, color: ColorARGB) {
        DrawUtils.drawGuiTexture(
            context,
            RenderLayer::getGuiTextured,
            id,
            textureWidth,
            textureHeight,
            u,
            v,
            x,
            y,
            width,
            height,
            color.toInt()
        );
    }
}