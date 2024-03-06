// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/donfreddy/troona/data/sort_by.proto

// Protobuf Java Version: 3.25.1
package com.donfreddy.troona.core.datastore;

/**
 * Protobuf enum {@code GenreSortByProto}
 */
public enum GenreSortByProto
    implements com.google.protobuf.Internal.EnumLite {
  /**
   * <code>GENRE_SORT_BY_GENRE = 0;</code>
   */
  GENRE_SORT_BY_GENRE(0),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>GENRE_SORT_BY_GENRE = 0;</code>
   */
  public static final int GENRE_SORT_BY_GENRE_VALUE = 0;


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
  public static GenreSortByProto valueOf(int value) {
    return forNumber(value);
  }

  public static GenreSortByProto forNumber(int value) {
    switch (value) {
      case 0: return GENRE_SORT_BY_GENRE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<GenreSortByProto>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      GenreSortByProto> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<GenreSortByProto>() {
          @java.lang.Override
          public GenreSortByProto findValueByNumber(int number) {
            return GenreSortByProto.forNumber(number);
          }
        };

  public static com.google.protobuf.Internal.EnumVerifier 
      internalGetVerifier() {
    return GenreSortByProtoVerifier.INSTANCE;
  }

  private static final class GenreSortByProtoVerifier implements 
       com.google.protobuf.Internal.EnumVerifier { 
          static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new GenreSortByProtoVerifier();
          @java.lang.Override
          public boolean isInRange(int number) {
            return GenreSortByProto.forNumber(number) != null;
          }
        };

  private final int value;

  private GenreSortByProto(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:GenreSortByProto)
}
