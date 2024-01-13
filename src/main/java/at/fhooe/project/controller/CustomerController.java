package at.fhooe.project.controller;

import at.fhooe.project.config.KafkaProperties;
import at.fhooe.project.model.Customer;
import at.fhooe.project.util.ModelConverter;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final ReadOnlyKeyValueStore<Long, byte[]> customerStore;
    private final KafkaTemplate<byte[], byte[]> template;
    private final KafkaProperties properties;

    public CustomerController(KafkaTemplate<byte[], byte[]> template, KafkaStreams streams, KafkaProperties properties) {
        this.template = template;
        this.properties = properties;
        this.customerStore = streams.store(StoreQueryParameters.fromNameAndType(properties.getCustomerStore(), QueryableStoreTypes.keyValueStore()));
    }

    @PostMapping
    public void addOrUpdateCustomer(@RequestBody Customer customer) {

        KeyValue<Long, byte[]> pair = ModelConverter.fromCustomer(customer);
        template.send(properties.getCustomerTopic(),
                ModelConverter.toByteArray(pair.key),
                pair.value
        );
        LOGGER.info("Added or updated customer: {}", customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable long id) {
        byte[] bytes = customerStore.get(id);
        if (bytes != null) {
            Customer customer = ModelConverter.toCustomer(KeyValue.pair(id, bytes));
            LOGGER.info("Retrieved customer: {}", customer);
            return customer;
        }
        return null;
    }

    @GetMapping("/all")
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        try (var a = customerStore.all()) {
            a.forEachRemaining(keyValue ->
                    customers.add(ModelConverter.toCustomer(KeyValue.pair(keyValue.key, keyValue.value))));
        }
        LOGGER.info("Retrieve all customers");
        return customers;
    }
}
