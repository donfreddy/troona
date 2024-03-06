// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/donfreddy/troona/data/dark_theme_config.proto

// Protobuf Java Version: 3.25.1
package com.donfreddy.troona.core.datastore;

/**
 * Protobuf enum {@code DarkThemeConfigProto}
 */
public enum DarkThemeConfigProto
    implements com.google.protobuf.Internal.EnumLite {
  /**
   * <code>DARK_THEME_CONFIG_FOLLOW_SYSTEM = 0;</code>
   */
  DARK_THEME_CONFIG_FOLLOW_SYSTEM(0),
  /**
   * <code>DARK_THEME_CONFIG_LIGHT = 1;</code>
   */
  DARK_THEME_CONFIG_LIGHT(1),
  /**
   * <code>DARK_THEME_CONFIG_DARK = 2;</code>
   */
  DARK_THEME_CONFIG_DARK(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>DARK_THEME_CONFIG_FOLLOW_SYSTEM = 0;</code>
   */
  public static final int DARK_THEME_CONFIG_FOLLOW_SYSTEM_VALUE = 0;
  /**
   * <code>DARK_THEME_CONFIG_LIGHT = 1;</code>
   */
  public static final int DARK_THEME_CONFIG_LIGHT_VALUE = 1;
  /**
   * <code>DARK_THEME_CONFIG_DARK = 2;</code>
   */
  public static final int DARK_THEME_CONFIG_DARK_VALUE = 2;


  @java.lang.Override
  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The number of the enum to look for.
   * @return The enum associated with the given number.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static DarkThemeConfigProto valueOf(int value) {
    return forNumber(value);
  }

  public static DarkThemeConfigProto forNumber(int value) {
    switch (value) {
      case 0: return DARK_THEME_CONFIG_FOLLOW_SYSTEM;
      case 1: return DARK_THEME_CONFIG_LIGHT;
      case 2: return DARK_THEME_CONFIG_DARK;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<DarkThemeConfigProto>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      DarkThemeConfigProto> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<DarkThemeConfigProto>() {
          @java.lang.Override
          public DarkThemeConfigProto findValueByNumber(int number) {
            return DarkThemeConfigProto.forNumber(number);
          }
        };

  public static com.google.protobuf.Internal.EnumVerifier 
      internalGetVerifier() {
    return DarkThemeConfigProtoVerifier.INSTANCE;
  }

  private static final class DarkThemeConfigProtoVerifier implements 
       com.google.protobuf.Internal.EnumVerifier { 
          static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new DarkThemeConfigProtoVerifier();
          @java.lang.Override
          public boolean isInRange(int number) {
            return DarkThemeConfigProto.forNumber(number) != null;
          }
        };

  private final int value;

  private DarkThemeConfigProto(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:DarkThemeConfigProto)
}

