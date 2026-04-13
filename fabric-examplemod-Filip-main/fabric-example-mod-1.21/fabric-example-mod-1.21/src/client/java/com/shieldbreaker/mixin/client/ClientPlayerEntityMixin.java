package com.shieldbreaker.mixin.client;

import com.shieldbreaker.AxeLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // In Official Mappings heißt die Hauptklasse "Minecraft" statt "MinecraftClient"
        AxeLogic.checkAndExecute(Minecraft.getInstance());
    }
}