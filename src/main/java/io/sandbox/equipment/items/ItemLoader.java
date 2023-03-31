package io.sandbox.equipment.items;

import io.sandbox.equipment.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ItemLoader {
  public static Item ENHANCEMENT_ORB = new StoneOfEnhancementItem(
    (new Settings()).maxCount(64).fireproof().rarity(Rarity.UNCOMMON),
    1
  );
  public static Item EMPOWERED_ORB = new StoneOfEnhancementItem(
    (new Settings()).maxCount(64).fireproof().rarity(Rarity.RARE),
    2
  );

  public static void init() {
    Registry.register(Registries.ITEM, Main.id("enhancement_orb"), ENHANCEMENT_ORB);
    Registry.register(Registries.ITEM, Main.id("empowered_orb"), EMPOWERED_ORB);

    ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
      content.add(ENHANCEMENT_ORB);
      content.add(EMPOWERED_ORB);
    });
  }
}
