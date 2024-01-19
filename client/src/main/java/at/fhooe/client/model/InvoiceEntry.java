package at.fhooe.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceEntry {
    private long articleId;
    private long amount;
}
