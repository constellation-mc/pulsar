package dev.zenfyr.pulsar.test.util;

import dev.zenfyr.pulsar.util.ColorUtil;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColorUtilTest {

  @ParameterizedTest
  @CsvSource({
    "1, 0, 0, 0xffff0000",
    "0, 1, 0, 0xff00ff00",
    "0, 0, 1, 0xff0000ff",
    "1, 1, 1, -1",
    "0, 0, 0, 0xff000000"
  })
  void testToColorFloat(float r, float g, float b, String expected) {
    int asColor = Long.decode(expected).intValue();
    Assertions.assertThat(ColorUtil.toColor(r, g, b)).isEqualTo(asColor);
  }

  @ParameterizedTest
  @CsvSource({
    "1, 0, 0, 0.5, 0x80ff0000",
    "0, 1, 0, 0.75, 0xbf00ff00",
    "0, 0, 1, 0.5, 0x800000ff",
    "1, 1, 1, 0.5, 0x80ffffff",
    "0, 0, 0, 0, 0"
  })
  void testToColorFloatAlpha(float r, float g, float b, float a, String expected) {
    int asColor = Long.decode(expected).intValue();
    Assertions.assertThat(ColorUtil.toColor(r, g, b, a)).isEqualTo(asColor);
  }

  @ParameterizedTest
  @CsvSource({
    "255, 0, 0, 0xffff0000",
    "0, 255, 0, 0xff00ff00",
    "0, 0, 255, 0xff0000ff",
    "255, 255, 255, 0xffffffff",
    "0, 0, 0, 0xff000000"
  })
  void testToColorInt(int r, int g, int b, String expected) {
    int asColor = Long.decode(expected).intValue();
    Assertions.assertThat(ColorUtil.toColor(r, g, b)).isEqualTo(asColor);
  }

  @ParameterizedTest
  @CsvSource({
    "255, 0, 0, 128, 0x80ff0000",
    "0, 255, 0, 191, 0xbf00ff00",
    "0, 0, 255, 128, 0x800000ff",
    "255, 255, 255, 128, 0x80ffffff",
    "0, 0, 0, 0, 0"
  })
  void testToColorIntAlpha(int r, int g, int b, int a, String expected) {
    int asColor = Long.decode(expected).intValue();
    Assertions.assertThat(ColorUtil.toColor(r, g, b, a)).isEqualTo(asColor);
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 128", "0xbf00ff00, 191", "0x800000ff, 128", "0x80ffffff, 128", "0, 0"})
  void testGetAlpha(String color, int expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getAlpha(asColor)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 255", "0xbf00ff00, 0", "0x800000ff, 0", "0x80ffffff, 255", "0, 0"})
  void testGetRed(String color, int expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getRed(asColor)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 0", "0xbf00ff00, 255", "0x800000ff, 0", "0x80ffffff, 255", "0, 0"})
  void testGetGreen(String color, int expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getGreen(asColor)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 0", "0xbf00ff00, 0", "0x800000ff, 255", "0x80ffffff, 255", "0, 0"})
  void testGetBlue(String color, int expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getBlue(asColor)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 0.5", "0xbf00ff00, 0.75", "0x800000ff, 0.5", "0xffffffff, 1", "0, 0"})
  void testGetAlphaF(String color, float expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getAlphaF(asColor)).isCloseTo(expected, Offset.offset(0.01f));
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 1", "0xbf00ff00, 0", "0x800000ff, 0", "0xffffffff, 1", "0, 0"})
  void testGetRedF(String color, float expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getRedF(asColor)).isCloseTo(expected, Offset.offset(0.01f));
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 0", "0xbf00ff00, 1", "0x800000ff, 0", "0xffffffff, 1", "0, 0"})
  void testGetGreenF(String color, float expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getGreenF(asColor)).isCloseTo(expected, Offset.offset(0.01f));
  }

  @ParameterizedTest
  @CsvSource({"0x80ff0000, 0", "0xbf00ff00, 0", "0x800000ff, 1", "0xffffffff, 1", "0, 0"})
  void testGetBlueF(String color, float expected) {
    int asColor = Long.decode(color).intValue();
    Assertions.assertThat(ColorUtil.getBlueF(asColor)).isCloseTo(expected, Offset.offset(0.01f));
  }

  @ParameterizedTest
  @CsvSource({
    "0, 100, 100, 0xffff0000",
    "28, 44, 100, 0xffFFC38F",
    "0, 0, 100, 0xffffffff",
    "0, 0, 0, 0xff000000"
  })
  void testHSBtoRGBInt(int h, int s, int b, String expected) {
    int asColor = Long.decode(expected).intValue();
    int hsb = ColorUtil.HSBtoRGB(h, s, b);

    Assertions.assertThat(hsb).isEqualTo(asColor);
  }

  @ParameterizedTest
  @CsvSource({
    "0, 1, 1, 0xffff0000",
    "0.077, 0.44, 1, 0xffFFC38F",
    "0, 0, 1, 0xffffffff",
    "0, 0, 0, 0xff000000"
  })
  void testHSBtoRGBFloat(float h, float s, float b, String expected) {
    int asColor = Long.decode(expected).intValue();
    int hsb = ColorUtil.HSBtoRGB(h, s, b);

    Assertions.assertThat(hsb).isEqualTo(asColor);
  }
}
