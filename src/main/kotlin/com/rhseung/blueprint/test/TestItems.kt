package com.rhseung.blueprint.test

import com.rhseung.blueprint.Blueprint
import com.rhseung.blueprint.color.Palette
import com.rhseung.blueprint.registration.DynamicTintItem
import com.rhseung.blueprint.registration.IModInit
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

object TestItems : IModInit {
    val PICKAXE_HEAD = object : DynamicTintItem(Blueprint.id("pickaxe_head"), ItemGroups.TOOLS, Item.Settings()) {
        override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
            val stack = user.getStackInHand(hand);

            if (getPalette(stack) == Palette.DIAMOND)
                setPalette(stack, Palette.DEFAULT);
            else
                setPalette(stack, Palette.DIAMOND);

            return ActionResult.SUCCESS;
        }
    };

    override fun initialize() {
        PICKAXE_HEAD.init();
    }

    override fun initializeClient() {
        PICKAXE_HEAD.initClient();
    }
}