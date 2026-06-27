package dev.zenfyr.pulsar.api.client.fakelevel;

import dev.zenfyr.pulsar.impl.client.fakelevel.BrightLightTextureImpl;
import net.minecraft.client.renderer.LightTexture;

/**
 * A {@link LightTexture} that never updates past the initial bright state.
 */
public final class BrightLightTexture {

  public static LightTexture getInstance() {
    return BrightLightTextureImpl.INSTANCE;
  }
}
