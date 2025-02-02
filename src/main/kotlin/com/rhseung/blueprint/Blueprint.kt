package com.rhseung.blueprint

import com.rhseung.blueprint.color.Palette
import net.fabricmc.api.ModInitializer
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Blueprint : ModInitializer {
    const val MOD_ID = "blueprint";
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID);

    fun id(path: String): Identifier = Identifier.of(MOD_ID, path);

    val PALETTE_COMPONENT: ComponentType<Palette> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        id("palette"),
        ComponentType.builder<Palette>()
            .codec(Palette.CODEC)
            .packetCodec(Palette.PACKET_CODEC)
            .build()
    );

    override fun onInitialize() {}
}