package com.rhseung.blueprint.registration

import com.rhseung.blueprint.Blueprint
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

abstract class InitializeItem(
    open val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>?,
    settings: Settings
) : Item(settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id))) {

    constructor(id: Identifier, settings: Settings)
        : this(id, null, settings);

    open fun init() {}

    @Environment(EnvType.CLIENT)
    open fun initClient() {}

    init {
        Registry.register(Registries.ITEM, id, this);
        itemGroup?.let { ItemGroupEvents.modifyEntriesEvent(it).register { it.add(this) } }
    }
}