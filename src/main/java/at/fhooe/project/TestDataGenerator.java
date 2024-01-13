package at.fhooe.project;

import at.fhooe.project.config.KafkaProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class TestDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final VarHandle LONG_HANDLE =
            MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);

    private static final Random RANDOM = new Random(5);
    private static final KafkaProperties KAFKA_PROPERTIES = new KafkaProperties();
    private static final KafkaTemplate<byte[], byte[]> KAFKA_TEMPLATE = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(
            Map.of(
                    ProducerConfig.ACKS_CONFIG, "all",
                    ProducerConfig.LINGER_MS_CONFIG, 100,
                    ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd",
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PROPERTIES.getBootstrapServers(),
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class
            )));

    private static final long ENTITY_COUNT = 10_000_000;
    private static final int ENTITY_TYPE_COUNT = 20;
    private static final int TENANT_COUNT = 100;
    private static final int PARTITIONS = 8;

    public static void main(String[] args) {
        /*
        ingestCustomers();
        ingestArticles();
        ingestInvoices();
    }

    public static void ingestResurrection() {
        LOGGER.info("Starting ingestion of resurrection data");
        byte[][] tenants = generateTenants();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            byte[] tenantType = tenants[i % TENANT_COUNT];
            long entityId = RANDOM.nextLong(Long.MAX_VALUE - 10000000) + 10000000;
            long entityType = RANDOM.nextLong(ENTITY_TYPE_COUNT) + 10000000;
            KAFKA_TEMPLATE.send(KAFKA_PROPERTIES.getResurrectionTopic(),
                    (int) (((long) LONG_HANDLE.get(tenantType, 0)) % PARTITIONS),
                    getKey(tenantType, entityId, entityType),
                    new byte[0]);
        }

        LOGGER.info("Finished ingestion of resurrection data");
        KAFKA_TEMPLATE.flush();
    }

    public static void ingestArchivation() {
        LOGGER.info("Starting ingestion of archivation data");
        byte[][] tenants = generateTenants();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            byte[] tenantType = tenants[i % TENANT_COUNT];
            long entityId = RANDOM.nextLong(Long.MAX_VALUE - 10000000) + 10000000;
            long entityType = RANDOM.nextLong(ENTITY_TYPE_COUNT) + 10000000;
            KAFKA_TEMPLATE.send(KAFKA_PROPERTIES.getInputTopic(),
                    (int) (((long) LONG_HANDLE.get(tenantType, 0)) % PARTITIONS),
                    getKey(tenantType,
                            entityId,
                            entityType),
                    ("0000000000000000").getBytes(StandardCharsets.UTF_8));
            //LOGGER.info("{} - {} - {}", Arrays.toString(tenantType), entityId, entityType);
        }
        LOGGER.info("Finished ingestion of archivation data");
        KAFKA_TEMPLATE.flush();
    }

    private static byte[][] generateTenants() {
        byte[][] tenants = new byte[TENANT_COUNT][];
        for (int i = 0; i < TENANT_COUNT; i++) {
            tenants[i] = new byte[8];
            for (int j = 0; j < 3; j++) {
                tenants[i][j] = (byte) RANDOM.nextInt('a', 'z' + 1);
            }
            for (int j = 3; j < 8; j++) {
                tenants[i][j] = (byte) RANDOM.nextInt('0', '9' + 1);
            }
        }
        return tenants;
    }

    private static byte[] getKey(byte[] key, long id, long type) {
        byte[] bytes = new byte[24];
        System.arraycopy(key, 0, bytes, 0, 8);
        LONG_HANDLE.set(bytes, 8, id);
        LONG_HANDLE.set(bytes, 16, type);
        return bytes;*/
    }
}
