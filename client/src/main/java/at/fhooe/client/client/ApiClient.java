package at.fhooe.client.client;

import at.fhooe.client.logic.ArticleService;
import at.fhooe.client.logic.CustomerService;
import at.fhooe.client.logic.InvoiceService;
import at.fhooe.client.model.Article;
import at.fhooe.client.model.Customer;
import at.fhooe.client.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ApiClient {

    private final Logger LOG = LoggerFactory.getLogger(ApiClient.class);
    private final WebClient apiClient = WebClient.create("http://localhost/");
    private final ArticleService articleService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;

    @Autowired
    public ApiClient(ArticleService articleService, CustomerService customerService, InvoiceService invoiceService) {
        this.articleService = articleService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
    }

    public void createData(int numbCustomers, int numbArticles, int numbInvoices) {
        List<Article> articles = articleService.generateRandomArticles(numbArticles);
        createArticles(articles);
        List<Customer> customers = customerService.generateRandomCustomers(numbCustomers);
        createClients(customers);
        List<Invoice> invoices = invoiceService.generateRandomInvoices(numbInvoices, articles, customers);
        createInvoices(invoices);
    }

    private void createArticles(List<Article> articles) {
        for (Article article : articles) {
            apiClient.post()
                    .uri("/article")
                    .bodyValue(article)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            ;
        }
    }

    private void createClients(List<Customer> customers) {
        for (Customer customer : customers) {
            apiClient.post()
                    .uri("/customer")
                    .bodyValue(customer)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
    }

    public void createInvoices(List<Invoice> invoices) {
        for (Invoice invoice : invoices) {
            apiClient.post()
                    .uri("/invoice")
                    .bodyValue(invoice)
                    .retrieve()
                    .toBodilessEntity()
                    .retry()
                    .block();
            LOG.info("Added invoice {}",invoice.getId());
        }
    }


}
