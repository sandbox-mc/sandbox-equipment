package io.sandbox.equipment.client;

import com.mojang.blaze3d.systems.RenderSystem;

import io.sandbox.equipment.Main;
import io.sandbox.equipment.attributes.AttributeLoader;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class HudOverlay implements HudRenderCallback {

  // Block Strength Icons
  private static final Identifier BLOCK_STR_ICON = Main.id("textures/hud_elements/gilded_shield_icon.png");
  private static final Identifier BLOCK_STR_DISABLED_FILL = Main.id("textures/hud_elements/block_strength_disabled_fill.png");

  // Rage Icons
  private static final Identifier RAGE_ICON = Main.id("textures/hud_elements/skull-14x14.png");
  private static final Identifier RAGE_FILL_ICON = Main.id("textures/hud_elements/red_skull-14x14.png");

  @Override
  public void onHudRender(MatrixStack matrixStack, float tickDelta) {
    int x = 0;
    int y = 0;
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null) {
      return;
    }

    // Get the starting point on the window
    int width = client.getWindow().getScaledWidth();
    int height = client.getWindow().getScaledHeight();
    x = width / 2;
    y = height;

    ClientPlayerEntity player = client.player;
    EntityAttributeInstance blockStrengthAttr = player.getAttributeInstance(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);
    double blockStrengthValue = blockStrengthAttr.getBaseValue();
    double maxBlockStrengthValue = player.getAttributeBaseValue(AttributeLoader.MAX_BLOCK_STRENGTH_ATTRIBUTE);

    // Only render block icon if holding a shield and they've blocked something recently
    if (player.isHolding(Items.SHIELD) && blockStrengthValue < maxBlockStrengthValue) {
  
      // 22 is how tall the shield icon is
      int percentBlockStrengthToPixels = (int)Math.ceil((blockStrengthValue / maxBlockStrengthValue) * 22);
  
      // RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      
      // Draw Block Strength icon
      RenderSystem.setShaderTexture(0, BLOCK_STR_ICON);
      DrawableHelper.drawTexture(matrixStack, x - 119, y - 46,0,0,21,22,21,22);
  
      // Overlay the disabled amount
      RenderSystem.setShaderTexture(0, BLOCK_STR_DISABLED_FILL);
      DrawableHelper.drawTexture(matrixStack,
        x - 119,
        y - 46 + percentBlockStrengthToPixels,
        0,
        percentBlockStrengthToPixels,
        21,
        22 - percentBlockStrengthToPixels,
        21,
        22
      );
    }

    double rageValue = player.getAttributeBaseValue(AttributeLoader.RAGE_LEVEL_ATTRIBUTE);
    if (rageValue > 0) {
      double maxRageValue = player.getAttributeValue(AttributeLoader.MAX_RAGE_ATTRIBUTE);
      
      int precentRage = 16 - (int)Math.ceil((rageValue / maxRageValue) * 16);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      
      // Draw Block Strength icon
      RenderSystem.setShaderTexture(0, RAGE_ICON);
      DrawableHelper.drawTexture(matrixStack, x - 9, y - 56,0,0,16,16,16,16);

      // Overlay the disabled amount
      RenderSystem.setShaderTexture(0, RAGE_FILL_ICON);
      DrawableHelper.drawTexture(matrixStack,
        x - 9,
        y - 56 + precentRage,
        0,
        precentRage,
        16,
        16 - precentRage,
        16,
        16
      );
    }
  }
}
