package com.rhseung.blueprint.datagen

import com.rhseung.blueprint.render.TextureImage
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

abstract class BlueprintTextureProvider(output: FabricDataOutput) : DataProvider {
    val dataOutput: FabricDataOutput = FabricDataOutput(
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
        return CompletableFuture.runAsync { generateTextures() };
    }

    fun getPath(id: Identifier): Path {
        return texturesPathResolver.resolve(id, "png");
    }

    fun getImage(id: Identifier): TextureImage {
        return TextureImage.open(getPath(id));
    }

    fun saveImage(image: TextureImage, id: Identifier) {
        image.save(getPath(id));
    }

    companion object {
        fun getPath(texturesPathResolver: DataOutput.PathResolver, id: Identifier): Path {
            return texturesPathResolver.resolve(id, "png");
        }

        fun getImage(texturesPathResolver: DataOutput.PathResolver, id: Identifier): TextureImage {
            return TextureImage.open(getPath(texturesPathResolver, id));
        }

        fun saveImage(texturesPathResolver: DataOutput.PathResolver, image: TextureImage, id: Identifier) {
            image.save(getPath(texturesPathResolver, id));
        }
    }

    abstract fun generateTextures();
}