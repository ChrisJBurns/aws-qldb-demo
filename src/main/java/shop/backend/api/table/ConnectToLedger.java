/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: MIT-0
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package shop.backend.api.table;

import com.amazonaws.services.qldbsession.AmazonQLDBSessionClientBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.qldb.PooledQldbDriver;

@Slf4j
@NoArgsConstructor
public class ConnectToLedger {
    public String ledgerName;
    private PooledQldbDriver driver;

    private static final Integer RETRY_LIMIT = 4;

    private ConnectToLedger(final String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public static ConnectToLedger withName(final String ledgerName) {
        return new ConnectToLedger(ledgerName);
    }

    /**
     * Create a pooled driver for creating sessions.
     *
     * @return The pooled driver for creating sessions.
     */
    public PooledQldbDriver createQldbDriver() {
        return PooledQldbDriver.builder()
                .withLedger(ledgerName)
                .withRetryLimit(RETRY_LIMIT)
                .withSessionClientBuilder(AmazonQLDBSessionClientBuilder.standard())
                .build();
    }

    /**
     * Create a pooled driver for creating sessions.
     *
     * @return The pooled driver for creating sessions.
     */
    public PooledQldbDriver getDriver() {
        if (driver == null) {
            driver = createQldbDriver();
        }
        return driver;
    }
}
