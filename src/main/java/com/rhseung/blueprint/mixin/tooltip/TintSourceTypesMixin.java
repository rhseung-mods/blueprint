package com.rhseung.blueprint.mixin.tooltip;

import com.mojang.serialization.MapCodec;
import com.rhseung.blueprint.Blueprint;
import com.rhseung.blueprint.color.PaletteTintSource;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TintSourceTypes.class)
public class TintSourceTypesMixin {
    @Shadow @Final public static Codecs.IdMapper<Identifier, MapCodec<? extends TintSource>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrapMixin(CallbackInfo ci) {
        ID_MAPPER.put(Blueprint.INSTANCE.id("palette"), PaletteTintSource.Companion.getMAP_CODEC());
    }
}
