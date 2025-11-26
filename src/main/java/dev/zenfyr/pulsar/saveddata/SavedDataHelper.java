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
      @NotNull ServerLevel world,
      Function<CompoundTag, T> readFunction,
      Supplier<T> supplier,
      String id) {
    return world.getDataStorage().computeIfAbsent(readFunction, supplier, id);
  }

  public static <T extends SavedData & DeserializableData> T getOrCreate(
      ServerLevel world, Supplier<T> supplier, String id) {
    return getOrCreate(
        world,
        nbt -> {
          T state = supplier.get();
          state.readNbt(nbt);
          return state;
        },
        supplier,
        id);
  }

  public static boolean isStateLoaded(@NotNull ServerLevel world, String id) {
    return world.getDataStorage().cache.containsKey(id);
  }

  public static <T extends SavedData> void consumeIfLoaded(
      ServerLevel world,
      String id,
      BiFunction<ServerLevel, String, T> getFunc,
      Consumer<T> action) {
    if (isStateLoaded(world, id)) {
      action.accept(getFunc.apply(world, id));
    }
  }

  public static <T extends SavedData, R> Optional<R> processIfLoaded(
      ServerLevel world,
      String id,
      BiFunction<ServerLevel, String, T> getFunc,
      Function<T, R> action) {
    if (isStateLoaded(world, id)) {
      return Optional.ofNullable(action.apply(getFunc.apply(world, id)));
    }
    return Optional.empty();
  }
}
