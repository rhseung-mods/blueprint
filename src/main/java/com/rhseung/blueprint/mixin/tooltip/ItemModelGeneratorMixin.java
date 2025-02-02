package com.rhseung.blueprint.mixin.tooltip;

import com.rhseung.blueprint.color.Palette;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.stream.IntStream;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    @Mutable @Shadow @Final public static List<String> LAYERS;

    static {
        LAYERS = IntStream.range(0, Palette.Companion.getSIZE()).mapToObj(i -> "layer" + i).toList();
    }
}
