package at.fhooe.project.model;

import java.util.Arrays;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public record InvoiceDetail(long id, Customer customer, String address, InvoiceDetailEntry[] entries,
                            double totalAmount) {

    public InvoiceDetail {
        requireNonNull(customer);
        requireNonNull(address);
        requireNonNull(entries);
    }

    public record InvoiceDetailEntry(Article article, long amount) {

        @Override
        public String toString() {
            return new StringJoiner(", ")
                    .add("article=" + article)
                    .add("amount=" + amount)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("id=" + id)
                .add("customer=" + customer)
                .add("address='" + address + "'")
                .add("entries=" + Arrays.toString(entries))
                .add("totalAmount=" + totalAmount)
                .toString();
    }
}
