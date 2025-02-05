package com.rhseung.blueprint.test

import com.rhseung.blueprint.datagen.BlueprintTextureProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class TestTextureProvider(output: FabricDataOutput) : BlueprintTextureProvider(output) {
    override fun generateTextures() {
        TestItems.PICKAXE_HEAD.generateTextures(this.texturesPathResolver);
    }
}