/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.certusine.certstore.api;

import com.io7m.certusine.api.CSTelemetryServiceType;
import com.io7m.repetoir.core.RPServiceType;
import org.osgi.annotation.versioning.ProviderType;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The type of certificate store factories.
 */

@ProviderType
public interface CSCertificateStoreFactoryType
  extends RPServiceType
{
  /**
   * Create or open a certificate store. The certificate store is created if it
   * does not exist.
   *
   * @param telemetry The telemetry service
   * @param file      The store file
   *
   * @return A new certificate store
   *
   * @throws IOException On I/O errors
   */

  CSCertificateStoreType open(
    CSTelemetryServiceType telemetry,
    Path file)
    throws IOException;
}
