package at.fhooe.project.util;

import at.fhooe.project.model.Article;
import at.fhooe.project.model.Customer;
import at.fhooe.project.model.Invoice;
import org.apache.kafka.streams.KeyValue;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

import static java.util.Objects.requireNonNull;

public class ModelConverter {

    private static final VarHandle DOUBLE_HANDLE =
            MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LONG_HANDLE =
            MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle ID_HANDLE =
            MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.BIG_ENDIAN);
    private static final VarHandle INT_HANDLE =
            MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);

    public static KeyValue<Long, byte[]> fromArticle(Article article) {
        requireNonNull(article);
        int nameLength = article.name().length();
        byte[] value = new byte[8 + 4 + nameLength];
        DOUBLE_HANDLE.set(value, KafkaOffsets.Article.PRICE_OFFSET, article.price());
        //Name
        INT_HANDLE.set(value, KafkaOffsets.Article.NAME_LENGTH_OFFSET, nameLength);
        byte[] byteRep = article.name().getBytes();
        System.arraycopy(byteRep, 0, value, KafkaOffsets.Article.NAME_OFFSET, nameLength);
        return KeyValue.pair(article.id(), value);
    }

    public static Article toArticle(KeyValue<Long, byte[]> pair) {
        requireNonNull(pair);
        double value = (double) DOUBLE_HANDLE.get(pair.value, KafkaOffsets.Article.PRICE_OFFSET);
        int nameLength = (int) INT_HANDLE.get(pair.value, KafkaOffsets.Article.NAME_LENGTH_OFFSET);
        byte[] name = new byte[nameLength];
        System.arraycopy(pair.value, KafkaOffsets.Article.NAME_OFFSET, name, 0, nameLength);
        return new Article(pair.key, new String(name), value);
    }

    public static KeyValue<Long, byte[]> fromCustomer(Customer customer) {
        requireNonNull(customer);
        int nameLength = customer.name().length();
        int addressLength = customer.address().length();
        byte[] value = new byte[4 + 4 + nameLength + addressLength];

        //Name
        INT_HANDLE.set(value, KafkaOffsets.Customer.NAME_LENGTH_OFFSET, nameLength);
        byte[] byteRep = customer.name().getBytes();
        System.arraycopy(byteRep, 0, value, KafkaOffsets.Customer.NAME_OFFSET, nameLength);

        int offsetStart = KafkaOffsets.Customer.NAME_OFFSET + nameLength;
        //Address
        INT_HANDLE.set(value, offsetStart + KafkaOffsets.Customer.ADDRESS_LENGTH_OFFSET, addressLength);
        byteRep = customer.address().getBytes();
        System.arraycopy(byteRep, 0, value, offsetStart + KafkaOffsets.Customer.ADDRESS_OFFSET, addressLength);

        return KeyValue.pair(customer.id(), value);
    }

    public static Customer toCustomer(KeyValue<Long, byte[]> pair) {
        requireNonNull(pair);
        int nameLength = (int) INT_HANDLE.get(pair.value, KafkaOffsets.Customer.NAME_LENGTH_OFFSET);
        byte[] name = new byte[nameLength];
        System.arraycopy(pair.value, KafkaOffsets.Customer.NAME_OFFSET, name, 0, nameLength);

        int offsetStart = KafkaOffsets.Customer.NAME_OFFSET + nameLength;
        int addressLength = (int) INT_HANDLE.get(pair.value, offsetStart + KafkaOffsets.Customer.ADDRESS_LENGTH_OFFSET);
        byte[] address = new byte[addressLength];
        System.arraycopy(pair.value, offsetStart + KafkaOffsets.Customer.NAME_OFFSET, address, 0, nameLength);
        return new Customer(pair.key, new String(name), new String(address));
    }

    public static KeyValue<byte[], byte[]> fromInvoice(Invoice invoice) {
        requireNonNull(invoice);
        int addressLength = invoice.address().length();
        int entriesLength = invoice.entries().length;
        byte[] key = new byte[8 + 8];
        byte[] value = new byte[4 + 4 + addressLength + 16 * entriesLength];

        LONG_HANDLE.set(key, KafkaOffsets.Invoice.CUSTOMER_ID_OFFSET, invoice.customerId());
        LONG_HANDLE.set(key, KafkaOffsets.Invoice.INVOICE_ID_OFFSET, invoice.id());

        //Address
        INT_HANDLE.set(value, KafkaOffsets.Invoice.ADDRESS_LENGTH_OFFSET, addressLength);
        byte[] byteRep = invoice.address().getBytes();
        System.arraycopy(byteRep, 0, value, KafkaOffsets.Invoice.ADDRESS_OFFSET, addressLength);


        int offsetStart = KafkaOffsets.Invoice.ADDRESS_OFFSET + addressLength;
        //Entries
        INT_HANDLE.set(value, offsetStart + KafkaOffsets.Invoice.ENTRIES_LENGTH_OFFSET, entriesLength);
        offsetStart += 4;
        for (Invoice.InvoiceEntry entry : invoice.entries()) {
            LONG_HANDLE.set(value, offsetStart + KafkaOffsets.Invoice.ARTICLE_ID_OFFSET, entry.articleId());
            LONG_HANDLE.set(value, offsetStart + KafkaOffsets.Invoice.AMOUNT_OFFSET, entry.amount());
            offsetStart += 16;
        }

        return KeyValue.pair(key, value);
    }

    public static Invoice toInvoice(KeyValue<byte[], byte[]> pair) {
        requireNonNull(pair);
        long invoiceId = (long) LONG_HANDLE.get(pair.key, KafkaOffsets.Invoice.INVOICE_ID_OFFSET);
        long customerId = (long) LONG_HANDLE.get(pair.key, KafkaOffsets.Invoice.CUSTOMER_ID_OFFSET);

        //Address
        int addressLength = (int) INT_HANDLE.get(pair.value, KafkaOffsets.Invoice.ADDRESS_LENGTH_OFFSET);
        byte[] address = new byte[addressLength];
        System.arraycopy(pair.value, KafkaOffsets.Invoice.ADDRESS_OFFSET, address, 0, addressLength);

        int offsetStart = KafkaOffsets.Invoice.ADDRESS_OFFSET + addressLength;
        int entriesLength = (int) INT_HANDLE.get(pair.value, offsetStart + KafkaOffsets.Invoice.ENTRIES_LENGTH_OFFSET);
        offsetStart += 4;
        Invoice.InvoiceEntry[] entries = new Invoice.InvoiceEntry[entriesLength];
        for (int i = 0; i < entriesLength; i++) {
            entries[i] = new Invoice.InvoiceEntry(
                    (long) LONG_HANDLE.get(pair.value, offsetStart + KafkaOffsets.Invoice.ARTICLE_ID_OFFSET),
                    (long) LONG_HANDLE.get(pair.value, offsetStart + KafkaOffsets.Invoice.AMOUNT_OFFSET)
            );
            offsetStart += 16;
        }
        return new Invoice(invoiceId, customerId, new String(address), entries);
    }

    public static byte[] toByteArray(long value) {
        byte[] array = new byte[8];
        ID_HANDLE.set(array, 0, value);
        return array;
    }
}
