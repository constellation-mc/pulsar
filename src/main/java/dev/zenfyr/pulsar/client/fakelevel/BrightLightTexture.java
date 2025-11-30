package dev.zenfyr.pulsar.client.fakelevel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;

/**
 * A {@link LightTexture} that never updates past the initial bright state.
 */
public final class BrightLightTexture extends LightTexture {

  public static final BrightLightTexture INSTANCE = new BrightLightTexture();

  private BrightLightTexture() {
    super(Minecraft.getInstance().gameRenderer, Minecraft.getInstance());
  }

  @Override
  public void updateLightTexture(float delta) {
    // no updates for you
  }
}
