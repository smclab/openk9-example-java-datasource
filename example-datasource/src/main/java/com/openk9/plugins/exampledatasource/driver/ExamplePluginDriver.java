package com.openk9.plugins.exampledatasource.driver;

import it.rios.ingestion.driver.manager.api.PluginDriver;
import it.rios.projectq.datasource.model.Datasource;
import it.rios.projectq.ingestion.logic.api.IngestionLogic;
import it.rios.projectq.json.api.ArrayNode;
import it.rios.projectq.json.api.JsonFactory;
import it.rios.projectq.json.api.JsonNode;
import it.rios.projectq.model.IngestionPayload;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component(
	immediate = true,
	service = PluginDriver.class
)
public class ExamplePluginDriver implements PluginDriver {

	@interface Config {
		boolean schedulerEnabled() default false;
	}

	@Activate
	public void activate(Config config) {
		_config = config;
	}

	@Modified
	public void modified(Config config) {
		_config = config;
	}

	@Override
	public String getName() {
		return "example";
	}

	@Override
	public Publisher<Void> invokeDataParser(
		Datasource datasource, Date fromDate, Date toDate) {

		JsonNode jsonNode =
			_jsonFactory.fromJsonToJsonNode(datasource.getJsonConfig());

		if (jsonNode.isArray()) {
			ArrayNode arrayJson = jsonNode.toArrayNode();

			for (int i = 0; i < arrayJson.size(); i++) {

				JsonNode node = arrayJson.get(i);

				if (node.hasNonNull("title")) {
					node = node
						.toObjectNode()
						.set("applicationName", node.get("title"));
				}

				_ingestionLogicSender.send(
					IngestionPayload
						.builder()
						.datasourceId(datasource.getDatasourceId())
						.rawContent(node.toString())
						.contentId(Integer.toString(i))
						.tenantId(datasource.getTenantId())
						.datasourcePayload(
							_jsonFactory
								.createObjectNode()
								.set(getName(), node.toObjectNode())
								.toMap()
						)
						.parsingDate(toDate.getTime())
						.type(new String[] {getName()})
						.build()
				);

			}

		}

		return Mono.empty();

	}

	@Override
	public boolean schedulerEnabled() {
		return _config.schedulerEnabled();
	}

	@Reference
	private IngestionLogic _ingestionLogicSender;

	@Reference
	private JsonFactory _jsonFactory;

	private Config _config;



}
