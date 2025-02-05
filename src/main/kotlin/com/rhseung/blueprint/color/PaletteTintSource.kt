package com.rhseung.blueprint.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.rhseung.blueprint.Blueprint
import net.minecraft.client.render.item.tint.TintSource
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

class PaletteTintSource(val index: Int) : TintSource {
    init {
        require(index in 0..<Palette.SIZE) { "Palette index($index) must be in range 0..<${Palette.SIZE}" };
    }

    companion object {
        val MAP_CODEC: MapCodec<out PaletteTintSource> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT.fieldOf("index").forGetter(PaletteTintSource::index)
            ).apply(instance, ::PaletteTintSource);
        };
    }

    override fun getTint(
        stack: ItemStack,
        world: ClientWorld?,
        user: LivingEntity?
    ): Int {
        val palette = stack.getOrDefault(Blueprint.PALETTE_COMPONENT, Palette.DEFAULT);
        return palette[index].fullAlpha().toInt();
    }

    override fun getCodec(): MapCodec<out TintSource> {
        return MAP_CODEC;
    }
}