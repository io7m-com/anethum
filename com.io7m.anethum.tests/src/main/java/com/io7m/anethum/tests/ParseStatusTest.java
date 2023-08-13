/*
 * Copyright © 2023 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.anethum.tests;

import com.io7m.anethum.api.ParseSeverity;
import com.io7m.anethum.api.ParseStatus;
import com.io7m.anethum.slf4j.ParseStatusLogging;
import com.io7m.jlexing.core.LexicalPosition;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParseStatusTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(ParseStatusTest.class);

  @Provide
  public Arbitrary<Throwable> throwables()
  {
    return Arbitraries.strings()
      .map(IOException::new);
  }

  /**
   * Mutable builders work as expected.
   *
   * @param errorCode   The error code
   * @param message1    A message
   * @param message2    A message
   * @param action      An action
   * @param attributes1 A set of attributes
   * @param exception   An exception
   */

  @Property
  public void testBuilder(
    final @ForAll String errorCode,
    final @ForAll String message1,
    final @ForAll String message2,
    final @ForAll String action,
    final @ForAll Map<String, String> attributes1,
    final @ForAll Map<String, String> attributes2,
    final @ForAll("throwables") Throwable exception)
  {
    var builder =
      ParseStatus.builder(errorCode, message1);

    for (final var entry : attributes1.entrySet()) {
      builder = builder.withAttribute(entry.getKey(), entry.getValue());
    }

    builder = builder.withAttributes(attributes2);
    builder = builder.withMessage(message2);
    builder = builder.withException(exception);
    builder = builder.withRemediatingAction(action);

    final var all =
      Stream.concat(
          attributes1.entrySet().stream(),
          attributes2.entrySet().stream())
        .collect(Collectors.toMap(
          Map.Entry::getKey,
          Map.Entry::getValue,
          (s0, s1) -> s1));

    final var error = builder.build();
    assertEquals(message2, error.message());
    assertEquals(errorCode, error.errorCode());
    assertEquals(all, error.attributes());
    assertEquals(exception, error.exception().orElseThrow());
    assertEquals(action, error.remediatingAction().orElseThrow());
  }

  /**
   * Logging works as expected.
   *
   * @param severity  The parse severity
   * @param errorCode The error code
   * @param message   A message
   */

  @Property
  public void testLoggingMinimal(
    final @ForAll ParseSeverity severity,
    final @ForAll String errorCode,
    final @ForAll String message,
    final @ForAll int line)
  {
    ParseStatusLogging.logMinimal(
      LOG,
      ParseStatus.builder(errorCode, message)
        .withSeverity(severity)
        .withLexical(LexicalPosition.of(line, 0, Optional.empty()))
        .build()
    );
  }

  /**
   * Logging works as expected.
   *
   * @param severity  The parse severity
   * @param errorCode The error code
   * @param message   A message
   */

  @Property
  public void testLoggingWithErrorCode(
    final @ForAll ParseSeverity severity,
    final @ForAll String errorCode,
    final @ForAll String message,
    final @ForAll int line)
  {
    ParseStatusLogging.logWithErrorCode(
      LOG,
      ParseStatus.builder(errorCode, message)
        .withSeverity(severity)
        .withLexical(LexicalPosition.of(line, 0, Optional.empty()))
        .build()
    );
  }

  /**
   * Logging works as expected.
   *
   * @param severity  The parse severity
   * @param errorCode The error code
   * @param message   A message
   */

  @Property
  public void testLoggingWithAll(
    final @ForAll ParseSeverity severity,
    final @ForAll String errorCode,
    final @ForAll String message,
    final @ForAll int line,
    final @ForAll Map<String, String> attributes)
  {
    ParseStatusLogging.logWithAll(
      LOG,
      ParseStatus.builder(errorCode, message)
        .withSeverity(severity)
        .withLexical(LexicalPosition.of(line, 0, Optional.empty()))
        .withAttributes(attributes)
        .build()
    );
  }
}
