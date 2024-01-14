package at.fhooe.project.logic;

import at.fhooe.project.model.Customer;
import at.fhooe.project.model.Invoice;
import at.fhooe.project.model.InvoiceDetail;
import at.fhooe.project.util.ModelConverter;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ValueAndTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class InvoiceProcessor extends ContextualProcessor<byte[], byte[], Void, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceProcessor.class);
    private final String articleStoreName;
    private final String customerStoreName;

    private ReadOnlyKeyValueStore<Long, ValueAndTimestamp<byte[]>> articleStore;
    private ReadOnlyKeyValueStore<Long, ValueAndTimestamp<byte[]>> customerStore;

    public InvoiceProcessor(String articleStoreName, String customerStoreName) {
        this.articleStoreName = articleStoreName;
        this.customerStoreName = customerStoreName;
    }

    @Override
    public void init(ProcessorContext<Void, String> context) {
        super.init(context);
        this.articleStore = requireNonNull(context().getStateStore(articleStoreName));
        this.customerStore = requireNonNull(context().getStateStore(customerStoreName));
    }

    @Override
    public void process(Record<byte[], byte[]> record) {
        Invoice invoice = ModelConverter.toInvoice(KeyValue.pair(record.key(), record.value()));
        Customer customer = ModelConverter.toCustomer(KeyValue.pair(invoice.customerId(), customerStore.get(invoice.customerId()).value()));
        InvoiceDetail.InvoiceDetailEntry[] entries = new InvoiceDetail.InvoiceDetailEntry[invoice.entries().length];
        double totalAmount = 0.0;
        for (int i = 0; i < invoice.entries().length; i++) {
            var entry = invoice.entries()[i];
            entries[i] = new InvoiceDetail.InvoiceDetailEntry(
                    ModelConverter.toArticle(KeyValue.pair(entry.articleId(), articleStore.get(entry.articleId()).value())),
                    entry.amount());
            totalAmount += entry.amount() * entries[i].article().price();
        }
        InvoiceDetail invoiceDetail = new InvoiceDetail(invoice.id(), customer, invoice.address(), entries, totalAmount);
        LOGGER.info("Add invoice to audit log: {}", invoiceDetail);
        context().forward(new Record<>(null, invoiceDetail.toString(), context().currentSystemTimeMs()));
    }
}
