package com.openk9.plugins.exampledatasource.driver;

import com.openk9.datasource.model.Datasource;
import com.openk9.ingestion.driver.manager.api.PluginDriver;
import com.openk9.ingestion.logic.api.IngestionLogic;
import com.openk9.json.api.ArrayNode;
import com.openk9.json.api.JsonFactory;
import com.openk9.json.api.JsonNode;
import com.openk9.model.IngestionPayload;
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

		for (int i = 0; i < 5; i++) {
			String id = "example-" + i;
			JsonNode data = _jsonFactory.createObjectNode()
							.put("title", "Item " + i)
							.put("description", "This is a test, you are looking for item number " + i);

			_ingestionLogicSender.send(
				IngestionPayload
					.builder()
					.datasourceId(datasource.getDatasourceId())
					.rawContent(id)
					.contentId(id)
					.tenantId(datasource.getTenantId())
					.datasourcePayload(
						_jsonFactory
							.createObjectNode()
							.set(getName(), data.toObjectNode())
							.toMap()
					)
					.parsingDate(toDate.getTime())
					.type(new String[] {getName()})
					.build()
			);
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
