package com.rhseung.blueprint

import com.rhseung.blueprint.test.TestLanguageProvider
import com.rhseung.blueprint.test.TestModelProvider
import com.rhseung.blueprint.test.TextureProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object BlueprintData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack();

        pack.addProvider(::TestLanguageProvider);
        pack.addProvider(::TestModelProvider);
        pack.addProvider(::TextureProvider);
    }
}