package io.sandbox.equipment;

import net.fabricmc.api.ModInitializer;

import io.sandbox.equipment.configTypes.ThornsConfig;

import io.sandbox.equipment.enchantments.EnchantLoader;
import io.sandbox.equipment.items.ItemLoader;
import io.sandbox.lib.Config;
import io.sandbox.lib.SandboxLogger;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {
	public static final String modId = "sandbox-equipment";
	private static final SandboxLogger LOGGER = new SandboxLogger("SandboxEquipment");

	private static Config<ThornsConfig> thornsConfig = new Config<ThornsConfig>(ThornsConfig.class, "SandboxMC/equipment/thornsConfig.json");

	@Override
	public void onInitialize() {
		LOGGER.info("The following equipment modifications were loaded:");

		if (getThornsConfig().enabled) {
			LOGGER.info("Cooler thorns enchantment");
		}

		ItemLoader.init();
		EnchantLoader.init();
	}

	public static ThornsConfig getThornsConfig() {
		return thornsConfig.getConfig();
	}

	public static Identifier id (String name) {
		return new Identifier(modId, name);
	}
}
