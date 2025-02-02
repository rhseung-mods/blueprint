package com.rhseung.blueprint.test

import com.rhseung.blueprint.color.ColorRGB
import com.rhseung.blueprint.color.Colors
import com.rhseung.blueprint.color.Palette
import com.rhseung.blueprint.render.TextureImage
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class TextureProvider(output: FabricDataOutput) : DataProvider {
    val dataOutput = FabricDataOutput(
        output.modContainer,
        output.path.parent.resolve("resources"),
        output.isStrictValidationEnabled
    );

    val texturesPathResolver: DataOutput.PathResolver = dataOutput
        .getResolver(DataOutput.OutputType.RESOURCE_PACK, "textures");

    override fun getName(): String {
        return "Texture Provider";
    }

    override fun run(writer: DataWriter): CompletableFuture<*> {
        val basePath: Path = texturesPathResolver.resolve(TestItems.TEST_PICKAXE.modelId, "png");
        val image = TextureImage.open(basePath);
        val palette = Palette.DEFAULT;

        return CompletableFuture.runAsync {
            TestItems.TEST_PICKAXE.textureMap.values.forEachIndexed { index, id ->
                val img = TextureImage(image.width, image.height);
                val targetColor: ColorRGB = palette[index];
                val positions: List<Pair<Int, Int>> = image.getPositions(targetColor);
                positions.forEach { (x, y) -> img[x, y] = Colors.WHITE; }

                val texturePath = texturesPathResolver.resolve(id, "png");
                img.save(texturePath);
            }
        };
    }
}