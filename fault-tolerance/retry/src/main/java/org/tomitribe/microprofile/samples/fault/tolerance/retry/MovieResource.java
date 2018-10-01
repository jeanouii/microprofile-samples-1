/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tomitribe.microprofile.samples.fault.tolerance.retry;

import org.eclipse.microprofile.faulttolerance.Retry;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Path("/movies")
public class MovieResource {
    static AtomicInteger retries = new AtomicInteger(0);

    /**
     * On a BusinessException, it will retry 5 times, without delay.
     * On the last request, the exception will not be thrown and the request will succeed.
     *
     * @return The movies list
     */
    @GET
    @Retry(maxRetries = 5, retryOn = BusinessException.class)
    public List<String> findAll() {
        if (retries.getAndAdd(1) < 5) {
            System.out.println("Try " + retries);
            throw new BusinessException();
        }

        return Stream.of("The Terminator", "The Matrix", "Rambo").collect(Collectors.toList());
    }
}
