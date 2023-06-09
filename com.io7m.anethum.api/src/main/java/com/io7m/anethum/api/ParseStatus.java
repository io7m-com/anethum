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


package com.io7m.anethum.api;

import com.io7m.jlexing.core.LexicalPosition;
import com.io7m.jlexing.core.LexicalPositions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * An immutable parse status value.
 *
 * @param severity          The severity
 * @param lexical           The lexical information
 * @param errorCode         The error code
 * @param message           The message
 * @param attributes        The attributes
 * @param remediatingAction The remediating action, if any
 * @param exception         The exception
 */

public record ParseStatus(
  ParseSeverity severity,
  LexicalPosition<URI> lexical,
  String errorCode,
  String message,
  Map<String, String> attributes,
  Optional<String> remediatingAction,
  Optional<Throwable> exception)
  implements ParseStatusType
{
  /**
   * An immutable parse status value.
   *
   * @param severity          The severity
   * @param lexical           The lexical information
   * @param errorCode         The error code
   * @param message           The message
   * @param attributes        The attributes
   * @param remediatingAction The remediating action, if any
   * @param exception         The exception
   */

  public ParseStatus
  {
    Objects.requireNonNull(severity, "severity");
    Objects.requireNonNull(lexical, "lexical");
    Objects.requireNonNull(errorCode, "errorCode");
    Objects.requireNonNull(message, "message");
    Objects.requireNonNull(attributes, "attributes");
    Objects.requireNonNull(remediatingAction, "remediatingAction");
    Objects.requireNonNull(exception, "exception");
  }

  /**
   * A mutable builder for status values.
   *
   * @param errorCode The error code
   * @param message   The error message
   *
   * @return A status code builder
   */

  public static ParseStatusBuilderType builder(
    final String errorCode,
    final String message)
  {
    return new Builder(errorCode, message);
  }

  private static final class Builder
    implements ParseStatusBuilderType
  {
    private final String errorCode;
    private final HashMap<String, String> attributes;
    private LexicalPosition<URI> lexical;
    private ParseSeverity severity;
    private String message;
    private Optional<String> remediatingAction;
    private Optional<Throwable> exception;

    private Builder(
      final String inErrorCode,
      final String inMessage)
    {
      this.lexical =
        LexicalPositions.zero();
      this.severity =
        ParseSeverity.PARSE_ERROR;
      this.errorCode =
        Objects.requireNonNull(inErrorCode, "errorCode");
      this.message =
        Objects.requireNonNull(inMessage, "message");
      this.attributes =
        new HashMap<>(4);
      this.remediatingAction =
        Optional.empty();
      this.exception =
        Optional.empty();
    }

    @Override
    public ParseStatusBuilderType withSeverity(
      final ParseSeverity newSeverity)
    {
      this.severity = Objects.requireNonNull(newSeverity, "newSeverity");
      return this;
    }

    @Override
    public ParseStatusBuilderType withLexical(
      final LexicalPosition<URI> newLexical)
    {
      this.lexical = Objects.requireNonNull(newLexical, "lexical");
      return this;
    }

    @Override
    public ParseStatusBuilderType withMessage(
      final String newMessage)
    {
      this.message = Objects.requireNonNull(newMessage, "message");
      return this;
    }

    @Override
    public ParseStatusBuilderType withAttribute(
      final String name,
      final String value)
    {
      this.attributes.put(
        Objects.requireNonNull(name, "name"),
        Objects.requireNonNull(value, "value")
      );
      return this;
    }

    @Override
    public ParseStatusBuilderType withRemediatingAction(
      final String newAction)
    {
      this.remediatingAction = Optional.of(newAction);
      return this;
    }

    @Override
    public ParseStatusBuilderType withException(
      final Throwable newException)
    {
      this.exception = Optional.of(newException);
      return this;
    }

    @Override
    public ParseStatus build()
    {
      return new ParseStatus(
        this.severity,
        this.lexical,
        this.errorCode,
        this.message,
        Map.copyOf(this.attributes),
        this.remediatingAction,
        this.exception
      );
    }
  }
}
