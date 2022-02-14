package io.openk9.plugins.exampledatasource.enrichprocessor;

import io.openk9.search.enrich.api.AsyncEnrichProcessor;
import io.openk9.search.enrich.api.EnrichProcessor;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = EnrichProcessor.class)
public class ExampleAsyncEnrichProcessor implements AsyncEnrichProcessor {
    @Override
    public String destinationName() {
        return "io.openk9.ner";
    }

    @Override
    public String name() {
        return AsyncExampleNerEnrichProcessor.class.getName();
    }

    @Override
    public boolean validate(IngestionPayload ingestionPayload) {

        String rawContent = ingestionPayload.getRawContent();
        return rawContent.length() > 0;
    }
}
