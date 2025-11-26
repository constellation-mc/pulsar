package dev.zenfyr.pulsar.client.fakeworld;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;

public final class AlwaysBrightLightmapTextureManager extends LightTexture {

  public static final AlwaysBrightLightmapTextureManager INSTANCE =
      new AlwaysBrightLightmapTextureManager();

  private AlwaysBrightLightmapTextureManager() {
    super(Minecraft.getInstance().gameRenderer, Minecraft.getInstance());
  }

  @Override
  public void updateLightTexture(float delta) {
    // no updates for you
  }
}
