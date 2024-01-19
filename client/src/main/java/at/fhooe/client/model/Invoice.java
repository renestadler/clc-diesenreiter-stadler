package at.fhooe.client.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private long id;
    private long customerId;
    private String address;
    private InvoiceEntry[] entries;
}
