package dev.zenfyr.pulsar.impl.client.fakelevel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;

public class BrightLightTextureImpl extends LightTexture {

  public static final LightTexture INSTANCE = new BrightLightTextureImpl();

  public BrightLightTextureImpl() {
    super(Minecraft.getInstance().gameRenderer, Minecraft.getInstance());
  }

  @Override
  public void updateLightTexture(float delta) {
    // no updates for you
  }
}
