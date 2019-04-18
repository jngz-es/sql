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

package com.amazon.opendistroforelasticsearch.sql.metrics;

import com.amazon.opendistroforelasticsearch.sql.query.join.BackOffRetryStrategy;

public class MetricFactory {

    public static Metric createMetric(MetricType type) {
        if (type.getType() == 1) {
            return new NumericMetric<>(type.getName(), new RollingCounter());
        } else if (type == MetricType.CIRCUIT_BREAKER) {
            return new GaugeMetric<>(type.getName(), 0, BackOffRetryStrategy.GET_CB_STATE);
        } else {
            return new NumericMetric<>(type.getName(), new BasicCounter());
        }
    }
}
