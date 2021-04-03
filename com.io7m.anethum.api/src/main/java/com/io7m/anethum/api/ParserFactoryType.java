/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> http://io7m.com
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

import com.io7m.anethum.common.ParseException;
import com.io7m.anethum.common.ParseStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A factory of parsers. Only the {@link #createParserWithContext(Object, URI, InputStream, Consumer)}
 * method is necessarily implemented by providers: All other methods are merely
 * convenience methods built atop this method.
 *
 * @param <C> The type of parser-specific context values
 * @param <T> The type of parsed values
 * @param <P> The precise type of parsers
 */

@FunctionalInterface
public interface ParserFactoryType<C, T, P extends ParserType<T>>
{
  /**
   * Create a new parser.
   *
   * @param context        The parser-specific context value, if required
   * @param source         The input source
   * @param stream         The input stream
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   */

  P createParserWithContext(
    C context,
    URI source,
    InputStream stream,
    Consumer<ParseStatus> statusConsumer
  );

  /**
   * Create a new parser.
   *
   * @param source         The input source
   * @param stream         The input stream
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   */

  default P createParser(
    final URI source,
    final InputStream stream,
    final Consumer<ParseStatus> statusConsumer)
  {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(stream, "stream");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    return this.createParserWithContext(
      null,
      source,
      stream,
      statusConsumer
    );
  }

  /**
   * Create a new parser for the given file.
   *
   * @param file           The file
   * @param context        The parser-specific context value, if required
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   *
   * @throws IOException On I/O errors
   */

  default P createParserForFileWithContext(
    final C context,
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    final var stream = Files.newInputStream(file);
    return this.createParserWithContext(
      context,
      file.toUri(),
      stream,
      statusConsumer
    );
  }

  /**
   * Create a new parser for the given file.
   *
   * @param file           The file
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   *
   * @throws IOException On I/O errors
   */

  default P createParser(
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    return this.createParserForFileWithContext(
      null,
      file,
      statusConsumer
    );
  }

  /**
   * Execute a parser for the given file.
   *
   * @param file           The file
   * @param context        The parser-specific context value, if required
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   *
   * @throws IOException    On I/O errors
   * @throws ParseException On parse errors
   */

  default T parseFileWithContext(
    final C context,
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException, ParseException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    try (var parser =
           this.createParserForFileWithContext(context, file, statusConsumer)) {
      return parser.execute();
    }
  }

  /**
   * Execute a parser for the given file.
   *
   * @param file           The file
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   *
   * @throws IOException    On I/O errors
   * @throws ParseException On parse errors
   */

  default T parseFile(
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException, ParseException
  {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    return this.parseFileWithContext(null, file, statusConsumer);
  }

  /**
   * Execute a parser for the given file.
   *
   * @param file    The file
   * @param context The parser-specific context value, if required
   *
   * @return A new parser
   *
   * @throws IOException    On I/O errors
   * @throws ParseException On parse errors
   */

  default T parseFileWithContext(
    final C context,
    final Path file)
    throws IOException, ParseException
  {
    Objects.requireNonNull(file, "file");

    final Consumer<ParseStatus> statusConsumer =
      parseStatus -> {

      };

    try (var parser = this.createParserForFileWithContext(
      context, file, statusConsumer)) {
      return parser.execute();
    }
  }

  /**
   * Execute a parser for the given file.
   *
   * @param file The file
   *
   * @return A new parser
   *
   * @throws IOException    On I/O errors
   * @throws ParseException On parse errors
   */

  default T parseFile(
    final Path file)
    throws IOException, ParseException
  {
    Objects.requireNonNull(file, "file");

    return this.parseFileWithContext(null, file);
  }

  /**
   * Execute a parser for the given stream.
   *
   * @param source         The source
   * @param stream         The stream
   * @param statusConsumer A consumer of status events
   *
   * @return A new parser
   *
   * @throws ParseException On parse errors
   */

  default T parse(
    final URI source,
    final InputStream stream,
    final Consumer<ParseStatus> statusConsumer)
    throws ParseException
  {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(stream, "stream");
    Objects.requireNonNull(statusConsumer, "statusConsumer");

    return this.createParser(source, stream, statusConsumer).execute();
  }

  /**
   * Execute a parser for the given stream.
   *
   * @param source The source
   * @param stream The stream
   *
   * @return A new parser
   *
   * @throws ParseException On parse errors
   */

  default T parse(
    final URI source,
    final InputStream stream)
    throws ParseException
  {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(stream, "stream");

    final Consumer<ParseStatus> statusConsumer =
      parseStatus -> {

      };

    return this.createParser(source, stream, statusConsumer).execute();
  }
}
