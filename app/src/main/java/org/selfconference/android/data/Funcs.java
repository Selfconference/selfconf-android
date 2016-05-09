package org.selfconference.android.data;

import rx.functions.Func1;

public final class Funcs {

  public static <T> Func1<T, T> identity() {
    return value -> value;
  }

  public static <T> Func1<T, Boolean> not(final Func1<T, Boolean> func) {
    return value -> !func.call(value);
  }

  private Funcs() {
    throw new AssertionError("No instances.");
  }
}
