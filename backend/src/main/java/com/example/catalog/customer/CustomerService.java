package com.example.catalog.customer;

import com.example.catalog.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());
        if (existingCustomer.isPresent()) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " already exists");
        }

        Customer newCustomer = new Customer();
        newCustomer.setName(request.getName());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setTier(request.getTier() == null ? CustomerTier.STANDARD : request.getTier());

        return customerRepository.save(newCustomer);
    }
}
