package com.rhseung.blueprint.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.registration.DynamicTintItem
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
        val fallback: Palette? = (stack.item as? DynamicTintItem)?.defaultPalette;
        val palette: Palette? = stack.get(Blueprint.PALETTE_COMPONENT) ?: fallback;

        // todo: 텍스쳐가 alpha 값이 있어도 불투명하게 표시되는 문제
        return if (palette != null)
            palette[index].fullAlpha().toInt();
        else
            -1;
    }

    override fun getCodec(): MapCodec<out TintSource> {
        return MAP_CODEC;
    }
}