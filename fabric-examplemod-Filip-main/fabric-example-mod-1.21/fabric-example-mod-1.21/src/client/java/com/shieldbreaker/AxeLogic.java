package com.shieldbreaker;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Random;

public class AxeLogic {
    private static boolean isBusy = false;
    private static long lastExecutionTime = 0;
    private static final Random random = new Random();

    public static void checkAndExecute(Minecraft client) {
        if (client.player == null || client.level == null || !AxeConfig.isEnabled || isBusy) return;

        // Cooldown: Nur alle 500ms eine Aktion ausführen
        if (System.currentTimeMillis() - lastExecutionTime < 500) return;

        // Raycast-Check: Schaust du den Gegner an?
        HitResult hit = client.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) return;

        if (((EntityHitResult) hit).getEntity() instanceof Player enemy) {
            if (enemy.isUsingItem() && enemy.getUseItem().is(Items.SHIELD) && client.player.distanceTo(enemy) < 4.0) {
                isBusy = true;

                // Menschliche Verzögerung in einem neuen Thread
                new Thread(() -> {
                    try {
                        Thread.sleep(120 + random.nextInt(130));
                        client.execute(() -> executeSafeBreak(client, enemy));
                    } catch (Exception e) {
                        isBusy = false;
                    }
                }).start();
            }

        }
    }

    private static void executeSafeBreak(Minecraft client, Player enemy) {
        try {
            // Axt in der Hotbar suchen
            int axeSlot = -1;
            for (int i = 0; i < 9; i++) {
                if (client.player.getInventory().getItem(i).getItem() instanceof AxeItem) {
                    axeSlot = i;
                    break;
                }
            }

            if (axeSlot != -1) {

                int originalSlot = client.player.getInventory().getSelectedSlot();


                // 1. Wechsel zur Axt (Paket an Server)
                client.player.connection.send(new ServerboundSetCarriedItemPacket(axeSlot));

                // 2. Der Schlag
                client.gameMode.attack(client.player, enemy);
                client.player.swing(InteractionHand.MAIN_HAND);

                // Kurze Pause (50-100ms), damit der Server den Slot-Wechsel registriert
                Thread.sleep(50 + random.nextInt(50));

                // 3. Zurück zum alten Slot (Paket an Server)
                client.player.connection.send(new ServerboundSetCarriedItemPacket(originalSlot));
                //IMPORTANT !!!!
                lastExecutionTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isBusy = false;
        }
    }
}