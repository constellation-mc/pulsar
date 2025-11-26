package dev.zenfyr.pulsar.util;

import lombok.experimental.UtilityClass;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@SuppressWarnings("unused")
public class TextUtil {

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull MutableComponent translatable(String key) {
    return Component.translatable(key);
  }

  @Contract(value = "_, _ -> new", pure = true)
  public static @NotNull MutableComponent translatable(String key, Object... args) {
    return Component.translatable(key, args);
  }

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull MutableComponent literal(String text) {
    return Component.literal(text);
  }

  @Contract(value = " -> new", pure = true)
  public static @NotNull MutableComponent empty() {
    return Component.empty();
  }
}
