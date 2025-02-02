package com.rhseung.blueprint.color

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Formatting
import kotlin.math.roundToInt

open class ColorRGB {
    val R: Int
    val r: Float
        get() = R.toFloat() / 255.0f;

    val G: Int
    val g: Float
        get() = G.toFloat() / 255.0f;

    val B: Int
    val b: Float
        get() = B.toFloat() / 255.0f;

    val H: Int
    val S: Float
    val V: Float

    constructor(R: Int, G: Int, B: Int) {
        this.R = R.coerceIn(0, 255)
        this.G = G.coerceIn(0, 255)
        this.B = B.coerceIn(0, 255)

        val r = this.R / 255.0F
        val g = this.G / 255.0F
        val b = this.B / 255.0F

        val max = kotlin.comparisons.maxOf(r, g, b)
        val min = kotlin.comparisons.minOf(r, g, b)

        this.H = ((when (max) {
            min -> 0.0F
            r -> (g - b) / (max - min)
            g -> 2 + (b - r) / (max - min)
            b -> 4 + (r - g) / (max - min)
            else -> 0.0F
        } * 60).roundToInt() + 360) % 360

        this.S = when (max) {
            0.0F -> 0.0F
            else -> (max - min) / max
        }

        this.V = max
    }

    constructor(H: Int, S: Float, V: Float) {
        this.H = H.coerceIn(0, 360)
        this.S = S.coerceIn(0.0F, 1.0F)
        this.V = V.coerceIn(0.0F, 1.0F)
        val max = (this.V * 255).roundToInt()
        val min = (max * (1 - this.S)).roundToInt()

        when (this.H) {
            in 300..<360 -> {
                this.R = max
                this.G = min
                this.B = (-((this.H - 360) / 60.0) * (max - min) + this.G).roundToInt()
            }

            in 0..<60 -> {
                this.R = max
                this.B = min
                this.G = ((this.H / 60.0) * (max - min) + this.B).roundToInt()
            }

            in 60..<120 -> {
                this.G = max
                this.B = min
                this.R = (-(this.H / 60.0 - 2) * (max - min) + this.B).roundToInt()
            }

            in 120..<180 -> {
                this.G = max
                this.R = min
                this.B = ((this.H / 60.0 - 2) * (max - min) + this.R).roundToInt()
            }

            in 180..<240 -> {
                this.B = max
                this.R = min
                this.G = (-(this.H / 60.0 - 4) * (max - min) + this.R).roundToInt()
            }

            in 240..<300 -> {
                this.B = max
                this.G = min
                this.R = ((this.H / 60.0 - 4) * (max - min) + this.G).roundToInt()
            }

            else -> error("impossible")
        }
    }

    constructor(rgb: Int) : this((rgb shr 16) and 0xFF, (rgb shr 8) and 0xFF, rgb and 0xFF);

    constructor(hex: String) : this(hex.substring(1).toInt(16));

    open operator fun component1() = R;
    open operator fun component2() = G;
    open operator fun component3() = B;
    operator fun compareTo(other: ColorRGB) = R.compareTo(other.R).takeIf { it != 0 }
        ?: G.compareTo(other.G).takeIf { it != 0 }
        ?: B.compareTo(other.B);

    fun r() = r;
    fun g() = g;
    fun b() = b;
    fun rgb() = (R shl 16) or (G shl 8) or B;
    fun argb(alpha: Int) = ColorARGB(alpha, this);

    fun zeroAlpha() = argb(0);
    fun fullAlpha() = argb(255);
    fun withAlpha(alpha: Int) = argb(alpha);

    fun toInt(alpha: Int = 255) = argb(alpha).toInt();

    override fun toString(): String {
        return "#06X".format(rgb());
    }

    override fun equals(other: Any?): Boolean {
        return other is ColorRGB && other.R == R && other.G == G && other.B == B;
    }

    override fun hashCode(): Int {
        var result = R;
        result = 31 * result + G;
        result = 31 * result + B;
        return result;
    }

    open fun darker(delta: Float): ColorRGB {
        return ColorRGB(H, S, V - delta);
    }

    open fun brighter(delta: Float): ColorRGB {
        return ColorRGB(H, S, V + delta);
    }

    companion object {
        val CODEC: Codec<ColorRGB> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("R").forGetter(ColorRGB::R),
                Codec.INT.fieldOf("G").forGetter(ColorRGB::G),
                Codec.INT.fieldOf("B").forGetter(ColorRGB::B)
            ).apply(instance, ::ColorRGB);
        };

        val PACKET_CODEC: PacketCodec<ByteBuf, ColorRGB> = PacketCodec.tuple(
            PacketCodecs.INTEGER, ColorRGB::R,
            PacketCodecs.INTEGER, ColorRGB::G,
            PacketCodecs.INTEGER, ColorRGB::B,
            ::ColorRGB
        );

        fun Pair<ColorRGB, ColorRGB>.gradient(ratioOfFirst: Float): ColorRGB {
            require(ratioOfFirst in 0f..1f) { "ratioOfFirst($ratioOfFirst) must be in range 0..1" };

            return ColorRGB(
                (this.first.H * ratioOfFirst + this.second.H * (1 - ratioOfFirst)).roundToInt(),
                this.first.S * ratioOfFirst + this.second.S * (1 - ratioOfFirst),
                this.first.V * ratioOfFirst + this.second.V * (1 - ratioOfFirst)
            );
        }

        fun List<ColorRGB>.gradient(ratios: List<Float>): ColorRGB {
            require(this.size == ratios.size) { "colors.size(${this.size}) must be equal to ratios.size(${ratios.size})" };
            require(ratios.all { it in 0f..1f }) { "ratios($ratios) must be in range 0..1" };
            require(ratios.sum() == 1f) { "ratios($ratios) must sum to 1" };

            var H = 0f;
            var S = 0f;
            var V = 0f;

            return this.zip(ratios).forEach { (color, ratio) ->
                H += color.H * ratio;
                S += color.S * ratio;
                V += color.V * ratio;
            }.let { ColorRGB(H.roundToInt(), S, V) };
        }

        fun Formatting.toColor() = ColorRGB(this.colorValue ?: 0);
    }
}
