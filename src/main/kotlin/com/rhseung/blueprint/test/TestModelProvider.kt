package com.rhseung.blueprint.test

import com.rhseung.blueprint.util.CollectionUtils.toTextureMap
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Model
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.TextureKey
import net.minecraft.util.Identifier
import java.util.*

class TestModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockModel: BlockStateModelGenerator) {
    }

    override fun generateItemModels(itemModel: ItemModelGenerator) {
        val model = item("handheld", *TestItems.TEST_PICKAXE.textureMap.keys.toTypedArray());

        model.upload(
            ModelIds.getItemModelId(TestItems.TEST_PICKAXE),
            TestItems.TEST_PICKAXE.textureMap.toTextureMap(),
            itemModel.writer
        );
    }

    private fun item(parent: String, vararg requiredTextureKeys: TextureKey): Model {
        return Model(
            Optional.of(Identifier.ofVanilla("item/$parent")),
            Optional.empty(),
            *requiredTextureKeys
        )
    }
}