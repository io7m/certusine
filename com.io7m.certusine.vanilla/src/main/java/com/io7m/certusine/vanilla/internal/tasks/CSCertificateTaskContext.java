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


package com.io7m.certusine.vanilla.internal.tasks;

import com.io7m.certusine.api.CSCertificate;
import com.io7m.certusine.api.CSDomain;
import com.io7m.certusine.api.CSOptions;
import com.io7m.certusine.api.CSTelemetryServiceType;
import com.io7m.certusine.vanilla.internal.CSStrings;
import com.io7m.certusine.vanilla.internal.dns.CSDNSQueriesFactoryType;
import com.io7m.certusine.vanilla.internal.dns.CSDNSTXTRecord;
import com.io7m.certusine.vanilla.internal.events.CSEventServiceType;
import com.io7m.certusine.vanilla.internal.store.CSCertificateStoreServiceType;
import com.io7m.certusine.vanilla.internal.tasks.CSCertificateTaskStatusType.CSCertificateTaskFailedPermanently;
import org.shredzone.acme4j.Problem;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The execution context for a task.
 */

public final class CSCertificateTaskContext
{
  private final CSStrings strings;
  private final CSEventServiceType events;
  private final CSTelemetryServiceType telemetry;
  private final CSOptions options;
  private final CSCertificateStoreServiceType certificateStores;
  private final Clock clock;
  private final CSDomain domain;
  private final CSCertificate certificate;
  private final int retryAttemptsMax;
  private final ArrayList<CSDNSTXTRecord> dnsRecords;
  private final CSDNSQueriesFactoryType dnsQueries;
  private boolean failed;

  /**
   * The execution context for a task.
   *
   * @param inStrings           The string resources
   * @param inEvents            The event service
   * @param inTelemetry         The telemetry service
   * @param inClock             The clock
   * @param inCertificateStores The certificate stores
   * @param inOptions           The options
   * @param inDomain            The domain
   * @param inCertificate       The certificate
   * @param inDnsQueries        The DNS query factory
   * @param inRetryAttemptsMax  The maximum number of retry attempts
   */

  public CSCertificateTaskContext(
    final CSStrings inStrings,
    final CSEventServiceType inEvents,
    final CSTelemetryServiceType inTelemetry,
    final CSOptions inOptions,
    final CSCertificateStoreServiceType inCertificateStores,
    final Clock inClock,
    final CSDomain inDomain,
    final CSCertificate inCertificate,
    final int inRetryAttemptsMax,
    final CSDNSQueriesFactoryType inDnsQueries)
  {
    this.strings =
      Objects.requireNonNull(inStrings, "inStrings");
    this.events =
      Objects.requireNonNull(inEvents, "inEvents");
    this.telemetry =
      Objects.requireNonNull(inTelemetry, "inTelemetry");
    this.options =
      Objects.requireNonNull(inOptions, "options");
    this.certificateStores =
      Objects.requireNonNull(inCertificateStores, "inCertificateStores");
    this.clock =
      Objects.requireNonNull(inClock, "inClock");
    this.domain =
      Objects.requireNonNull(inDomain, "inDomain");
    this.certificate =
      Objects.requireNonNull(inCertificate, "inCertificate");
    this.dnsQueries =
      Objects.requireNonNull(inDnsQueries, "dnsQueries");

    this.retryAttemptsMax =
      inRetryAttemptsMax;
    this.dnsRecords =
      new ArrayList<>();
  }

  /**
   * @return The telemetry service
   */

  public CSTelemetryServiceType telemetry()
  {
    return this.telemetry;
  }

  /**
   * @return The certificate store used during execution
   */

  public CSCertificateStoreServiceType certificateStores()
  {
    return this.certificateStores;
  }

  /**
   * @return The current time
   */

  public OffsetDateTime now()
  {
    return OffsetDateTime.now(this.clock);
  }

  /**
   * @return The clock used for time operations
   */

  public Clock clock()
  {
    return this.clock;
  }

  /**
   * @return A factory of DNS queries
   */

  public CSDNSQueriesFactoryType dnsQueries()
  {
    return this.dnsQueries;
  }

  /**
   * @return The configuration options
   */

  public CSOptions options()
  {
    return this.options;
  }

  /**
   * @return The string resources
   */

  public CSStrings strings()
  {
    return this.strings;
  }

  /**
   * @return The current domain
   */

  public CSDomain domain()
  {
    return this.domain;
  }

  /**
   * @return The current certificate being processed
   */

  public CSCertificate certificate()
  {
    return this.certificate;
  }

  /**
   * @return The maximum number of times a task can be retried
   */

  public int retryAttemptsMax()
  {
    return this.retryAttemptsMax;
  }

  /**
   * A DNS record was created.
   *
   * @param name The record name
   * @param text The record text
   */

  public void dnsRecordCreated(
    final String name,
    final String text)
  {
    this.dnsRecords.add(new CSDNSTXTRecord(name, text));
  }

  /**
   * @return The list of DNS TXT records created during execution
   */

  public List<CSDNSTXTRecord> dnsRecordsCreated()
  {
    return List.copyOf(this.dnsRecords);
  }

  /**
   * @param retryAttempts The number of times a task has been retried
   *
   * @return {@code true} if the given number of retry attempts exceeds the
   * allowed number
   */

  public boolean retryAttemptsExhausted(
    final int retryAttempts)
  {
    return retryAttempts > this.retryAttemptsMax;
  }

  /**
   * Format a problem report as text.
   *
   * @param error The report
   *
   * @return The formatted report
   */

  public String formatProblem(
    final Problem error)
  {
    if (error == null) {
      return this.strings().format("errorReportUnavailable");
    }

    final var stringBuilder = new StringBuilder(128);
    stringBuilder.append(error.getTitle());
    stringBuilder.append(": ");
    stringBuilder.append(error.getDetail());
    stringBuilder.append(System.lineSeparator());
    for (final var problem : error.getSubProblems()) {
      stringBuilder.append(this.formatProblem(problem));
    }
    return stringBuilder.toString();
  }

  /**
   * @return The event service
   */

  public CSEventServiceType events()
  {
    return this.events;
  }

  /**
   * @return {@code true} if a task associated with this context has permanently failed
   */

  public boolean isFailed()
  {
    return this.failed;
  }

  /**
   * Mark this context as permanently failed due to the given exception,
   * and return a failed permanently status.
   *
   * @param exception The exception
   *
   * @return The failed status
   */

  public CSCertificateTaskFailedPermanently failedPermanently(
    final Exception exception)
  {
    this.failed = true;
    return new CSCertificateTaskFailedPermanently(exception);
  }
}
