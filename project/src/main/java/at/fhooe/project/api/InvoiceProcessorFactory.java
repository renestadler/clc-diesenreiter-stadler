package at.fhooe.project.api;

import at.fhooe.project.logic.InvoiceProcessor;
import org.apache.kafka.streams.processor.api.Processor;

@FunctionalInterface
public interface InvoiceProcessorFactory {

    Processor<byte[], byte[], Void, String> forStore(String articleStoreName, String customerStoreName);

    static InvoiceProcessorFactory createFor() {
        return InvoiceProcessor::new;
    }

}
