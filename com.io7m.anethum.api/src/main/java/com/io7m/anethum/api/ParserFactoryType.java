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
import java.util.function.Consumer;

/**
 * A factory of parsers.
 *
 * @param <C> The type of parser-specific context values
 * @param <T> The type of parsed values
 */

public interface ParserFactoryType<C, T>
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

  ParserType<T> createParser(
    C context,
    URI source,
    InputStream stream,
    Consumer<ParseStatus> statusConsumer
  );

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

  default ParserType<T> createParser(
    final C context,
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException
  {
    final var stream = Files.newInputStream(file);
    return this.createParser(context, file.toUri(), stream, statusConsumer);
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

  default T parse(
    final C context,
    final Path file,
    final Consumer<ParseStatus> statusConsumer)
    throws IOException, ParseException
  {
    try (var parser = this.createParser(context, file, statusConsumer)) {
      return parser.execute();
    }
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

  default T parse(
    final C context,
    final Path file)
    throws IOException, ParseException
  {
    final Consumer<ParseStatus> statusConsumer =
      parseStatus -> {

      };

    try (var parser = this.createParser(context, file, statusConsumer)) {
      return parser.execute();
    }
  }
}
