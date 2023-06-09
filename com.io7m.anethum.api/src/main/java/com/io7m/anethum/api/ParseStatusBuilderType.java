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
import org.osgi.annotation.versioning.ConsumerType;

import java.net.URI;
import java.util.Map;

/**
 * The type of mutable builders that can construct parse status values.
 */

@ConsumerType
public interface ParseStatusBuilderType
{
  /**
   * Update the severity.
   *
   * @param severity The new severity
   *
   * @return this
   */

  ParseStatusBuilderType withSeverity(
    ParseSeverity severity);

  /**
   * Update the lexical information.
   *
   * @param lexical The new lexical information
   *
   * @return this
   */

  ParseStatusBuilderType withLexical(
    LexicalPosition<URI> lexical);

  /**
   * Update the message.
   *
   * @param message The new message
   *
   * @return this
   */

  ParseStatusBuilderType withMessage(
    String message);

  /**
   * Add an attribute.
   *
   * @param name  The attribute name
   * @param value The attribute value
   *
   * @return this
   */

  ParseStatusBuilderType withAttribute(
    String name,
    String value);

  /**
   * Add all the given attributes.
   *
   * @param attributes The attributes
   *
   * @return this
   */

  default ParseStatusBuilderType withAttributes(
    final Map<String, String> attributes)
  {
    for (final var e : attributes.entrySet()) {
      this.withAttribute(e.getKey(), e.getValue());
    }
    return this;
  }

  /**
   * Update the remediating action.
   *
   * @param action The new action
   *
   * @return this
   */

  ParseStatusBuilderType withRemediatingAction(
    String action);

  /**
   * Update the exception.
   *
   * @param exception The new exception
   *
   * @return this
   */

  ParseStatusBuilderType withException(
    Throwable exception);

  /**
   * @return An immutable parse status based on the information so far
   */

  ParseStatus build();
}
