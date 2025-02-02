package com.rhseung.blueprint.test

import com.rhseung.blueprint.color.Colors
import com.rhseung.blueprint.lang.TranslatableText
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class TestLanguageProvider(
    output: FabricDataOutput,
    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricLanguageProvider(output, registryLookup) {

    companion object {
        val PALETTE_TEXT = TranslatableText("palette")
            .add("Palette: ", Colors.GRAY);
    }

    override fun generateTranslations(lookUp: RegistryWrapper.WrapperLookup, translationBuilder: TranslationBuilder) {
        TranslatableText.VALUES.forEach { text ->
            translationBuilder.add(text.key, text.value);
        };

        translationBuilder.add(TestItems.TEST_PICKAXE, "Test Pickaxe");
    }
}