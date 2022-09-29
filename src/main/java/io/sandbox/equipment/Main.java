package io.sandbox.equipment;

import net.fabricmc.api.ModInitializer;
import io.sandbox.equipment.configTypes.ThornsConfig;
import io.sandbox.lib.Config;
import io.sandbox.lib.SandboxLogger;

public class Main implements ModInitializer {
	private static final SandboxLogger LOGGER = new SandboxLogger("SandboxEquipment");

	private static Config<ThornsConfig> thornsConfig = new Config<ThornsConfig>(ThornsConfig.class, "SandboxMC/equipment/thornsConfig.json");

	@Override
	public void onInitialize() {
		LOGGER.info("The following equipment modifications were loaded:");

		if (getThornsConfig().enabled) {
			LOGGER.info("Cooler thorns enchantment");
		}
	}

	public static ThornsConfig getThornsConfig() {
		return thornsConfig.getConfig();
	}
}
