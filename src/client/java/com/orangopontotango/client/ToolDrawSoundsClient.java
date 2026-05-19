package com.orangopontotango.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ToolDrawSoundsClient implements ClientModInitializer {
    public static final String MOD_ID = "tool-draw-sounds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final List<TagKey<Item>> DRAWABLE_TAGS = List.of(
        ItemTags.SWORDS,
        ItemTags.AXES
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Tool Draw Sounds initialized");
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
        // forUI = no world position, no falloff, purely local UI-style sound.
        Minecraft.getInstance().getSoundManager().play(
            SimpleSoundInstance.forUI(sound, 0.8f, 1.0f)
        );
    }

    private static SoundEvent pickSoundFor(ItemStack stack) {
        if (stack.typeHolder().is(ItemTags.AXES)) {
            return SoundEvents.AXE_STRIP;
        }
        return SoundEvents.ARMOR_EQUIP_IRON.value();
    }
}
