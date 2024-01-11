package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerDao customerDao;
    CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        Integer id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 2
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(10);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        Integer id = 10;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {

        String email = "alex@gmail.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowWhenEmailExistWhileAddingACustomer() {

        String email = "alex@gmail.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        verify(customerDao, never()).insertCustomer(any());

    }

    @Test
    void removeCustomer() {
        Integer id = 10;

        when(customerDao.existCustomerWithId(id)).thenReturn(true);

        underTest.removeCustomer(id);

        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenRemoveCustomerNotExists() {
        Integer id = 10;

        when(customerDao.existCustomerWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.removeCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("unable to remove, customer with id [%s] doesn't exist".formatted(id));

        verify(customerDao, never()).deleteCustomer(any());
    }

    @Test
    void canUpdateAllTheCustomerProperties() {
        Integer id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", newEmail, 23
        );

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(updateRequest, id);

        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        Integer id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", null, null
        );

        underTest.updateCustomer(updateRequest, id);

        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        Integer id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null
        );

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(updateRequest, id);

        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        Integer id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 23
        );

        underTest.updateCustomer(updateRequest, id);

        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        Integer id = 10;

        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null
        );

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(updateRequest, id))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        Integer id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        assertThatThrownBy(() -> underTest.updateCustomer(updateRequest, id))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("there's no changes");

        verify(customerDao, never()).updateCustomer(any());
    }
}