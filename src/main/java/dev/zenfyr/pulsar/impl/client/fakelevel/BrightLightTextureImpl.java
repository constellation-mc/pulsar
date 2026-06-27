package dev.zenfyr.pulsar.impl.client.fakelevel;

import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;

public class BrightLightTextureImpl extends Lightmap {

  public static final Lightmap INSTANCE = new BrightLightTextureImpl();
  private static final LightmapRenderState RENDER_STATE = new LightmapRenderState();

  public BrightLightTextureImpl() {
    super();
  }

  public static void init() {
    // NOOP
  }

  @Override
  public void render(LightmapRenderState renderState) {
    super.render(RENDER_STATE);
  }
}
