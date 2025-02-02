package com.rhseung.blueprint.registration

import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.color.ColorARGB
import com.rhseung.blueprint.color.Colors
import com.rhseung.blueprint.color.Palette
import com.rhseung.blueprint.datagen.BlueprintTextureProvider
import com.rhseung.blueprint.render.TextureImage
import com.rhseung.blueprint.util.CollectionUtils.toTextureMap
import com.rhseung.blueprint.util.ReflectionUtils.getProperty
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.DataOutput
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
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
class PaletteTintItem(
    id: Identifier,
    val modelId: Identifier,
    itemGroup: RegistryKey<ItemGroup>?,
    settings: Settings,
) : InitializeItem(id, itemGroup, settings.component(Blueprint.PALETTE_COMPONENT, Palette.DEFAULT)) {

    constructor(id: Identifier, itemGroup: RegistryKey<ItemGroup>, settings: Settings): this(id, id.withPrefixedPath("item/"), itemGroup, settings);

    constructor(id: Identifier, settings: Settings): this(id, id.withPrefixedPath("item/"), null, settings);

    override fun init() {}

    override fun initClient() {
        ColorProviderRegistry.ITEM.register({ stack, tintIndex ->
            getPalette(stack)[tintIndex].toInt()
        }, this);
    }

    fun getPalette(stack: ItemStack): Palette {
        return stack.getOrDefault(Blueprint.PALETTE_COMPONENT, Palette.DEFAULT);
    }

    private val textureMap: Map<TextureKey, Identifier> = (0..<Palette.SIZE).associate {
        TextureKey.of("layer$it") to modelId.withSuffixedPath("/$it")
    };

    fun generateModels(parent: Model, itemModel: ItemModelGenerator) {
        val parent = parent.getProperty<Optional<Identifier>>("parent").get();

        val model = Model(
            Optional.of(parent.withPrefixedPath("item/")),
            Optional.empty(),
            *textureMap.keys.toTypedArray()
        );

        model.upload(modelId, textureMap.toTextureMap(), itemModel.writer);
    }

    fun generateTextures(texturesPathResolver: DataOutput.PathResolver) {
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