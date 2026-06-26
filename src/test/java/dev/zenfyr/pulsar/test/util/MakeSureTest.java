package dev.zenfyr.pulsar.test.util;

import dev.zenfyr.pulsar.api.util.MakeSure;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MakeSureTest {

  private static final Object NOTNULL = new Object();
  private static final String MSG = "assert failed";

  @Test
  void testNotNull() {
    Assertions.assertThat(MakeSure.notNull(NOTNULL)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.notNull(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void testNotNullMsg() {
    Assertions.assertThat(MakeSure.notNull(NOTNULL, MSG)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.notNull(null, MSG))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotNullSupplier() {
    Assertions.assertThat(MakeSure.notNull(NOTNULL, Object::new)).isEqualTo(NOTNULL);
    Assertions.assertThat(MakeSure.notNull(null, () -> NOTNULL)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.notNull(null, () -> null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void testNotNullSupplierMsg() {
    Assertions.assertThat(MakeSure.notNull(NOTNULL, Object::new, MSG)).isEqualTo(NOTNULL);
    Assertions.assertThat(MakeSure.notNull(null, () -> NOTNULL, MSG)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.notNull(null, () -> null, MSG))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotNulls() {
    Assertions.assertThatCode(() -> MakeSure.notNulls(NOTNULL, MSG, new Object()))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notNulls((Object[]) null))
        .isInstanceOf(NullPointerException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notNulls(NOTNULL, MSG, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void testNotNullsMsg() {
    Assertions.assertThatCode(() -> MakeSure.notNulls(MSG, NOTNULL, MSG, new Object()))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notNulls(MSG, (Object[]) null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notNulls(MSG, NOTNULL, MSG, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(MSG);
  }

  @Test
  void testIsTrue() {
    Assertions.assertThatCode(() -> MakeSure.isTrue(true)).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.isTrue(false))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testIsTrueMsg() {
    Assertions.assertThatCode(() -> MakeSure.isTrue(true, MSG)).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.isTrue(false, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testIsTruePredicate() {
    Assertions.assertThat(MakeSure.isTrue(NOTNULL, o -> o == NOTNULL)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.isTrue(NOTNULL, o -> o != NOTNULL))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testIsTruePredicateMsg() {
    Assertions.assertThat(MakeSure.isTrue(NOTNULL, o -> o == NOTNULL, MSG)).isEqualTo(NOTNULL);
    Assertions.assertThatThrownBy(() -> MakeSure.isTrue(NOTNULL, o -> o != NOTNULL, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotEmptyArray() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(new Object[] {MSG}))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Object[]) null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(new Object[0]))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testNotEmptyArrayMsg() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(new Object[] {MSG}, MSG))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Object[]) null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(new Object[0], MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotEmptyCollection() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(List.of(MSG))).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Collection<?>) null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(List.of()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testNotEmptyCollectionMsg() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(List.of(MSG), MSG))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Collection<?>) null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(List.of(), MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotEmptyMap() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(Map.of(MSG, NOTNULL)))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Map<?, ?>) null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(Map.of()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testNotEmptyMapMsg() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(Map.of(MSG, NOTNULL), MSG))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((Map<?, ?>) null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(Map.of(), MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotEmptyString() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(MSG)).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((String) null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testNotEmptyStringMsg() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(MSG, MSG)).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty((String) null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty("", MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void testNotEmptyStringSupplier() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(MSG, () -> null)).doesNotThrowAnyException();
    Assertions.assertThatCode(() -> MakeSure.notEmpty(null, () -> MSG)).doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(null, () -> null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty("", () -> null))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(null, () -> ""))
        .isInstanceOf(IllegalArgumentException.class);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty("", () -> ""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testNotEmptyStringSupplierMsg() {
    Assertions.assertThatCode(() -> MakeSure.notEmpty(MSG, () -> null, MSG))
        .doesNotThrowAnyException();
    Assertions.assertThatCode(() -> MakeSure.notEmpty(null, () -> MSG, MSG))
        .doesNotThrowAnyException();
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(null, () -> null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty("", () -> null, MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty(null, () -> "", MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
    Assertions.assertThatThrownBy(() -> MakeSure.notEmpty("", () -> "", MSG))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG);
  }

  @Test
  void isFalse() {}

  @Test
  void testIsFalse() {}

  @Test
  void testIsFalse1() {}

  @Test
  void testIsFalse2() {}
}
