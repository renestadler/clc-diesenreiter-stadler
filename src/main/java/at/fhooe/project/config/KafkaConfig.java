package at.fhooe.project.config;

import at.fhooe.project.api.InvoiceProcessorFactory;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.Properties;

@EnableKafka
@Configuration
@Import(KafkaProperties.class)
public class KafkaConfig {

    @Bean
    public InvoiceProcessorFactory invoiceProcessorFactory() {
        return InvoiceProcessorFactory.createFor();
    }

    @Bean
    public Topology topology(InvoiceProcessorFactory invoiceProcessorFactory,
                             KafkaProperties kafkaProperties) {
        return new TopologyFactory(kafkaProperties, invoiceProcessorFactory)
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public KafkaStreams kafkaStreams(Topology topology, KafkaProperties kafkaProperties) {
        return new KafkaStreams(topology, kafkaProperties.toProperties());
    }

    @Bean
    public ProducerFactory<byte[], byte[]> producerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>((Map<String, Object>) (Map) kafkaProperties.toProperties());
    }

    @Bean
    public KafkaTemplate<byte[], byte[]> kafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }
}