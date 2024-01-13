package at.fhooe.project.config;

import at.fhooe.project.api.InvoiceProcessorFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

public class TopologyFactory {

    private final KafkaProperties kafkaProperties;
    private final InvoiceProcessorFactory invoiceProcessorFactory;

    public TopologyFactory(KafkaProperties kafkaProperties, InvoiceProcessorFactory invoiceProcessorFactory) {
        this.kafkaProperties = kafkaProperties;
        this.invoiceProcessorFactory = invoiceProcessorFactory;
    }

    public Topology build() {
        StreamsBuilder builder = new StreamsBuilder();
        addInvoiceHandling(builder);
        return builder.build();
    }

    private void addInvoiceHandling(StreamsBuilder builder) {
        GlobalKTable<Long, byte[]> customerTable = builder.globalTable(kafkaProperties.getCustomerTopic(), Consumed.with(Serdes.Long(), Serdes.ByteArray()),
                Materialized.as(kafkaProperties.getCustomerStore()));
        GlobalKTable<Long, byte[]> articleTable = builder.globalTable(kafkaProperties.getArticleTopic(), Consumed.with(Serdes.Long(), Serdes.ByteArray()),
                Materialized.as(kafkaProperties.getArticleStore()));

        builder.stream(kafkaProperties.getInvoiceTopic(), Consumed.with(Serdes.ByteArray(), Serdes.ByteArray()).withName("invoice-handling"))
                .process(() -> invoiceProcessorFactory.forStore(articleTable.queryableStoreName(), customerTable.queryableStoreName()),
                        Named.as("invoice-processor"))
                .to(kafkaProperties.getInvoiceDetailTopic(), Produced.with(Serdes.Void(), Serdes.String()).withName("invoice-audit-log"));
    }
}
