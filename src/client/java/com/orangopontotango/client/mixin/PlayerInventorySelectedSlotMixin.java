package com.orangopontotango.client.mixin;

import com.orangopontotango.client.ToolDrawSoundsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class PlayerInventorySelectedSlotMixin {

    @Shadow public abstract int getSelectedSlot();

    @Shadow @Final public Player player;

    @Inject(method = "setSelectedSlot(I)V", at = @At("HEAD"))
    private void toolDrawSounds$onSetSelectedSlot(int slot, CallbackInfo ci) {
        if (slot == this.getSelectedSlot()) return;

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || this.player != client.player) return;

        ItemStack newStack = this.player.getInventory().getItem(slot);
        ToolDrawSoundsClient.handleSlotChange(newStack);
    }
}
