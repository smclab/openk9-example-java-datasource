/*
 * Copyright (c) 2020-present SMC Treviso s.r.l. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.openk9.plugins.exampledatasource.enrichprocessor;

import io.openk9.json.api.JsonFactory;
import io.openk9.json.api.JsonNode;
import io.openk9.json.api.ObjectNode;
import io.openk9.model.EnrichItem;
import io.openk9.model.DatasourceContext;
import io.openk9.http.client.HttpClient;
import io.openk9.http.client.HttpClientFactory;
import io.openk9.http.web.HttpHandler;
import io.openk9.plugin.driver.manager.model.PluginDriverDTO;
import io.openk9.search.enrich.api.EnrichProcessor;
import io.openk9.search.enrich.api.SyncEnrichProcessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component(immediate = true, service = EnrichProcessor.class)
public class ExampleSyncEnrichProcessor implements SyncEnrichProcessor {

    @interface Config {
        String url() default "http://example-parser/";
        String path() default "/predict";
        int method() default HttpHandler.POST;
        String[] headers() default "Content-Type:application/json";
    }

    @Activate
    public void activate(Config config) {
        _config = config;
        _httpClient = _httpClientFactory.getHttpClient(_config.url());
    }

    @Modified
    public void modified(Config config) {
        activate(config);
    }

    @Override
    public Mono<ObjectNode> process(
            ObjectNode objectNode, DatasourceContext datasourceContext,
            EnrichItem enrichItem, PluginDriverDTO pluginDriverDTO) {

        Map<String, Object> headers = Arrays
                .stream(_config.headers())
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(e -> e[0], e -> e[1]));

        String jsonConfig = enrichItem.getJsonConfig();

        ObjectNode datasourceConfiguration =
                _jsonFactory.fromJsonToJsonNode(jsonConfig).toObjectNode();

        JsonNode rawContentNode = objectNode.get("rawContent");

        JsonNode confidenceNode = datasourceConfiguration.get("confidence");

        ObjectNode request = _jsonFactory.createObjectNode();

        request.put("confidence", confidenceNode);

        request.put("content", rawContentNode);

        return Mono.from(
                        _httpClient.request(
                                _config.method(),
                                _config.path(),
                                request.toString(),
                                headers))
                .map(_jsonFactory::fromJsonToJsonNode)
                .map(JsonNode::toObjectNode)
                .map(objectNode::merge);

    }

    @Override
    public String name() {
        return SpacesTypeEnrichProcessor.class.getName();
    }

    private HttpClient _httpClient;

    private Config _config;

    @Reference
    private HttpClientFactory _httpClientFactory;

    @Reference
    private JsonFactory _jsonFactory;

}