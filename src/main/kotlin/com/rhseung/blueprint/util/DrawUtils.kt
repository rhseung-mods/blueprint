package com.rhseung.blueprint.util

import com.rhseung.blueprint.util.ReflectionUtils.get
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.texture.GuiAtlasManager
import net.minecraft.client.texture.Scaling
import net.minecraft.client.texture.Scaling.NineSlice
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import org.joml.Matrix4f
import java.util.function.Function
import kotlin.math.min

object DrawUtils {
    fun getSprite(context: DrawContext, id: Identifier): Sprite {
        val guiAtlasManager: GuiAtlasManager = context["guiAtlasManager"];
        return guiAtlasManager.getSprite(id);
    }

    fun getScaling(context: DrawContext, id: Identifier): Scaling {
        val guiAtlasManager: GuiAtlasManager = context["guiAtlasManager"];
        val sprite = guiAtlasManager.getSprite(id);
        return guiAtlasManager.getScaling(sprite);
    }

    fun drawGuiTexture(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, id: Identifier, x: Float, y: Float, width: Int, height: Int, color: Int = -1) {
        val sprite: Sprite = getSprite(context, id);
        val scaling: Scaling = getScaling(context, id);

        if (scaling is Scaling.Stretch)
            drawSpriteStretched(context, renderLayers, sprite, x, y, width, height, color);
        else if (scaling is Scaling.Tile)
            drawSpriteTiled(context, renderLayers, sprite, x, y, width, height, 0, 0, scaling.width(), scaling.height(), scaling.width(), scaling.height(), color);
        else if (scaling is NineSlice)
            drawSpriteNineSliced(context, renderLayers, sprite, scaling, x, y, width, height, color);
    }

    fun drawGuiTexture(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, id: Identifier, textureWidth: Int, textureHeight: Int, u: Int, v: Int, x: Float, y: Float, width: Int, height: Int, color: Int = -1) {
        val sprite: Sprite = getSprite(context, id);
        val scaling: Scaling = getScaling(context, id);

        if (scaling is Scaling.Stretch)
            drawSpriteRegion(context, renderLayers, sprite, textureWidth, textureHeight, u, v, x, y, width, height, color);
        else if (scaling is Scaling.Tile)
            drawSpriteStretched(context, renderLayers, sprite, x, y, width, height, color);
    }

    fun drawSpriteRegion(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, sprite: Sprite, textureWidth: Int, textureHeight: Int, u: Int, v: Int, x: Float, y: Float, width: Int, height: Int, color: Int) {
        if (width != 0 && height != 0) {
            drawTexturedQuad(context, renderLayers, sprite.atlasId, x, x + width, y, y + height, sprite.getFrameU(u.toFloat() / textureWidth.toFloat()), sprite.getFrameU((u + width).toFloat() / textureWidth.toFloat()), sprite.getFrameV(v.toFloat() / textureHeight.toFloat()), sprite.getFrameV((v + height).toFloat() / textureHeight.toFloat()), color);
        }
    }

    fun drawSpriteStretched(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, sprite: Sprite, x: Float, y: Float, width: Int, height: Int, color: Int) {
        if (width != 0 && height != 0) {
            drawTexturedQuad(context, renderLayers, sprite.atlasId, x, x + width, y, y + height, sprite.minU, sprite.maxU, sprite.minV, sprite.maxV, color);
        }
    }

    fun drawSpriteTiled(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, sprite: Sprite, x: Float, y: Float, width: Int, height: Int, u: Int, v: Int, tileWidth: Int, tileHeight: Int, textureWidth: Int, textureHeight: Int, color: Int) {
        if (width > 0 && height > 0) {
            if (tileWidth > 0 && tileHeight > 0) {
                var i = 0;
                while (i < width) {
                    val j = min(tileWidth.toDouble(), (width - i).toDouble()).toInt();

                    var k = 0;
                    while (k < height) {
                        val l = min(tileHeight.toDouble(), (height - k).toDouble()).toInt();

                        drawSpriteRegion(context, renderLayers, sprite, textureWidth, textureHeight, u, v, x + i, y + k, j, l, color);
                        k += tileHeight;
                    }
                    i += tileWidth;
                }
            }
            else
                throw IllegalArgumentException("Tiled sprite texture size must be positive, got " + tileWidth + "x" + tileHeight);
        }
    }

