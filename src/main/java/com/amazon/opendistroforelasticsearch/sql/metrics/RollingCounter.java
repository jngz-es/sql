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

import java.time.Clock;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAdder;

public class RollingCounter implements Counter<Long> {

    private final long capacity;
    private final long window;
    private final long interval;
    private final Clock clock;
    private final ConcurrentSkipListMap<Long, Long> time2CountWin;
    private final LongAdder count;

    RollingCounter() {
        this.window = 3600;
        this.interval = 60;
        clock = Clock.systemDefaultZone();
        time2CountWin = new ConcurrentSkipListMap<>();
        count = new LongAdder();
        capacity = window / interval * 2;
    }

    RollingCounter(long window, long interval) {
        this.window = window;
        this.interval = interval;
        clock = Clock.systemDefaultZone();
        time2CountWin = new ConcurrentSkipListMap<>();
        count = new LongAdder();
        capacity = window / interval * 2;
    }

    @Override
    public void increment() {
        trim();
        time2CountWin.compute(getKey(clock.millis()), (k, v) -> (v == null) ? 1 : v+1);
    }

    @Override
    public void increment(long n) {
        trim();
        time2CountWin.compute(getKey(clock.millis()), (k, v) -> (v == null) ? 1 : v+n);
    }

    @Override
    public Long getValue() {
        return getValue(getPreKey(clock.millis()));
    }

    public long getValue(long key) {
        return time2CountWin.get(key);
    }

    public long getSum() {
        return count.longValue();
    }

    private void trim() {
        if (time2CountWin.size() > capacity) {
            time2CountWin.headMap(getKey(clock.millis() - window)).clear();
        }
    }

    private long getKey(long millis) {
        return millis / 1000 / 60;
    }

    private long getPreKey(long millis) {
        return getKey(millis) - 1;
    }

    public int size() {
        return time2CountWin.size();
    }

}
