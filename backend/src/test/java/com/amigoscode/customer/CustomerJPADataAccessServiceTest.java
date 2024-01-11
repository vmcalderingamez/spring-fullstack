package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        Integer id = 1;

        underTest.selectCustomerById(id);

        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 2
        );

        underTest.insertCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
        String email = "foo@gmail.com";

        underTest.existPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        Integer id = 1;

        underTest.deleteCustomer(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void existCustomerWithId() {
        Integer id = 0;

        underTest.existCustomerWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 2
        );

        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);
    }
}