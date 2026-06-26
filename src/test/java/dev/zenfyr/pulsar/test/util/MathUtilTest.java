package dev.zenfyr.pulsar.test.util;

import static org.junit.jupiter.api.Assertions.*;

import dev.zenfyr.pulsar.api.util.MathUtil;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class MathUtilTest {

  @Test
  void testNextDoubleRandom() {
    Random random = new Random(42);
    double min = 5.0;
    double max = 10.0;

    for (int i = 0; i < 100; i++) {
      double result = MathUtil.nextDouble(random, min, max);
      assertTrue(result >= min && result < max, "Value out of bounds: " + result);
    }

    assertEquals(10.0, MathUtil.nextDouble(random, 10.0, 5.0));
    assertEquals(5.0, MathUtil.nextDouble(random, 5.0, 5.0));
  }

  @Test
  void testNextDoubleThreadLocal() {
    double min = 1.5;
    double max = 2.5;
    double result = MathUtil.nextDouble(min, max);
    assertTrue(result >= min && result < max);
  }

  @Test
  void testNextFloatRandom() {
    Random random = new Random(42);
    float min = 1.0f;
    float max = 4.0f;

    for (int i = 0; i < 100; i++) {
      float result = MathUtil.nextFloat(random, min, max);
      assertTrue(result >= min && result < max);
    }

    assertEquals(4.0f, MathUtil.nextFloat(random, 4.0f, 1.0f));
  }

  @Test
  void testNextFloatThreadLocal() {
    float min = 10.0f;
    float max = 20.0f;
    float result = MathUtil.nextFloat(min, max);
    assertTrue(result >= min && result < max);
  }

  @Test
  void testNextIntRandom() {
    Random random = new Random(42);
    int min = 10;
    int max = 20;

    for (int i = 0; i < 100; i++) {
      int result = MathUtil.nextInt(random, min, max);
      assertTrue(result >= min && result <= max);
    }

    assertEquals(20, MathUtil.nextInt(random, 20, 10));
  }

  @Test
  void testNextIntThreadLocal() {
    int min = -5;
    int max = 5;
    int result = MathUtil.nextInt(min, max);
    assertTrue(result >= min && result <= max);
  }

  @Test
  void testNextLongRandom() {
    Random random = new Random(42);
    long min = 100L;
    long max = 200L;

    for (int i = 0; i < 100; i++) {
      long result = MathUtil.nextLong(random, min, max);
      assertTrue(result >= min && result <= max);
    }

    assertEquals(200L, MathUtil.nextLong(random, 200L, 100L));
  }

  @Test
  void testNextLongThreadLocal() {
    long min = 1000L;
    long max = 2000L;
    long result = MathUtil.nextLong(min, max);
    assertTrue(result >= min && result <= max);
  }

  @Test
  void testRandomFactoryMethods() {
    assertNotNull(MathUtil.threadRandom());
    assertNotNull(MathUtil.random());
    assertNotNull(MathUtil.random(12345L));
  }

  @Test
  void testClampDouble() {
    assertEquals(5.0, MathUtil.clamp(5.0, 1.0, 10.0));
    assertEquals(1.0, MathUtil.clamp(0.0, 1.0, 10.0));
    assertEquals(10.0, MathUtil.clamp(15.0, 1.0, 10.0));
  }

  @Test
  void testClampFloat() {
    assertEquals(5.0f, MathUtil.clamp(5.0f, 1.0f, 10.0f));
    assertEquals(1.0f, MathUtil.clamp(0.0f, 1.0f, 10.0f));
    assertEquals(10.0f, MathUtil.clamp(15.0f, 1.0f, 10.0f));
  }

  @Test
  void testClampInt() {
    assertEquals(5, MathUtil.clamp(5, 1, 10));
    assertEquals(1, MathUtil.clamp(0, 1, 10));
    assertEquals(10, MathUtil.clamp(15, 1, 10));
  }

  @Test
  void testClampLong() {
    assertEquals(5L, MathUtil.clamp(5L, 1L, 10L));
    assertEquals(1L, MathUtil.clamp(0L, 1L, 10L));
    assertEquals(10L, MathUtil.clamp(15L, 1L, 10L));
  }

  @Test
  void testFastFloor() {
    assertEquals(5, MathUtil.fastFloor(5.9));
    assertEquals(5, MathUtil.fastFloor(5.0));
    assertEquals(-6, MathUtil.fastFloor(-5.1));
  }

  @Test
  void testFastCeil() {
    assertEquals(6, MathUtil.fastCeil(5.1));
    assertEquals(5, MathUtil.fastCeil(5.0));
    assertEquals(-5, MathUtil.fastCeil(-5.9));
  }
}
