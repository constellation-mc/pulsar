package dev.zenfyr.pulsar.api.client.fakelevel;

import dev.zenfyr.pulsar.impl.client.fakelevel.BrightLightTextureImpl;
import net.minecraft.client.renderer.Lightmap;

/**
 * A {@link Lightmap} that never updates past the initial bright state.
 */
public final class BrightLightTexture {

  public static Lightmap getInstance() {
    return BrightLightTextureImpl.INSTANCE;
  }
}
