package dev.zenfyr.pulsar.api.client.fakelevel;

import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;

/**
 * A {@link Lightmap} that never updates past the initial bright state.
 */
public final class BrightLightTexture extends Lightmap {

  public static final BrightLightTexture INSTANCE = new BrightLightTexture();
  private static final LightmapRenderState RENDER_STATE = new LightmapRenderState();

  @Override
  public void render(LightmapRenderState renderState) {
    super.render(RENDER_STATE);
  }

  public static void init() {
    // NOOP
  }
}
