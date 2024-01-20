package at.fhooe.client;

import at.fhooe.client.client.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {
	public static Logger logger = LoggerFactory.getLogger(ClientApplication.class);

	@Autowired
	public ApiClient client;

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// args[0] = numbCustomers
		// args[1] = numbArticles
		// args[2] = numbInvoices

		if (args.length < 3) {
			logger.error("Invalid number of arguments.");
			return;
		}

		logger.info("Generating customers {}, articles {}, invoices {}", args[0], args[1], args[2]);

		client.createData(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
		logger.info("Done");
	}
}
