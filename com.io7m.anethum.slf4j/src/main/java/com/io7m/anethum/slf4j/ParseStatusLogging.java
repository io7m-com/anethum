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


package com.io7m.anethum.slf4j;

import com.io7m.anethum.api.ParseStatus;
import org.slf4j.Logger;

/**
 * Functions to log parse status messages.
 */

public final class ParseStatusLogging
{
  private ParseStatusLogging()
  {

  }

  /**
   * Log a status message.
   *
   * @param logger The logger
   * @param status The status
   */

  public static void logMinimal(
    final Logger logger,
    final ParseStatus status)
  {
    switch (status.severity()) {
      case PARSE_INFO -> {
        logger.info(
          "{}:{}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.message()
        );
      }
      case PARSE_WARNING -> {
        logger.warn(
          "{}:{}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.message()
        );
      }
      case PARSE_ERROR -> {
        logger.error(
          "{}:{}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.message()
        );
      }
    }
  }

  /**
   * Log a status message.
   *
   * @param logger The logger
   * @param status The status
   */

  public static void logWithErrorCode(
    final Logger logger,
    final ParseStatus status)
  {
    switch (status.severity()) {
      case PARSE_INFO -> {
        logger.info(
          "{}:{}: {}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.errorCode(),
          status.message()
        );
      }
      case PARSE_WARNING -> {
        logger.warn(
          "{}:{}: {}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.errorCode(),
          status.message()
        );
      }
      case PARSE_ERROR -> {
        logger.error(
          "{}:{}: {}: {}",
          Integer.valueOf(status.lexical().line()),
          Integer.valueOf(status.lexical().column()),
          status.errorCode(),
          status.message()
        );
      }
    }
  }
}
