package com.rhseung.blueprint.render

import com.rhseung.blueprint.color.ColorARGB
import com.rhseung.blueprint.color.ColorRGB
import com.rhseung.blueprint.color.Colors
import com.rhseung.blueprint.color.Palette
import kotlinx.io.files.FileNotFoundException
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.atlas.AtlasSource.RESOURCE_FINDER
import net.minecraft.resource.InputSupplier
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.exists

data class TextureImage(val image: NativeImage) {
    val width: Int
        get() = image.width;

    val height: Int
        get() = image.height;

    constructor(width: Int, height: Int, fill: ColorARGB) : this(NativeImage(width, height, false)) {
        forEach { x, y ->
            this[x, y] = fill;
        }
    };

    constructor(width: Int, height: Int) : this(width, height, Colors.EMPTY);

    operator fun get(x: Int, y: Int): ColorARGB {
        return ColorARGB(image.getColorArgb(x, y));
    }

    operator fun set(x: Int, y: Int, color: ColorARGB) {
        image.setColorArgb(x, y, color.toInt());
    }

    fun copy(): TextureImage {
        val newImage = NativeImage(width, height, false);
        newImage.copyFrom(image);

        return TextureImage(newImage);
    }

    fun forEach(action: (Int, Int) -> Unit) {
        for (x in 0..<width) {
            for (y in 0..<height) {
                action(x, y);
            }
        }
    }

    fun getColors(): Set<ColorARGB> {
        val colors = mutableSetOf<ColorARGB>();

        forEach { x, y ->
            colors.add(this[x, y]);
        }

        return colors;
    }

    fun getColorsCount(): Map<ColorARGB, Int> {
        val colors = mutableMapOf<ColorARGB, Int>().withDefault { 0 };

        forEach { x, y ->
            colors[this[x, y]] = colors.getValue(this[x, y]) + 1;
        }

        return colors;
    }

    fun getPositions(color: ColorARGB): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>();

        forEach { x, y ->
            if (this[x, y].A > 0 && this[x, y] == color) {
                positions.add(x to y);
            }
        }

        return positions;
    }

    fun getPositions(color: ColorRGB): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>();

        forEach { x, y ->
            if (this[x, y].A > 0 && this[x, y].toRGB() == color) {
                positions.add(x to y);
            }
        }

        return positions;
    }

    fun save(path: Path) {
        if (!path.exists())
            path.parent.toFile().mkdirs();

        image.writeTo(path);
    }

    companion object {
        fun open(path: Path): TextureImage {
            val inputStream: InputStream = InputSupplier.create(path).get();
            return TextureImage(NativeImage.read(inputStream));
        }
    }
}