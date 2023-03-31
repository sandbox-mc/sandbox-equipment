package io.sandbox.equipment.configTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import io.sandbox.equipment.tags.TagsLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class AttributeConfig {
  public List<Attribute> attributes = new ArrayList<>();

  public Attribute getAttributeByName(String name) {
    for (Attribute attribute : attributes) {
      if (attribute.attributeName == name) {
        return attribute;
      }
    }

    return null;
  }

  public List<EquipmentSlot> getSlots(ItemStack itemStack) {
    List<EquipmentSlot> slotList = new ArrayList<>();
    if (itemStack.isIn(TagsLoader.MAINHAND_SLOT)) {
      slotList.add(EquipmentSlot.MAINHAND);
    }

    if (itemStack.isIn(TagsLoader.OFFHAND_SLOT)) {
      slotList.add(EquipmentSlot.OFFHAND);
    }
    
    if (itemStack.isIn(TagsLoader.FEET_SLOT)) {
      slotList.add(EquipmentSlot.FEET);
    }
    
    if (itemStack.isIn(TagsLoader.HEAD_SLOT)) {
      slotList.add(EquipmentSlot.HEAD);
    }
    
    if (itemStack.isIn(TagsLoader.LEGS_SLOT)) {
      slotList.add(EquipmentSlot.LEGS);
    }
    
    if (itemStack.isIn(TagsLoader.CHEST_SLOT)) {
      slotList.add(EquipmentSlot.CHEST);
    }

    return slotList;
  }

  public List<AttributeLevelRoll> getRandomWeightedAttribute(int level, ItemStack itemToCheck, Integer count, Random random) {
    Map<Integer, AttributeLevelRoll> weightedList = new HashMap<>();
    for (Attribute attribute : attributes) {
      List<AttributeLevelRoll> attributeRolls = new ArrayList<>();

      // Does the attribute allow this item?
      Boolean allowed = false;

      // Is it part of an allowTag?
      for (String allowedTag : attribute.allowedTags) {
        TagKey<Item> tag = TagKey.of(RegistryKeys.ITEM, new Identifier(allowedTag));
        if (tag != null && itemToCheck.isIn(tag)) {
          allowed = true;
          break;
        }
      }

      // is it in the allowList?
      for (String allowedItem : attribute.allowedItems) {
        Item allowedItemType = Registries.ITEM.get(new Identifier(allowedItem));
        if (allowedItemType != null && itemToCheck.isOf(allowedItemType)) {
          allowed = true;
          break;
        }
      }

      if (!allowed) {
        continue;
      }

      // Is there any level rolls that match?
      for (AttributeLevelRoll slotAttributeLevel : attribute.levelRolls) {
        if (slotAttributeLevel.ilvl.matchLevel(level)) {
          attributeRolls.add(slotAttributeLevel);
        }
      }

      if (attributeRolls.size() == 0) {
        // Skip if does not match level
        continue;
      }

      // Only one slot can take each roll
      for (AttributeLevelRoll attrLevel : attributeRolls) {
        // Populate child with name for lookup later
        attrLevel.attributeName = attribute.attributeName;
  
        TreeSet<Integer> previousWeightSet = new TreeSet<Integer>(weightedList.keySet());
        int lastMaxTotalWeight = weightedList.size() > 0 ? previousWeightSet.last() : 0;
  
        weightedList.put(lastMaxTotalWeight + attrLevel.weight, attrLevel);
      }
    }

    // Return null if no attributes match for itemType
    if (weightedList.size() == 0) {
      return null;
    }

    TreeSet<Integer> weights = new TreeSet<Integer>(weightedList.keySet());
    List<AttributeLevelRoll> attributeList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int weightRoll = random.nextInt(weights.last()) + 1;
      // Iterate from lowest to greatest until hit
      for (Integer weightKey : weights) {
        if (weightRoll <= weightKey) {
          attributeList.add(weightedList.get(weightKey));
          break;
        }
      }
    }

    return attributeList.size() > 0 ? attributeList : null;
  }
}
