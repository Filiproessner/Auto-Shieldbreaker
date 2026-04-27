package com.shieldbreaker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class AxeBot implements ClientModInitializer {
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.shieldbreaker.toggle", // Name in den Optionen
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KeyMapping.Category.MISC
        ));

        // jeden Tick prüfen, ob die Taste gedrückt wurde
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (toggleKey.consumeClick()) {
                // Toggle den Status
                AxeConfig.isEnabled = !AxeConfig.isEnabled;

                // Message to player
                String status = AxeConfig.isEnabled ? "§aENABLED" : "§cDISABLED";
                client.player.displayClientMessage(
                        Component.literal("§8[§6ShieldBreaker§8] " + status),
                        true
                );
            }

            //run if enabled
            if (AxeConfig.isEnabled) {
                AxeLogic.checkAndExecute(client);
            }
        });
    }
}