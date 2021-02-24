package com.openk9.plugins.exampledatasource.driver;

import com.openk9.ingestion.driver.manager.api.DocumentType;
import com.openk9.ingestion.driver.manager.api.DocumentTypeFactory;
import com.openk9.ingestion.driver.manager.api.DocumentTypeFactoryRegistry;
import com.openk9.ingestion.driver.manager.api.DocumentTypeFactoryRegistryAware;
import com.openk9.ingestion.driver.manager.api.PluginDriver;
import com.openk9.ingestion.driver.manager.api.SearchKeyword;
import com.openk9.osgi.util.AutoCloseables;
import com.openk9.search.client.api.mapping.Field;
import com.openk9.search.client.api.mapping.FieldType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Map;


@Component(
	immediate = true,
	service = DocumentTypeFactoryRegistryAware.class
)
public class DocumentTypeDefinition implements
	DocumentTypeFactoryRegistryAware {

	@Override
	public AutoCloseables.AutoCloseableSafe apply(
		DocumentTypeFactoryRegistry documentTypeFactoryRegistry) {

		String pluginDriverName = _pluginDriver.getName();

		return documentTypeFactoryRegistry
			.register(
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, true,
					DocumentType
						.builder()
						.name(pluginDriverName)
						.searchKeywords(
							List.of(
								SearchKeyword.text("title", pluginDriverName),
								SearchKeyword.boostText(
									"applicationName", pluginDriverName, 10.0f),
								SearchKeyword.text("URL", pluginDriverName),
								SearchKeyword.text(
									"description", pluginDriverName)
							)
						)
						.sourceFields(
							List.of(
								Field.of("title", FieldType.TEXT),
								Field.of("applicationName", FieldType.TEXT),
								Field.of("URL", FieldType.TEXT),
								Field.of("description", FieldType.TEXT),
								Field.of(
									"icon",
									FieldType.TEXT,
									Map.of("index", false)
								)
							)
						)
						.build())
			);
	}

	@Reference(
		target = "(component.name=com.openk9.plugins.exampledatasource.driver.ExamplePluginDriver)"
	)
	private PluginDriver _pluginDriver;

}
