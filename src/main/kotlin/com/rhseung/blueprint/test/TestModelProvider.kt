package com.rhseung.blueprint.test

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator
import net.minecraft.client.data.Models

class TestModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockModel: BlockStateModelGenerator) {
    }

    override fun generateItemModels(itemModel: ItemModelGenerator) {
        TestItems.PICKAXE_HEAD.generateModels("handheld", itemModel);
    }
}