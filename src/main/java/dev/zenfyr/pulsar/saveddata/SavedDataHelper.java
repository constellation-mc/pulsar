package dev.zenfyr.pulsar.saveddata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@ApiStatus.Experimental
public final class SavedDataHelper {

  public static <T extends SavedData> T getOrCreate(
      @NotNull ServerLevel level,
      Function<CompoundTag, T> readFunction,
      Supplier<T> supplier,
      String id) {
    return level.getDataStorage().computeIfAbsent(readFunction, supplier, id);
  }

  public static <T extends SavedData & DeserializableData> T getOrCreate(
      ServerLevel level, Supplier<T> supplier, String id) {
    return getOrCreate(
        level,
        nbt -> {
          T state = supplier.get();
          state.readSaveData(nbt);
          return state;
        },
        supplier,
        id);
  }

  public static boolean isStateLoaded(@NotNull ServerLevel level, String id) {
    return level.getDataStorage().cache.containsKey(id);
  }

  public static <T extends SavedData> void consumeIfLoaded(
      ServerLevel level,
      String id,
      BiFunction<ServerLevel, String, T> getFunc,
      Consumer<T> action) {
    if (isStateLoaded(level, id)) {
      action.accept(getFunc.apply(level, id));
    }
  }

  public static <T extends SavedData, R> Optional<R> processIfLoaded(
      ServerLevel level,
      String id,
      BiFunction<ServerLevel, String, T> getFunc,
      Function<T, R> action) {
    if (isStateLoaded(level, id)) {
      return Optional.ofNullable(action.apply(getFunc.apply(level, id)));
    }
    return Optional.empty();
  }
}
