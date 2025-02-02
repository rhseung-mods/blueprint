package com.rhseung.blueprint

import com.rhseung.blueprint.test.TestItems
import net.fabricmc.api.ClientModInitializer

object BlueprintClient : ClientModInitializer {
    override fun onInitializeClient() {
        TestItems.initializeClient();
    }
}