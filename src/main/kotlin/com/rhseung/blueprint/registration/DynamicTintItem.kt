package com.rhseung.blueprint.registration

import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.color.ColorARGB
import com.rhseung.blueprint.color.Colors
import com.rhseung.blueprint.color.Palette
import com.rhseung.blueprint.color.PaletteTintSource
import com.rhseung.blueprint.datagen.BlueprintTextureProvider
import com.rhseung.blueprint.render.TextureImage
import com.rhseung.blueprint.util.CollectionUtils.toTextureMap
import net.minecraft.client.data.ItemModelGenerator
import net.minecraft.client.data.ItemModels
import net.minecraft.client.data.Model
import net.minecraft.client.data.TextureKey
import net.minecraft.client.render.item.model.BasicItemModel
import net.minecraft.client.render.item.model.ItemModel
import net.minecraft.data.DataOutput
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import java.util.*

/**
 * @param modelId must start with "item/"
 *
 * @see init for initialization [net.fabricmc.api.ModInitializer]
 * @see initClient for client initialization [net.fabricmc.api.ClientModInitializer]
 * @see generateModels for model generation [FabricModelProvider]
 * @see generateTextures for texture generation [BlueprintTextureProvider]
 */
open class DynamicTintItem(
    id: Identifier,
    open val modelId: Identifier,
    itemGroup: RegistryKey<ItemGroup>?,
    settings: Settings,
    open val defaultPalette: Palette = Palette.DEFAULT
) : InitializeItem(id, itemGroup, settings.component(Blueprint.PALETTE_COMPONENT, defaultPalette)) {

    constructor(id: Identifier, itemGroup: RegistryKey<ItemGroup>?, settings: Settings, defaultPalette: Palette = Palette.DEFAULT): this(id, id.withPrefixedPath("item/"), itemGroup, settings, defaultPalette);

    constructor(id: Identifier, settings: Settings, defaultPalette: Palette = Palette.DEFAULT): this(id, id.withPrefixedPath("item/"), null, settings, defaultPalette);

    override fun init() {}

    override fun initClient() {}

    open fun getPalette(stack: ItemStack): Palette? {
        return stack.get(Blueprint.PALETTE_COMPONENT);
    }

    open fun getPaletteOrDefault(stack: ItemStack): Palette {
        return getPalette(stack) ?: defaultPalette;
    }

    open fun setPalette(stack: ItemStack, palette: Palette) {
        stack.set(Blueprint.PALETTE_COMPONENT, palette);
    }

    protected open val textureMap: Map<TextureKey, Identifier> = (0..<Palette.SIZE).associate {
        TextureKey.of("layer$it") to modelId.withSuffixedPath("/$it")
    };

    open fun generateModels(parent: String, itemModel: ItemModelGenerator): BasicItemModel.Unbaked {
        val parent = Model(
            Optional.of(Identifier.ofVanilla("item/$parent")),
            Optional.empty(),
            *textureMap.keys.toTypedArray()
        );

        val unbaked: BasicItemModel.Unbaked = ItemModels.tinted(
            parent.upload(
                modelId,
                textureMap.toTextureMap(),
                itemModel.modelCollector
            ),
            *(0..<Palette.SIZE).map(::PaletteTintSource).toTypedArray()
        ) as BasicItemModel.Unbaked;

        itemModel.output.accept(this, unbaked);

        return unbaked;
    }

    open fun generateTextures(texturesPathResolver: DataOutput.PathResolver) {
        val baseImage = BlueprintTextureProvider.getImage(texturesPathResolver, modelId);

        require(Palette.DEFAULT.toSet().containsAll(baseImage.getColors().map(ColorARGB::toRGB))) {
            "Palette colors must contain all base image colors(${Palette.DEFAULT})";
        };

        textureMap.values.forEachIndexed { index, id ->
            val image = TextureImage(baseImage.width, baseImage.height);
            val targetColor = Palette.DEFAULT[index];

            val positions = baseImage.getPositions(targetColor);
            positions.forEach { (x, y) -> image[x, y] = Colors.WHITE; }

            BlueprintTextureProvider.saveImage(texturesPathResolver, image, id);
        }
    }
}