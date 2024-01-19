package at.fhooe.client.logic;

import at.fhooe.client.model.Article;
import at.fhooe.client.model.Customer;
import at.fhooe.client.model.Invoice;
import at.fhooe.client.model.InvoiceEntry;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class InvoiceService {
    int MIN_ENTRIES = 1; int MAX_ENTRIES = 10;
    int MIN_ARTICLES = 1; int MAX_ARTICLES = 25;

    public List<Invoice> generateRandomInvoices(int count, List<Article> articles, List<Customer> customers) {
        Faker faker = new Faker();

        List<Invoice> invoices = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Invoice invoice = new Invoice();
            invoice.setId(i);

            Customer randomCustomer = getRandomElement(customers);
            invoice.setCustomerId(randomCustomer.getId());
            invoice.setAddress(randomCustomer.getAddress());

            int numbEntries = getRandomNumberInRange(MIN_ENTRIES, MAX_ENTRIES);
            InvoiceEntry[] entries = new InvoiceEntry[numbEntries];
            for (int j = 0; j < numbEntries; j++){
                Article article = getRandomElement(articles);
                entries[i] = new InvoiceEntry(article.getId(), getRandomNumberInRange(MIN_ARTICLES, MAX_ARTICLES));
            }
            invoice.setEntries(entries);

            invoices.add(invoice);
        }
        return invoices;
    }

    private <T> T getRandomElement(List<T> list) {
        int randomIndex = new Random().nextInt(list.size());
        return list.get(randomIndex);
    }
    private int getRandomNumberInRange(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

}
