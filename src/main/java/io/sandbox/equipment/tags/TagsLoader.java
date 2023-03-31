package io.sandbox.equipment.tags;

import io.sandbox.equipment.Main;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TagsLoader {
  public static final TagKey<Item> PARRY_ITEMS = TagKey.of(RegistryKeys.ITEM, Main.id("parry_items"));
  public static final TagKey<Item> LIFE_ON_HIT_ITEMS = TagKey.of(RegistryKeys.ITEM, Main.id("life_on_hit_items"));

  // Slot tags
  public static final TagKey<Item> MAINHAND_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("mainhand_slot"));
  public static final TagKey<Item> OFFHAND_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("offhand_slot"));
  public static final TagKey<Item> FEET_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("feet_slot"));
  public static final TagKey<Item> LEGS_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("legs_slot"));
  public static final TagKey<Item> CHEST_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("chest_slot"));
  public static final TagKey<Item> HEAD_SLOT = TagKey.of(RegistryKeys.ITEM, Main.id("head_slot"));


  public static void init () {
  }
}
