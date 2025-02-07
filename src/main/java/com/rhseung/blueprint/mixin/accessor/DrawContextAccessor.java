package com.rhseung.blueprint.mixin.accessor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.GuiAtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
    @Accessor
    GuiAtlasManager getGuiAtlasManager();

    @Accessor
    VertexConsumerProvider.Immediate getVertexConsumers();
}
