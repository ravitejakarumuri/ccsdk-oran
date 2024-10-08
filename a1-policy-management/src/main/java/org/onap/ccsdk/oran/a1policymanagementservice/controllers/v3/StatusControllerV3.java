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

package org.onap.ccsdk.oran.a1policymanagementservice.controllers.v3;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.onap.ccsdk.oran.a1policymanagementservice.controllers.api.v3.HealthCheckApi;
import org.onap.ccsdk.oran.a1policymanagementservice.controllers.v2.Consts;
import org.onap.ccsdk.oran.a1policymanagementservice.controllers.v2.StatusController;
import org.onap.ccsdk.oran.a1policymanagementservice.mappers.v3.StatusControllerMapper;
import org.onap.ccsdk.oran.a1policymanagementservice.models.v3.StatusInfo;
import org.onap.ccsdk.oran.a1policymanagementservice.service.v3.ErrorHandlingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController("statusControllerV3")
@RequiredArgsConstructor
@Tag( //
        name = StatusControllerV3.API_NAME, //
        description = StatusControllerV3.API_DESCRIPTION //
)
@RequestMapping(Consts.V3_API_ROOT)
public class StatusControllerV3 implements HealthCheckApi {

    public static final String API_NAME = "Health Check";
    public static final String API_DESCRIPTION = "API used to get the health status and statistics of this service";


    private final StatusController statusController;

    private final StatusControllerMapper statusControllerMapper;

    private final ErrorHandlingService errorHandlingService;

    @Override
    public Mono<ResponseEntity<StatusInfo>> getStatus(ServerWebExchange exchange) throws Exception {
        return statusController.getStatus(exchange)
                .map(statusInfoResponseEntity -> new ResponseEntity<>(statusControllerMapper.toStatusInfoV3
                        (statusInfoResponseEntity.getBody()), statusInfoResponseEntity.getStatusCode()))
                .doOnError(errorHandlingService::handleError);
    }
}
