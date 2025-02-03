package com.rhseung.blueprint.mixin.tooltip;

import com.rhseung.blueprint.color.ColorRGB;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

@Mixin(Formatting.class)
public class FormattingMixin {
    @Final
    @Shadow
    @Mutable
    @Nullable
    private Integer colorValue;

    @Final
    @Shadow
    public static Formatting RED;

    @Unique
    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    @Unique
    private static void setColorValue(Formatting formatting, int colorValue) {
        ((FormattingMixin) (Object) formatting).setColorValue(colorValue);
    }

    static {
//        setColorValue(Formatting.BLACK, new ColorRGB(0.0f, 0.0f, 0.0f).toInt());
//        setColorValue(Formatting.DARK_BLUE, new ColorRGB(0.2f, 0.29f, 0.93f).toInt());
//        setColorValue(Formatting.DARK_GREEN, new ColorRGB(0.05f, 0.67f, 0.22f).toInt());
//        setColorValue(Formatting.DARK_AQUA, new ColorRGB(0.0f, 0.71f, 0.70f).toInt());
//        setColorValue(Formatting.DARK_RED, new ColorRGB(0.81f, 0.08f, 0.23f).toInt());
//        setColorValue(Formatting.DARK_PURPLE, new ColorRGB(0.56f, 0.11f, 0.96f).toInt());
//        setColorValue(Formatting.GOLD, new ColorRGB(0.56f, 0.11f, 0.96f).toInt());
//        setColorValue(Formatting.GRAY, new ColorRGB(0.65f, 0.67f, 0.70f).toInt());
//        setColorValue(Formatting.DARK_GRAY, new ColorRGB(0.46f, 0.46f, 0.5f).toInt());
//        setColorValue(Formatting.BLUE, new ColorRGB(0.09f, 0.63f, 1f).toInt());
//        setColorValue(Formatting.GREEN, new ColorRGB(0.46f, 0.92f, 0.2f).toInt());
//        setColorValue(Formatting.AQUA, new ColorRGB(0.21f, 0.92f, 0.99f).toInt());
//        setColorValue(Formatting.RED, new ColorRGB(0.99f, 0.32f, 0.19f).toInt());
//        setColorValue(Formatting.LIGHT_PURPLE, new ColorRGB(0.76f, 0.18f, 0.93f).toInt());
//        setColorValue(Formatting.YELLOW, new ColorRGB(1f, 0.95f, 0.15f).toInt());
//        setColorValue(Formatting.WHITE, new ColorRGB(1f, 1f, 1f).toInt());
    }
}
