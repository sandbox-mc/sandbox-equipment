package io.sandbox.equipment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sandbox.equipment.enchantments.EnchantLoader;
import io.sandbox.equipment.items.ItemLoader;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {
	public static final String MOD_ID = "equipment";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("equipment");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ItemLoader.init();
		EnchantLoader.init();

		LOGGER.info("Sandbox Equipment loaded");
	}

	public static Identifier id (String name) {
		return new Identifier(MOD_ID, name);
	}
}
