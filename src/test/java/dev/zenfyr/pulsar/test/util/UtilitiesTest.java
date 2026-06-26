package dev.zenfyr.pulsar.test.util;

import static org.junit.jupiter.api.Assertions.*;

import dev.zenfyr.pulsar.api.util.Utilities;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UtilitiesTest {

  @Test
  void testPickAtRandom() {
    Float[] list = new Float[] {1f, 2f, 3f, 4f, 5f, 6f};
    assertNotNull(Utilities.pickAtRandom(list));
  }

  @Test
  void testPickAtRandomList() {
    List<Float> list = List.of(1f, 2f, 3f, 4f, 5f, 6f);
    assertNotNull(Utilities.pickAtRandom(list));
  }

  @Test
  void testGetTruth() {
    assertNotNull(Utilities.getTruth());
    assertTrue(Utilities.getTruth().getAsBoolean());
  }

  @Test
  void testGetFalse() {
    assertNotNull(Utilities.getFalse());
    assertFalse(Utilities.getFalse().getAsBoolean());
  }

  @Test
  void testCast() {
    assertNotNull(Utilities.cast(new Object()));
  }

  @Test
  void testSupply() {
    var value = Utilities.supply(() -> 13);
    assertEquals(13, value);
  }

  @Test
  void testSupplyObject() {
    var value = Utilities.supply(new ArrayList<>(), (val) -> {
      val.add(13);
    });
    assertEquals(13, value.get(0));
  }

  String dummyCaller() {
    return Utilities.getCallerName();
  }

  @Test
  void testGetCaller() {
    String value = dummyCaller();
    assertEquals("dev.zenfyr.pulsar.test.util.UtilitiesTest#testGetCaller", value);
  }

  Class<?> dummyCallerClass() {
    return Utilities.getCallerClass();
  }

  @Test
  void testGetCallerClass() {
    Class<?> value = dummyCallerClass();
    assertEquals(UtilitiesTest.class, value);
  }

  @FunctionalInterface
  interface CustomFunction {
    String invoke();
  }

  public static String getTestLambda() {
    return "metafactory swag";
  }

  @Test
  void testMakeLambdaCustom() throws NoSuchMethodException, IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findStatic(
        UtilitiesTest.class, "getTestLambda", MethodType.methodType(String.class));

    CustomFunction lambda = Utilities.makeLambda(lookup, CustomFunction.class, handle);

    assertNotNull(lambda);
    assertEquals("metafactory swag", lambda.invoke());
  }
}
