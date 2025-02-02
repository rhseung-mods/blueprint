package com.rhseung.blueprint.color

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class Palette(val mainIndex: Int, val colors: List<ColorRGB>) {
    val mainColor: ColorRGB = colors[mainIndex];

    init {
        require(colors.size == SIZE) { "Palette must have exactly $SIZE colors" }
    }

    operator fun get(index: Int): ColorRGB {
        return colors[index];
    }

    fun indexOf(color: ColorRGB): Int {
        return colors.indexOf(color).takeIf { it >= 0 }
            ?: throw IllegalArgumentException("$color not found in palette $colors");
    }

    fun colorOf(baseColor: ColorRGB): ColorRGB {
        return colors[DEFAULT.indexOf(baseColor)];
    }

    fun toSet(): Set<ColorRGB> {
        return colors.toSet();
    }

    fun toList(): List<ColorRGB> {
        return colors.sortedWith { a, b -> a.compareTo(b) }.reversed();
    }

    companion object {
        fun fromColor(color: ColorRGB): Palette {
            return Palette(DEFAULT.indexOf(color), DEFAULT.colors);
        }

        val SIZE = (0..10).count();

        val CODEC: Codec<Palette> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("main_index").forGetter(Palette::mainIndex),
                Codec.list(ColorRGB.CODEC).fieldOf("colors").forGetter(Palette::colors)
            ).apply(instance, ::Palette)
        };

        val PACKET_CODEC: PacketCodec<ByteBuf, Palette> = PacketCodec.tuple(
            PacketCodecs.INTEGER, Palette::mainIndex,
            ColorRGB.PACKET_CODEC.collect(PacketCodecs.toList()), Palette::colors,
            ::Palette
        );

        val DEFAULT = Palette(
            3, listOf(
                ColorRGB(255, 255, 255),
                ColorRGB(229, 229, 229),
                ColorRGB(204, 204, 204),
                ColorRGB(178, 178, 178),
                ColorRGB(153, 153, 153),
                ColorRGB(127, 127, 127),
                ColorRGB(102, 102, 102),
                ColorRGB(76, 76, 76),
                ColorRGB(51, 51, 51),
                ColorRGB(25, 25, 25),
                ColorRGB(0, 0, 0)
            )
        );

        val DIAMOND = Palette(
            3, listOf(
                ColorRGB(255, 255, 255),
                ColorRGB(215, 249, 245),
                ColorRGB(164, 253, 240),
                ColorRGB(51, 235, 216),
                ColorRGB(43, 199, 181),
                ColorRGB(38, 160, 150),
                ColorRGB(30, 138, 130),
                ColorRGB(21, 99, 98),
                ColorRGB(14, 63, 63),
                ColorRGB(8, 37, 35),
                ColorRGB(0, 0, 0)
            )
        );
    }
}