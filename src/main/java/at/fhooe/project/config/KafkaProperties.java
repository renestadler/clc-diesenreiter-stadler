package at.fhooe.project.config;

import at.fhooe.project.util.KafkaSettingConstants;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

@ConfigurationProperties(prefix = "kafka")
@SuppressWarnings("unused")
public class KafkaProperties {

    private final Properties properties = new Properties();

    public KafkaProperties() {

        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "data-processing-service");
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");

        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 50 * 1024 * 1024);

        properties.put(StreamsConfig.POLL_MS_CONFIG, 100);
        properties.put(StreamsConfig.TOPOLOGY_OPTIMIZATION_CONFIG, "all");
        properties.put(StreamsConfig.STATESTORE_CACHE_MAX_BYTES_CONFIG, 1024 * 1024 * 1024);
        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4);

        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 100);

        properties.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ByteArrayDeserializer.class);
        properties.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ByteArrayDeserializer.class);
        properties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class);
        properties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class);
        properties.put(
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                Serdes.ByteArraySerde.class);
        properties.put(
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                Serdes.ByteArraySerde.class);

        properties.put(KafkaSettingConstants.INVOICE_DETAIL_TOPIC, "invoice-audit-log-v1");
        properties.put(KafkaSettingConstants.INVOICE_TOPIC, "invoice-topic-v1");
        properties.put(KafkaSettingConstants.CUSTOMER_TOPIC, "customer-topic-v1");
        properties.put(KafkaSettingConstants.ARTICLE_TOPIC, "article-topic-v1");
        properties.put(KafkaSettingConstants.CUSTOMER_STORE, "customer-store-v1");
        properties.put(KafkaSettingConstants.ARTICLE_STORE, "article-store-v1");
    }

    public String getInvoiceDetailTopic() {
        return properties.getProperty(KafkaSettingConstants.INVOICE_DETAIL_TOPIC);
    }

    public void setInvoiceDetailTopic(String invoiceDetailTopic) {
        setOrRemoveProperty(KafkaSettingConstants.INVOICE_DETAIL_TOPIC, invoiceDetailTopic);
    }

    public String getInvoiceTopic() {
        return properties.getProperty(KafkaSettingConstants.INVOICE_TOPIC);
    }

    public void setInvoiceTopic(String invoiceTopic) {
        setOrRemoveProperty(KafkaSettingConstants.INVOICE_TOPIC, invoiceTopic);
    }

    public String getCustomerTopic() {
        return properties.getProperty(KafkaSettingConstants.CUSTOMER_TOPIC);
    }

    public void setCustomerTopic(String customerTopic) {
        setOrRemoveProperty(KafkaSettingConstants.CUSTOMER_TOPIC, customerTopic);
    }

    public String getArticleTopic() {
        return properties.getProperty(KafkaSettingConstants.ARTICLE_TOPIC);
    }

    public void setArticleTopic(String articleTopic) {
        setOrRemoveProperty(KafkaSettingConstants.ARTICLE_TOPIC, articleTopic);
    }

    public String getCustomerStore() {
        return properties.getProperty(KafkaSettingConstants.CUSTOMER_STORE);
    }

    public void setCustomerStore(String customerStore) {
        setOrRemoveProperty(KafkaSettingConstants.CUSTOMER_STORE, customerStore);
    }

    public String getArticleStore() {
        return properties.getProperty(KafkaSettingConstants.ARTICLE_STORE);
    }

    public void setArticleStore(String articleStore) {
        setOrRemoveProperty(KafkaSettingConstants.ARTICLE_STORE, articleStore);
    }


    public String getBootstrapServers() {
        return properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
    }

    public void setBootstrapServers(String bootstrapServers) {
        setOrRemoveProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    }

    public String getApplicationId() {
        return properties.getProperty(StreamsConfig.APPLICATION_ID_CONFIG);
    }

    public void setApplicationId(String applicationId) {
        setOrRemoveProperty(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
    }

    private void setOrRemoveProperty(String key, String value) {
        if (ObjectUtils.isEmpty(value)) {
            properties.remove(key);
        } else {
            properties.setProperty(key, value);
        }
    }

    public Properties toProperties() {
        return properties;
    }
}
