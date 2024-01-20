package at.fhooe.project.controller;

import at.fhooe.project.config.KafkaProperties;
import at.fhooe.project.model.Invoice;
import at.fhooe.project.util.ModelConverter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.streams.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    private final KafkaTemplate<byte[], byte[]> template;
    private final KafkaProperties properties;

    public InvoiceController(KafkaTemplate<byte[], byte[]> template, KafkaProperties properties) {
        this.template = template;
        this.properties = properties;
    }

    @PostMapping
    public void addOrUpdateInvoice(@RequestBody Invoice invoice) {
        KeyValue<byte[], byte[]> pair = ModelConverter.fromInvoice(invoice);
        template.send(properties.getInvoiceTopic(),
                pair.key,
                pair.value
        );
        LOGGER.info("Added or updated invoice: {}", pair.key, invoice);
    }

    @GetMapping("/audit")
    public List<String> getInvoiceForCustomer() {
        Properties props = new Properties();
        props.putAll(properties.toProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        List<String> invoices = new ArrayList<>();
        try (KafkaConsumer<Void, String> consumer = new KafkaConsumer<>(props)) {
            consumer.unsubscribe();
            List<PartitionInfo> partitionInfos = consumer.partitionsFor(properties.getInvoiceDetailTopic());
            consumer.assign(partitionInfos.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition())).toList());
            consumer.seekToBeginning(partitionInfos.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition())).toList());
            ConsumerRecords<Void, String> records = null;
            do {
                // Poll for records
                records = consumer.poll(Duration.ofMillis(100));

                // Process the records (print or handle as needed)
                records.forEach(record -> {
                    invoices.add(record.value());
                });

            } while (records.count() > 0);
        }
        LOGGER.info("Retrieve all invoices for the last seven years");
        return invoices;
    }
}
