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

package com.io7m.certusine.vanilla.internal.store;

import com.io7m.certusine.certstore.api.CSCertificateStoreFactoryType;
import com.io7m.certusine.certstore.api.CSCertificateStoreType;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.tx.TransactionStore;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A certificate store based on the H2 MVStore class.
 */

public final class CSCertificateStoreH2MVFactory
  implements CSCertificateStoreFactoryType
{
  /**
   * A certificate store based on the H2 MVStore class.
   */

  public CSCertificateStoreH2MVFactory()
  {

  }

  @Override
  public CSCertificateStoreType open(
    final Path file)
    throws IOException
  {
    Objects.requireNonNull(file, "file");

    try {
      final var store =
        MVStore.open(file.toAbsolutePath().toString());
      final var txStore =
        new TransactionStore(store);

      return new CSCertificateStoreH2MV(store, txStore);
    } catch (final Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public String description()
  {
    return "H2 MVStore certificate store.";
  }

  @Override
  public String toString()
  {
    return "[CSCertificateStoreH2MVFactory 0x%s]"
      .formatted(Long.toUnsignedString(this.hashCode(), 16));
  }
}
