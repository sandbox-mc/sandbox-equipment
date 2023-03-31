package io.sandbox.equipment.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Multimap;

import io.sandbox.equipment.Main;
import io.sandbox.equipment.configTypes.AttributeConfig;
import io.sandbox.equipment.configTypes.AttributeLevelRoll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class StoneOfEnhancementItem extends Item {
  private static Random random = Random.create();
  private static final String[] ARMOR_MODIFIERS = new String[]{
    "845DB27C-C624-495F-8C9F-6020A9A58B6B",
    "D8499B04-0E66-4726-AB29-64469D734E0D",
    "9F3D476D-C118-4544-8365-64846904B48E",
    "2AD3F246-FEE1-4E67-B886-69FD380BB150"
  };
  public static final String ENHANCEMENT_COUNT = "enhancement_count";
  public int attributeCount;

  public StoneOfEnhancementItem(Settings settings, Integer attributeCount) {
    super(settings);
    this.attributeCount = attributeCount;
  }

  public static void applyAttributes() {

  }

  @Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    ItemStack stoneStack = player.getStackInHand(hand);
    ItemStack itemStack = null;

    // Get the item in the hand not holding this item
    if (hand.equals(Hand.MAIN_HAND)) {
      itemStack = player.getOffHandStack();
    } else {
      itemStack = player.getMainHandStack();
    }
    
    if (itemStack != null) {
      NbtCompound nbt = itemStack.getOrCreateNbt();
      Integer enhancementCount = nbt.getInt(ENHANCEMENT_COUNT);

      // Check if already enhanced and alert if it is
      if (enhancementCount >= this.attributeCount) {
        player.sendMessage(Text.translatable("item.message.is_enhanced"), true);
        return TypedActionResult.success(stoneStack); // return original stack
      }

      AttributeConfig attributeConfig = Main.getAttributeConfig();

      // Randomly selected list of attributes
      List<AttributeLevelRoll> attributeLevelRolls = attributeConfig.getRandomWeightedAttribute(
        0, itemStack, this.attributeCount - enhancementCount, random);
      List<EquipmentSlot> slots = attributeConfig.getSlots(itemStack);

      if (attributeLevelRolls == null || attributeLevelRolls.size() == 0 || slots.size() == 0) {
        player.sendMessage(
          Text.translatable(
            "item.message.item_not_added_to_config",
            Text.translatable(itemStack.getTranslationKey())
          ),
          true);
        return TypedActionResult.success(stoneStack); // return original stack
      }

      for (EquipmentSlot slot : EquipmentSlot.values()) {
        // Get base stats to add as base attributes
        Multimap<EntityAttribute, EntityAttributeModifier> multimap = itemStack.getItem().getAttributeModifiers(slot);
  
        if (multimap.size() > 0) {
          // Add base stats
          for (Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
            itemStack.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
          }
        }
      }

      for (AttributeLevelRoll attributeLevelRoll : attributeLevelRolls) {
        EntityAttribute attribute = Registries.ATTRIBUTE.get(new Identifier(attributeLevelRoll.attributeName));
        
        for (EquipmentSlot slot : slots) {
          itemStack.addAttributeModifier(
            attribute,
            new EntityAttributeModifier(attributeLevelRoll.attributeName + itemStack.getTranslationKey(), attributeLevelRoll.rollValue(), Operation.ADDITION),
            slot
          );
        }
      }

      // Hide original tooltips
      itemStack.addHideFlag(net.minecraft.item.ItemStack.TooltipSection.MODIFIERS);
      itemStack.addHideFlag(net.minecraft.item.ItemStack.TooltipSection.ENCHANTMENTS);

      // Mark how many enhancements it has total
      nbt.putInt(ENHANCEMENT_COUNT, attributeLevelRolls.size() + enhancementCount);
      itemStack.setNbt(nbt);
      stoneStack.decrement(1);
    }

    return TypedActionResult.success(stoneStack);
  }

  public static void appendAttributeTooltip(
    ItemStack stack,
    @Nullable World world,
    List<Text> tooltip,
    TooltipContext context
  ) {
    for (EquipmentSlot slot : EquipmentSlot.values()) {
      Multimap<EntityAttribute, EntityAttributeModifier> attributes = stack.getAttributeModifiers(slot);
      MinecraftClient client = MinecraftClient.getInstance();
      ClientPlayerEntity clientPlayer = client.player;
      boolean hasAddedArmor = false;
  
      if (slot == EquipmentSlot.MAINHAND) {
        // Add up damage stats
        Iterator<EntityAttributeModifier> damageAttributeIterator = attributes.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
          .iterator();
        if (damageAttributeIterator.hasNext()) {
          double totalDamage = clientPlayer.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
          totalDamage += (double) EnchantmentHelper.getAttackDamage(stack, EntityGroup.DEFAULT);
          while (damageAttributeIterator.hasNext()) {
            EntityAttributeModifier modifier = damageAttributeIterator.next();
            totalDamage += modifier.getValue();
          }
      
          tooltip.add(ScreenTexts.EMPTY);
          tooltip.add(Text.translatable("item.stat.display.damage", ItemStack.MODIFIER_FORMAT.format(totalDamage)).formatted(Formatting.DARK_GREEN));
        }
    
        // Add up Attack Speed Stats
        Iterator<EntityAttributeModifier> attackSpeedAttributeIterator = attributes
            .get(EntityAttributes.GENERIC_ATTACK_SPEED).iterator();
        if (attackSpeedAttributeIterator.hasNext()) {
          double totalAttackSpeed = clientPlayer.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
          while (attackSpeedAttributeIterator.hasNext()) {
            EntityAttributeModifier modifier = attackSpeedAttributeIterator.next();
            totalAttackSpeed += modifier.getValue();
          }
      
          tooltip.add(Text.translatable("item.stat.display.attack_speed", ItemStack.MODIFIER_FORMAT.format(totalAttackSpeed)).formatted(Formatting.DARK_GREEN));
        }
      }

      if (!hasAddedArmor) {
        Iterator<EntityAttributeModifier> armorAttributeIterator = attributes.get(EntityAttributes.GENERIC_ARMOR).iterator();
        if (armorAttributeIterator.hasNext()) {
          double totalArmor = 0;
          while (armorAttributeIterator.hasNext()) {
            EntityAttributeModifier modifier = armorAttributeIterator.next();
            totalArmor += modifier.getValue();
          }
      
          tooltip.add(ScreenTexts.EMPTY);
          tooltip.add(Text.translatable("item.stat.display.armor." + slot.getName(), ItemStack.MODIFIER_FORMAT.format(totalArmor)).formatted(Formatting.DARK_GREEN));
          hasAddedArmor = true;
        }

        Iterator<EntityAttributeModifier> armorToughnessAttributeIterator = attributes.get(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).iterator();
        if (armorToughnessAttributeIterator.hasNext()) {
          double totalArmorToughness = 0;
          while (armorToughnessAttributeIterator.hasNext()) {
            EntityAttributeModifier modifier = armorToughnessAttributeIterator.next();
            totalArmorToughness += modifier.getValue();
          }

          if (totalArmorToughness != 0) {
            tooltip.add(Text.translatable("item.stat.display.armor.armor_toughness", ItemStack.MODIFIER_FORMAT.format(totalArmorToughness)).formatted(Formatting.DARK_GREEN));
          }
        }

        Iterator<EntityAttributeModifier> knockbackAttributeIterator = attributes.get(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).iterator();
        if (knockbackAttributeIterator.hasNext()) {
          double totalKnockbackResistance = 0;
          while (knockbackAttributeIterator.hasNext()) {
            EntityAttributeModifier modifier = knockbackAttributeIterator.next();
            totalKnockbackResistance += modifier.getValue();
          }

          if (totalKnockbackResistance != 0) {
            tooltip.add(Text.translatable("item.stat.display.armor.knockback_resistance", ItemStack.MODIFIER_FORMAT.format(totalKnockbackResistance * 10)).formatted(Formatting.DARK_GREEN));
          }
        }
      }
  

      if (!attributes.isEmpty()) {
        tooltip.add(ScreenTexts.EMPTY);
        Iterator<Entry<EntityAttribute, EntityAttributeModifier>> attribtuIterator = attributes.entries().iterator();
        while (attribtuIterator.hasNext()) {
          Entry<EntityAttribute, EntityAttributeModifier> entry = (Entry<EntityAttribute, EntityAttributeModifier>) attribtuIterator.next();
          EntityAttributeModifier entityAttributeModifier = entry.getValue();
          EntityAttribute entityAttribute = entry.getKey();
          if (
            entityAttributeModifier.getId().equals(Item.ATTACK_DAMAGE_MODIFIER_ID) ||
            entityAttributeModifier.getId().equals(Item.ATTACK_SPEED_MODIFIER_ID) ||
            Arrays.asList(ARMOR_MODIFIERS).contains(entityAttributeModifier.getId().toString().toUpperCase())
          ) {
            // We've already printed off the base attack_speed/base_damage/armor attributes
            continue;
          }

          double value = entityAttributeModifier.getValue();
          if (value != 0) {
            tooltip.add(
              Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(),
                new Object[] {
                  ItemStack.MODIFIER_FORMAT.format(value),
                  Text.translatable(entityAttribute.getTranslationKey())
                }).formatted(Formatting.BLUE));
          }

        }
      }
    }

    NbtList enchantments = stack.getEnchantments();
    if (enchantments.size() > 0) {
      if (Screen.hasShiftDown()) {
        tooltip.add(ScreenTexts.EMPTY);
        ItemStack.appendEnchantments(tooltip, enchantments);
        tooltip.add(ScreenTexts.EMPTY);
      } else {
        tooltip.add(ScreenTexts.EMPTY);
        appendEnchantmentIcons(tooltip, enchantments);
        tooltip.add(ScreenTexts.EMPTY);
      }
    }
  }

  public static void appendEnchantmentIcons(List<Text> tooltip, NbtList enchantments) {
    List<Object> textList = new ArrayList<Object>();
    for (int i = 0; i < enchantments.size(); ++i) {
      NbtCompound nbtCompound = enchantments.getCompound(i);
      Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent((e) -> {
        textList.add(Text.translatable(e.getTranslationKey() + ".icon"));
      });
    }

    tooltip.add(Text.translatable("enchantment.sandbox.icon.list", textList.toArray()));
  }
}
