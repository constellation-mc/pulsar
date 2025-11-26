package dev.zenfyr.pulsar.mixin;

import dev.zenfyr.pulsar.util.Utilities;
import java.util.*;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.tree.AnnotationNode;

@UtilityClass
public class AsmUtil {

  private static final List<Map<String, Object>> EMPTY_ANN_LIST =
      Collections.unmodifiableList(new ArrayList<>());

  public static Map<String, Object> mapAnnotationNode(AnnotationNode node) {
    Map<String, Object> values = new HashMap<>();

    if (node == null || node.values == null) return values;

    for (int i = 0; i < node.values.size(); i += 2) {
      String name = (String) node.values.get(i);
      Object value = AsmUtil.mapObjectFromAnnotation(node.values.get(i + 1), true, false);
      if (name != null && value != null) values.putIfAbsent(name, value);
    }

    return values;
  }

  public static <T> T getAnnotationValue(AnnotationNode node, String name, T defaultValue) {
    if (node == null || node.values == null) return defaultValue;

    for (int i = 0; i < node.values.size(); i += 2) {
      if (name.equals(node.values.get(i))) {
        return Utilities.cast(AsmUtil.mapObjectFromAnnotation(node.values.get(i + 1), true, false));
      }
    }

    return defaultValue;
  }

  public static Object mapObjectFromAnnotation(Object value) {
    return AsmUtil.mapObjectFromAnnotation(value, true, false);
  }

  public static Object mapObjectFromAnnotation(
      Object value, boolean loadEnums, boolean loadClasses) {
    if (value instanceof List<?> list) {
      List<Object> process = new ArrayList<>(list.size());
      for (Object o : list) {
        process.add(mapObjectFromAnnotation(o, true, false));
      }
      return process;
    } else if (value instanceof AnnotationNode node) {
      Map<String, Object> values = new HashMap<>();

      if (node == null || node.values == null) return values;

      for (int i = 0; i < node.values.size(); i += 2) {
        String name = (String) node.values.get(i);
        Object value1 = mapObjectFromAnnotation(node.values.get(i + 1), true, false);
        if (name != null && value1 != null) values.putIfAbsent(name, value1);
      }

      return values;
    } else if (value instanceof Type type && loadClasses) {
      try {
        return Class.forName(type.getClassName());
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    } else if (value instanceof String[] enum0 && loadEnums) {
      try {
        Class<?> cls =
            Class.forName(enum0[0].replace('/', '.').substring(1, enum0[0].length() - 1));
        if (Enum.class.isAssignableFrom(cls)) {
          return Enum.valueOf(Utilities.cast(cls), enum0[1]);
        }
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    } else {
      return value;
    }
    return value;
  }

  public static void insAdapter(MethodVisitor mv, @NonNull Consumer<InstructionAdapter> consumer) {
    consumer.accept(new InstructionAdapter(mv));
  }

  public static void insAdapter(
      ClassVisitor cv,
      int access,
      String name,
      String descriptor,
      Consumer<InstructionAdapter> consumer) {
    insAdapter(cv, access, name, descriptor, null, null, consumer);
  }

  public static void insAdapter(
      ClassVisitor cv,
      int access,
      String name,
      String descriptor,
      String signature,
      Consumer<InstructionAdapter> consumer) {
    insAdapter(cv, access, name, descriptor, signature, null, consumer);
  }

  public static void insAdapter(
      @NonNull ClassVisitor cv,
      int access,
      String name,
      String descriptor,
      @Nullable String signature,
      @Nullable String[] exceptions,
      @NonNull Consumer<InstructionAdapter> consumer) {
    consumer.accept(
        new InstructionAdapter(cv.visitMethod(access, name, descriptor, signature, exceptions)));
  }

  public static List<Map<String, Object>> emptyAnnotationList() {
    return EMPTY_ANN_LIST;
  }
}
