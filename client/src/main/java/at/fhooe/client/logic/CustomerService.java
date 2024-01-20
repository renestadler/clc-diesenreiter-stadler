package at.fhooe.client.logic;

import at.fhooe.client.model.Customer;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CustomerService {

    public List<Customer> generateRandomCustomers(int count) {
        Faker faker = new Faker();

        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Customer customer = new Customer();
            customer.setId(new Random().nextLong());
            customer.setName(faker.name().fullName());
            customer.setAddress(faker.address().streetAddress());
            customers.add(customer);
        }
        return customers;
    }
}


