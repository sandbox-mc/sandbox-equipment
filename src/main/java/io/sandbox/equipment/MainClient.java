package io.sandbox.equipment;

import io.sandbox.equipment.client.HudOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MainClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    HudRenderCallback.EVENT.register(new HudOverlay());
  }
}
