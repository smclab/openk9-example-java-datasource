package com.openk9.plugins.exampledatasource.driver;

import it.rios.ingestion.driver.manager.api.DocumentType;
import it.rios.ingestion.driver.manager.api.DocumentTypeFactory;
import it.rios.ingestion.driver.manager.api.DocumentTypeFactoryRegistry;
import it.rios.ingestion.driver.manager.api.DocumentTypeFactoryRegistryAware;
import it.rios.ingestion.driver.manager.api.PluginDriver;
import it.rios.ingestion.driver.manager.api.SearchKeyword;
import it.rios.projectq.osgi.util.AutoCloseables;
import it.rios.projectq.search.client.api.mapping.Field;
import it.rios.projectq.search.client.api.mapping.FieldType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static it.rios.projectq.common.api.collection.Collections.ofList;
import static it.rios.projectq.common.api.collection.Collections.ofMap;

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
							ofList(
								SearchKeyword.text("title", pluginDriverName),
								SearchKeyword.boostText("applicationName", pluginDriverName, 10.0f),
								SearchKeyword.text("URL", pluginDriverName),
								SearchKeyword.text("description", pluginDriverName)
							)
						)
						.sourceFields(
							ofList(
								Field.of("title", FieldType.TEXT),
								Field.of("applicationName", FieldType.TEXT),
								Field.of("URL", FieldType.TEXT),
								Field.of("description", FieldType.TEXT),
								Field.of(
									"icon",
									FieldType.TEXT,
									ofMap("index", false)
								)
							)
						)
						.build())
			);
	}

	@Reference(
		target = "(component.name=it.rios.projectq.plugins.applications.driver.ExamplePluginDriver)"
	)
	private PluginDriver _pluginDriver;

}
