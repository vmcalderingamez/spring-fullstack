package com.amigoscode.customer;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);
    void deleteCustomer(Integer customerId);
    boolean existCustomerWithId(Integer id);
    void updateCustomer(Customer customer);

}
