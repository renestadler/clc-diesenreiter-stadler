package at.fhooe.project.model;

import static java.util.Objects.requireNonNull;

public record Invoice(long id, long customerId, String address, InvoiceEntry[] entries) {

    public Invoice {
        requireNonNull(address);
        requireNonNull(entries);
    }

    public record InvoiceEntry(long articleId, long amount) {
    }
}
