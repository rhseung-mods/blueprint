package com.rhseung.blueprint.color

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import kotlin.math.roundToInt

class ColorARGB : ColorRGB {
    val A: Int;
    val a: Float
        get() = A.toFloat() / 255.0f;

    constructor(A: Int, R: Int, G: Int, B: Int) : super(R, G, B) {
        this.A = A.coerceIn(0, 255);
    }

    constructor(A: Int, H: Int, S: Float, V: Float) : super(H, S, V) {
        this.A = A.coerceIn(0, 255);
    }

    constructor(argb: Int) : super((argb shr 16) and 0xFF, (argb shr 8) and 0xFF, argb and 0xFF) {
        this.A = (argb shr 24) and 0xFF;
    }

    constructor(alpha: Int, rgb: ColorRGB) : this(alpha, rgb.R, rgb.G, rgb.B);

    override operator fun component1() = A;
    override operator fun component2() = R;
    override operator fun component3() = G;
    operator fun component4() = B;
    operator fun compareTo(other: ColorARGB) = R.compareTo(other.R).takeIf { it != 0 }
        ?: G.compareTo(other.G).takeIf { it != 0 }
        ?: B.compareTo(other.B).takeIf { it != 0 }
        ?: A.compareTo(other.A);

    fun a() = a;
    fun argb() = (A shl 24) or rgb();

    override fun toInt() = argb();
    fun toRGB() = ColorRGB(R, G, B);

    override fun toString(): String {
        return "#08X".format(argb());
    }

    override fun equals(other: Any?): Boolean {
        return other is ColorARGB && other.A == A && super.equals(other);
    }

    override fun hashCode(): Int {
        var result = A.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }

    override fun darker(delta: Float): ColorARGB {
        return ColorARGB(A, H, S, V - delta);
    }

    override fun brighter(delta: Float): ColorARGB {
        return ColorARGB(A, H, S, V + delta);
    }

    companion object {
        val CODEC: Codec<ColorARGB> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("A").forGetter(ColorARGB::A),
                Codec.INT.fieldOf("R").forGetter(ColorRGB::R),
                Codec.INT.fieldOf("G").forGetter(ColorRGB::G),
                Codec.INT.fieldOf("B").forGetter(ColorRGB::B)
            ).apply(instance, ::ColorARGB);
        };

        val PACKET_CODEC: PacketCodec<ByteBuf, ColorARGB> = PacketCodec.tuple(
            PacketCodecs.INTEGER, ColorARGB::A,
            PacketCodecs.INTEGER, ColorRGB::R,
            PacketCodecs.INTEGER, ColorRGB::G,
            PacketCodecs.INTEGER, ColorRGB::B,
            ::ColorARGB
        );

        fun Pair<ColorARGB, ColorARGB>.gradient(ratioOfFirst: Float): ColorARGB {
            require(ratioOfFirst in 0f..1f) { "ratioOfFirst($ratioOfFirst) must be in range 0..1" };

            return ColorARGB(
                (this.first.A * ratioOfFirst + this.second.A * (1 - ratioOfFirst)).roundToInt(),
                (this.first.H * ratioOfFirst + this.second.H * (1 - ratioOfFirst)).roundToInt(),
                this.first.S * ratioOfFirst + this.second.S * (1 - ratioOfFirst),
                this.first.V * ratioOfFirst + this.second.V * (1 - ratioOfFirst),
            );
        }

        fun List<ColorARGB>.gradient(ratios: List<Float>): ColorARGB {
            require(this.size == ratios.size) { "colors.size(${this.size}) must be equal to ratios.size(${ratios.size})" };
            require(ratios.all { it in 0f..1f }) { "ratios($ratios) must be in range 0..1" };
            require(ratios.sum() == 1f) { "ratios($ratios) must sum to 1" };

            var A = 0f;
            var H = 0f;
            var S = 0f;
            var V = 0f;

            return this.zip(ratios).forEach { (color, ratio) ->
                A += color.A * ratio;
                H += color.H * ratio;
                S += color.S * ratio;
                V += color.V * ratio;
            }.let { ColorARGB(A.roundToInt(), H.roundToInt(), S, V) }
        }
    }
}