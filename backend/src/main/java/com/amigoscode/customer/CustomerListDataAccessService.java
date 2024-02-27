package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                22,
                Gender.MALE);
        customers.add(alex);

        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                19,
                Gender.MALE);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream().filter(customer -> customer.getId() == customerId)
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customers.stream().filter(c -> c.getId() == customerId)
                .findFirst().ifPresent(customers::remove);
    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId() == id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

}
