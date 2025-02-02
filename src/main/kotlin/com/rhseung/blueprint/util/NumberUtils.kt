package com.rhseung.blueprint.util

import com.ibm.icu.text.DecimalFormat
import kotlin.math.abs

object NumberUtils {
    fun Float.roundTo(n: Int): Float {
        return "%.${n}f".format(this).toFloat();
    }

    fun Double.roundTo(n: Int): Double {
        return "%.${n}f".format(this).toDouble();
    }

    fun Float.toStringPretty(): String {
        return DecimalFormat("#.##").format(this);
    }

    fun Double.toStringPretty(): String {
        return DecimalFormat("#.##").format(this);
    }

    fun IntProgression.size(): Int {
        return abs((this.last - this.first) / this.step) + 1;
    }

    infix fun Int.rangeWith(size: Int): IntRange {
        return this..<(this + size);
    }

    fun Boolean.toInt(): Int {
        return if (this) 1 else 0;
    }
}