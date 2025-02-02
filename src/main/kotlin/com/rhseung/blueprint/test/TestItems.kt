package com.rhseung.blueprint.test

import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.registration.IModInit
import com.rhseung.blueprint.registration.PaletteTintItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups

object TestItems : IModInit {
    override fun initialize() {
        TEST_PICKAXE.init();
    }

    override fun initializeClient() {
        TEST_PICKAXE.initClient();
    }

    val TEST_PICKAXE = PaletteTintItem(
        Blueprint.id("test_pickaxe"),
        ItemGroups.TOOLS,
        Item.Settings(),
    );
}