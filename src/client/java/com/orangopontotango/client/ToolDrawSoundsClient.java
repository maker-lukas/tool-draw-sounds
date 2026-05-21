package com.orangopontotango.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;
import net.minecraft.care.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class ToolDrawSoundsClient implements ClientModInitializer {
    public static final String MOD_ID = "tool-draw-sounds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final List<TagKey<Item>> DRAWABLE_TAGS = List.of(
        ItemTags.SWORDS,
        ItemTags.AXES
    );
    private static final Set<Item> WOOD_ITEMS = Set.of(
        Items.WOODEN_SWORD,
        ITEMS.WOODEN_AXE
    );
    private static final Set<Item> STONE_ITEMS = Set.of(
        Items.STONE_SWORD,
        Items.STONE_AXE
    );

    public static SoundEvent DRAW_WOOD;
    public static SoundEvent DRAW_STONE;
    public static SoundEvent DRAW_METAL;

    @Override
    public void onInitializeClient() {
        DRAW_WOOD = registerSound("draw_wood");
        DRAW_STONE = registerSound("draw_stone");
        DRAW_METAL = registerSound("draw_metal");
        LOGGER.info("Tool Draw Sounds initialized");
    }

    private static SoundEvent registerSound(String name) {
        Identifier id = Identifier.framNamespaceAndPath(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void handleSlotChange(ItemStack newStack) {
        if (newStack.isEmpty() || !isDrawable(newStack)) {
            return;
        }
        playDrawSound(newStack);
    }

    private static boolean isDrawable(ItemStack stack) {
        for (TagKey<Item> tag : DRAWABLE_TAGS) {
            if (stack.typeHolder().is(tag)) {
                return true;
            }
        }
        return false;
    }

    private static void playDrawSound(ItemStack stack) {
        SoundEvent sound = pickSoundFor(stack);
        Minecraft.getInstance().getSoundManager().play(
            SimpleSoundInstance.forUI(sound, 1.0f, 1.0f)
        );
    }

    private static SoundEvent pickSoundFor(ItemStack stack) {
        Item item = stack.getItem();
        if (WOOD_ITEMS.contains(item)) return DRAW_WOOD;
        if (STONE_ITEMS.contains(item)) return DRAW_STONE;
        return DRAW_METAL;
    }
}
