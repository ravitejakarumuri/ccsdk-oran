/*-
 * ========================LICENSE_START=================================
 * ONAP : ccsdk oran
 * ======================================================================
 * Copyright (C) 2024 OpenInfra Foundation Europe. All rights reserved.
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

package org.onap.ccsdk.oran.a1policymanagementservice.database;

import org.onap.ccsdk.oran.a1policymanagementservice.configuration.ApplicationConfig;
import org.onap.ccsdk.oran.a1policymanagementservice.repository.Policies;
import org.onap.ccsdk.oran.a1policymanagementservice.repository.PolicyTypes;
import org.onap.ccsdk.oran.a1policymanagementservice.repository.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-enabled", havingValue = "true")
public class DatabaseDependentBeanFactory {
    @Bean
    @DependsOn({ "springContextProvider", "flywayInitializer" })
    public Services getServices(@Autowired ApplicationConfig applicationConfig) {
        Services services = new Services(applicationConfig);
        services.restoreFromDatabase().subscribe();
        return services;
    }

    @Bean
    @DependsOn({ "springContextProvider", "flywayInitializer" })
    public PolicyTypes getPolicyTypes(@Autowired ApplicationConfig applicationConfig) {
        PolicyTypes types = new PolicyTypes(applicationConfig);
        types.restoreFromDatabase().subscribe();
        return types;
    }

    @Bean
    @DependsOn({ "springContextProvider", "flywayInitializer" })
    public Policies getPolicies(@Autowired ApplicationConfig applicationConfig) {
        return new Policies(applicationConfig);
    }
}
