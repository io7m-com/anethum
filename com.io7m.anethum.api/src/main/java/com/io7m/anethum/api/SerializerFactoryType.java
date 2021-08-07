/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.anethum.api;

import com.io7m.anethum.common.SerializeException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A factory of serializers. Only the {@link #createSerializerWithContext(Object, URI, OutputStream)}
 * method is necessarily implemented by providers: All other methods are merely
 * convenience methods built atop this method.
 *
 * @param <C> The type of serializer-specific context values
 * @param <T> The type of serialized values
 * @param <S> The precise type of serializers
 */

@FunctionalInterface
public interface SerializerFactoryType<C, T, S extends SerializerType<T>>
{
  /**
   * Create a new serializer.
   *
   * @param context The serializer-specific context value, if required
   * @param target  The output target
   * @param stream  The output stream
   *
   * @return A new serializer
   */

  S createSerializerWithContext(
    C context,
    URI target,
    OutputStream stream
  );

  /**
   * Create a new serializer.
   *
   * @param target The output source
   * @param stream The output stream
   *
   * @return A new serializer
   */

  default S createSerializer(
    final URI target,
    final OutputStream stream)
  {
    Objects.requireNonNull(target, "source");
    Objects.requireNonNull(stream, "stream");

    return this.createSerializerWithContext(
      null,
      target,
      stream
    );
  }

  /**
   * Create a new serializer for the given file.
   *
   * @param file    The file
   * @param context The serializer-specific context value, if required
   *
   * @return A new serializer
   *
   * @throws IOException On I/O errors
   */

  default S createSerializerForFileWithContext(
    final C context,
    final Path file)
    throws IOException
  {
    Objects.requireNonNull(file, "file");

    final var stream = Files.newOutputStream(file);
    return this.createSerializerWithContext(
      context,
      file.toUri(),
      stream
    );
  }

  /**
   * Create a new serializer for the given file.
   *
   * @param file The file
   *
   * @return A new serializer
   *
   * @throws IOException On I/O errors
   */

  default S createSerializer(
    final Path file)
    throws IOException
  {
    Objects.requireNonNull(file, "file");

    return this.createSerializerForFileWithContext(
      null,
      file
    );
  }

  /**
   * Execute a serializer for the given file.
   *
   * @param file    The file
   * @param context The serializer-specific context value, if required
   * @param value   The value
   *
   * @throws IOException        On I/O errors
   * @throws SerializeException On serialization errors
   */

  default void serializeFileWithContext(
    final C context,
    final Path file,
    final T value)
    throws IOException, SerializeException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(value, "value");

    try (var serializer =
           this.createSerializerForFileWithContext(context, file)) {
      serializer.execute(value);
    }
  }

  /**
   * Execute a serializer for the given file.
   *
   * @param file  The file
   * @param value The value
   *
   * @throws IOException        On I/O errors
   * @throws SerializeException On serialization errors
   */

  default void serializeFile(
    final Path file,
    final T value)
    throws IOException, SerializeException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(value, "value");

    this.serializeFileWithContext(null, file, value);
  }

  /**
   * Execute a serializer for the given value.
   *
   * @param source The source
   * @param stream The stream
   * @param value  The value
   *
   * @throws SerializeException On serialization errors
   */

  default void serialize(
    final URI source,
    final OutputStream stream,
    final T value)
    throws SerializeException
  {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(stream, "stream");
    Objects.requireNonNull(value, "value");

    this.createSerializer(source, stream).execute(value);
  }
}
