package com.shieldbreaker;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("shieldbreaker");

	@Override
	public void onInitialize() {
		// Hier muss nichts rein, aber die Klasse MUSS existieren
		LOGGER.info("ShieldBreaker Common Initialized (Access Widener active)");
	}
}