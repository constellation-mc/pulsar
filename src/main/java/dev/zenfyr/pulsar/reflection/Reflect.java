package dev.zenfyr.pulsar.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
@SuppressWarnings("unused")
public class Reflect {

  //
  // find members
  //

  public static <T> Optional<Constructor<T>> findConstructor(
      @NotNull Class<T> clazz, Object... args) {
    return Optional.ofNullable(
        findConstructor0(clazz, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)));
  }

  public static <T> Optional<Constructor<T>> findConstructor(
      @NotNull Class<T> clazz, Class<?>... args) {
    return Optional.ofNullable(findConstructor0(clazz, args));
  }

  public static Optional<Method> findMethod(@NotNull Class<?> clazz, String name, Object... args) {
    return Optional.ofNullable(findMethod0(
        clazz, false, name, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)));
  }

  public static Optional<Method> findMethod(
      @NotNull Class<?> clazz, String name, Class<?>... args) {
    return Optional.ofNullable(findMethod0(clazz, false, name, args));
  }

  public static Optional<Field> findField(@NotNull Class<?> clazz, String name) {
    return Optional.ofNullable(findField0(clazz, false, name));
  }

  public static Optional<Method> findMethodInHierarchy(
      @NotNull Class<?> clazz, String name, Object... args) {
    return Optional.ofNullable(findMethod0(
        clazz, true, name, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)));
  }

  public static Optional<Method> findMethodInHierarchy(
      @NotNull Class<?> clazz, String name, Class<?>... args) {
    return Optional.ofNullable(findMethod0(clazz, true, name, args));
  }

  public static Optional<Field> findFieldInHierarchy(@NotNull Class<?> clazz, String name) {
    return Optional.ofNullable(findField0(clazz, true, name));
  }

  //
  // set accessible
  //

  public static <T extends AccessibleObject> T setAccessible(T member) {
    return Reflect.setAccessible(member, true);
  }

  public static <T extends AccessibleObject> T setAccessible(@NonNull T member, boolean set) {
    try {
      member.setAccessible(set);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return member;
  }

  private static @Nullable <T> Constructor<T> findConstructor0(
      @NonNull Class<T> clazz, Class<?>... classes) {
    Constructor<T>[] ctxs = (Constructor<T>[]) clazz.getDeclaredConstructors();
    if (ctxs.length == 1) {
      return checkCtx(ctxs[0], classes) ? ctxs[0] : null;
    } else {
      try {
        return clazz.getDeclaredConstructor(classes);
      } catch (Exception e) {
        for (Constructor<T> ctx : ctxs) {
          if (checkCtx(ctx, classes)) return ctx;
        }
      }
    }
    return null;
  }

  private static boolean checkCtx(@NonNull Constructor<?> ctx, Class<?>[] classes) {
    if (ctx.getParameterCount() != classes.length) return false;

    Class<?>[] pt = ctx.getParameterTypes();
    for (int i = 0; i < ctx.getParameterCount(); i++) {
      if (!ClassUtils.isAssignable(classes[i], pt[i])) {
        return false;
      }
    }
    return true;
  }

  private static @Nullable <T> Method findMethod0(
      @NonNull Class<T> clazz, boolean traverse, String name, Class<?>... classes) {
    Method[] methods = clazz.getDeclaredMethods();
    if (methods.length == 1) {
      return checkMethod(methods[0], name, classes) ? methods[0] : null;
    } else {
      try {
        return clazz.getDeclaredMethod(name, classes);
      } catch (Throwable e) {
        for (Method method : methods) {
          if (checkMethod(method, name, classes)) return method;
        }
      }
    }
    return traverse && clazz.getSuperclass() != null
        ? findMethod0(clazz.getSuperclass(), true, name, classes)
        : null;
  }

  private static boolean checkMethod(@NonNull Method method, String name, Class<?>[] classes) {
    if (!method.getName().equals(name)) return false;
    if (method.getParameterCount() != classes.length) return false;

    Class<?>[] pt = method.getParameterTypes();
    for (int i = 0; i < method.getParameterCount(); i++) {
      if (!ClassUtils.isAssignable(classes[i], pt[i])) {
        return false;
      }
    }
    return true;
  }

  private static <T> @Nullable Field findField0(
      @NonNull Class<T> clazz, boolean traverse, String name) {
    Field[] fields = clazz.getDeclaredFields();
    if (fields.length == 1) {
      return fields[0].getName().equals(name) ? fields[0] : null;
    } else {
      for (Field field : fields) {
        if (field.getName().equals(name)) {
          return field;
        }
      }
    }
    return traverse && clazz.getSuperclass() != null
        ? findField0(clazz.getSuperclass(), true, name)
        : null;
  }
}
