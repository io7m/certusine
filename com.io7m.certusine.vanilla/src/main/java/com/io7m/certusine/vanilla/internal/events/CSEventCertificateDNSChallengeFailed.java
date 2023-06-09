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


package com.io7m.certusine.vanilla.internal.events;

import com.io7m.certusine.api.CSCertificateName;
import com.io7m.certusine.api.CSDomain;

import java.util.Map;
import java.util.Objects;

/**
 * A DNS challenge failed during a renewal attempt.
 *
 * @param domain          The domain
 * @param certificateName The certificate name
 */

public record CSEventCertificateDNSChallengeFailed(
  CSDomain domain,
  CSCertificateName certificateName)
  implements CSEventType
{
  /**
   * A DNS challenge failed during a renewal attempt.
   *
   * @param domain          The domain
   * @param certificateName The certificate name
   */

  public CSEventCertificateDNSChallengeFailed
  {
    Objects.requireNonNull(domain, "domain");
    Objects.requireNonNull(certificateName, "certificateName");
  }

  @Override
  public String message()
  {
    return "certificate DNS challenge failed";
  }

  @Override
  public boolean isFailure()
  {
    return true;
  }

  @Override
  public Map<String, String> attributes()
  {
    return Map.ofEntries(
      Map.entry("certusine.domain", this.domain.domain()),
      Map.entry("certusine.certificate", this.certificateName.value())
    );
  }
}
