/*-
 * ========================LICENSE_START=================================
 * ONAP : ccsdk oran
 * ======================================================================
 * Copyright (C) 2019-2020 Nordix Foundation. All rights reserved.
 * ======================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================LICENSE_END===================================
 */

package org.onap.ccsdk.oran.a1policymanagementservice.utils;

import static org.mockito.Mockito.spy;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.onap.ccsdk.oran.a1policymanagementservice.clients.A1Client;
import org.onap.ccsdk.oran.a1policymanagementservice.clients.A1ClientFactory;
import org.onap.ccsdk.oran.a1policymanagementservice.clients.SecurityContext;
import org.onap.ccsdk.oran.a1policymanagementservice.configuration.ApplicationConfig;
import org.onap.ccsdk.oran.a1policymanagementservice.repository.PolicyTypes;
import org.onap.ccsdk.oran.a1policymanagementservice.repository.Ric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

public class MockA1ClientFactory extends A1ClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Map<String, MockA1Client> clients = new HashMap<>();
    private PolicyTypes policyTypes;
    private Duration asynchDelay = Duration.ofSeconds(0);
    private final ApplicationConfig appConfig;

    public MockA1ClientFactory(ApplicationConfig config, PolicyTypes policyTypes) {
        super(config, new SecurityContext(""));
        this.policyTypes = policyTypes;
        this.appConfig = config;
    }

    @Override
    public Mono<A1Client> createA1Client(Ric ric) {
        return Mono.just(getOrCreateA1Client(ric.id()));
    }

    public MockA1Client getOrCreateA1Client(String ricId) {
        if (!clients.containsKey(ricId)) {
            logger.debug("Creating client for RIC: {}", ricId);
            MockA1Client client = spy(new MockA1Client(appConfig, policyTypes, asynchDelay));
            clients.put(ricId, client);
        }
        return clients.get(ricId);
    }

    public void setPolicyTypes(PolicyTypes policyTypes) {
        this.policyTypes = policyTypes;
    }

    /**
     * Simulate network latency. The REST responses will be generated by separate
     * threads
     *
     * @param delay the delay between the request and the response
     */
    public void setResponseDelay(Duration delay) {
        this.asynchDelay = delay;
    }

    public void reset() {
        this.asynchDelay = Duration.ofSeconds(0);
        clients.clear();
    }

    public PolicyTypes getPolicyTypes() {
        return this.policyTypes;
    }

}
