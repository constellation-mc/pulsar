package dev.zenfyr.pulsar.api.util;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@SuppressWarnings("unused")
public class PlayerUtil {

  public static List<Player> getPlayers(TargetingConditions conditions, Level level, AABB box) {
    return level.players().stream()
        .filter(playerEntity ->
            box.contains(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ())
                && conditions.test((ServerLevel) level, null, playerEntity))
        .collect(ImmutableList.toImmutableList());
  }

  public static List<Player> findPlayersInRange(Level level, BlockPos pos, int range) {
    return getPlayers(
        TargetingConditions.forNonCombat().range(range), level, new AABB(pos).inflate(range));
  }

  public static List<Player> findNonCreativePlayersInRange(Level level, BlockPos pos, int range) {
    return findPlayersInRange(level, pos, range).stream()
        .filter(player -> !player.isCreative())
        .collect(ImmutableList.toImmutableList());
  }

  public static @NotNull Optional<Player> findClosestPlayerInRange(
      Level level, BlockPos pos, int range) {
    return findPlayersInRange(level, pos, range).stream()
        .min(Comparator.comparingDouble(
            player -> player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())));
  }

  public static @NotNull Optional<Player> findClosestNonCreativePlayerInRange(
      Level level, BlockPos pos, int range) {
    return findNonCreativePlayersInRange(level, pos, range).stream()
        .min(Comparator.comparingDouble(
            player -> player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())));
  }
}
