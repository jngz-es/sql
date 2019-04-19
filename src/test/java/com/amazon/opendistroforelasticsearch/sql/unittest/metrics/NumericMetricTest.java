/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package com.amazon.opendistroforelasticsearch.sql.unittest.metrics;

import com.amazon.opendistroforelasticsearch.sql.metrics.BasicCounter;
import com.amazon.opendistroforelasticsearch.sql.metrics.NumericMetric;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class NumericMetricTest {

    @Test
    public void increment() {
        NumericMetric metric = new NumericMetric("test", new BasicCounter());
        for (int i=0; i<5; ++i) {
            metric.increment();
        }

        assertThat(metric.getValue(), equalTo(5L));
    }

    @Test
    public void incrementN() {
        NumericMetric metric = new NumericMetric("test", new BasicCounter());
        metric.increment(5);

        assertThat(metric.getValue(), equalTo(5L));
    }

}