    fun drawSpriteNineSliced(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, sprite: Sprite, nineSlice: NineSlice, x: Float, y: Float, width: Int, height: Int, color: Int) {
        val border = nineSlice.border();
        val i = min(border.left().toDouble(), (width / 2).toDouble()).toInt();
        val j = min(border.right().toDouble(), (width / 2).toDouble()).toInt();
        val k = min(border.top().toDouble(), (height / 2).toDouble()).toInt();
        val l = min(border.bottom().toDouble(), (height / 2).toDouble()).toInt();

        if (width == nineSlice.width && height == nineSlice.height) {
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, height, color);
        }
        else if (height == nineSlice.height) {
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, height, color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x + i, y, width - j - i, height, i, 0, nineSlice.width() - j - i, nineSlice.height(), nineSlice.width(), nineSlice.height(), color);
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(),nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, height, color);
        }
        else if (width == nineSlice.width) {
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, k, color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x, y + k, width, height - l - k, 0, k, nineSlice.width(), nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, width, l, color);
        }
        else {
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, k, color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x + i, y, width - j - i, k, i, 0, nineSlice.width() - j - i, k, nineSlice.width(), nineSlice.height(), color);
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, k, color);
            drawSpriteRegion(context,renderLayers, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, i, l, color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x + i, y + height - l, width - j - i, l, i, nineSlice.height() - l, nineSlice.width() - j - i, l, nineSlice.width(), nineSlice.height(), color);
            drawSpriteRegion(context, renderLayers, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, nineSlice.height() - l, x + width - j, y + height - l, j, l, color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x, y + k, i, height - l - k, 0, k, i, nineSlice.height() - l - k, nineSlice.width(),nineSlice.height(), color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x + i, y + k, width - j - i, height - l - k, i, k, nineSlice.width() - j - i, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
            drawInnerSprite(context, renderLayers, nineSlice, sprite, x + width - j, y + k, i, height - l - k, nineSlice.width() - j, k, j, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
        }
    }

    fun drawInnerSprite(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, nineSlice: NineSlice, sprite: Sprite, x: Float, y: Float, width: Int, height: Int, u: Int, v: Int, tileWidth: Int, tileHeight: Int, textureWidth: Int, textureHeight: Int, color: Int) {
        if (width > 0 && height > 0) {
            if (nineSlice.stretchInner)
                drawSpriteRegion(context, renderLayers, sprite, textureWidth, textureHeight, u, v, x, y, width, height, color);
            else
                drawSpriteTiled(context, renderLayers, sprite, x, y, width, height, u, v, tileWidth, tileHeight, textureWidth, textureHeight, color);
        }
    }

    fun drawTexturedQuad(context: DrawContext, renderLayers: Function<Identifier, RenderLayer>, texture: Identifier, x1: Float, x2: Float, y1: Float, y2: Float, u1: Float, u2: Float, v1: Float, v2: Float, color: Int) {
        val renderLayer: RenderLayer = renderLayers.apply(texture);
        val matrix: Matrix4f = context.matrices.peek().positionMatrix;
        val vertexConsumerProvider: VertexConsumerProvider.Immediate = context["vertexConsumers"];
        val vertexConsumer: VertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

        val d: Float = 0b1 / 32768f;
        val u1: Float = u1 + d;
        val u2: Float = u2 + d;
        val v1: Float = v1 - d;
        val v2: Float = v2 - d;

        vertexConsumer.vertex(matrix, x1, y1, 0f).texture(u1, v1).color(color);
        vertexConsumer.vertex(matrix, x1, y2, 0f).texture(u1, v2).color(color);
        vertexConsumer.vertex(matrix, x2, y2, 0f).texture(u2, v2).color(color);
        vertexConsumer.vertex(matrix, x2, y1, 0f).texture(u2, v1).color(color);
    }
}