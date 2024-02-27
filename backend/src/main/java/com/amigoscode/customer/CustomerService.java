package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
       return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //check if email exist
        if (customerDao.existPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("email already taken");
        }
        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());
        customerDao.insertCustomer(customer);
    }

    public void removeCustomer(Integer id) {
        //check if customer exist
        if (!customerDao.existCustomerWithId(id)) {
            throw new ResourceNotFoundException("unable to remove, customer with id [%s] doesn't exist".formatted(id));
        }
        //delete
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(CustomerUpdateRequest updateRequest, Integer customerId) {
        Customer customer = getCustomer(customerId);

        boolean change = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            change = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(updateRequest.email());
            change = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            change = true;
        }

        if (!change) throw new RequestValidationException("there's no changes");

        customerDao.updateCustomer(customer);
    }

}