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

package com.io7m.certusine.tests;

import com.io7m.certusine.vanilla.internal.dns.CSDNSQueriesType;
import com.io7m.certusine.vanilla.internal.dns.CSDNSTXTRecord;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public final class CSFakeDNSQueries implements CSDNSQueriesType
{
  public Queue<List<String>> authoritativeNameServerResponses;
  public Queue<List<CSDNSTXTRecord>> txtRecordResponses;

  public CSFakeDNSQueries()
  {
    this.authoritativeNameServerResponses =
      new ArrayDeque<>();
    this.txtRecordResponses =
      new ArrayDeque<>();
  }

  @Override
  public List<String> findAuthoritativeNameServersForDomain(
    final String domain)
    throws IOException
  {
    if (this.authoritativeNameServerResponses.isEmpty()) {
      throw new IOException("No response!");
    }
    return this.authoritativeNameServerResponses.poll();
  }

  @Override
  public List<CSDNSTXTRecord> findTXTRecordsForDomain(
    final String domain)
    throws IOException
  {
    if (this.txtRecordResponses.isEmpty()) {
      throw new IOException("No response!");
    }
    return this.txtRecordResponses.poll();
  }
}