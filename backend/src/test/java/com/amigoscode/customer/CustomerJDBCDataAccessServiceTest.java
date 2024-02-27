package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();


        //Then
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {

        Integer id = 0;

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();

    }

    @Test
    void existPersonWithEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        boolean actual = underTest.existPersonWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailReturnFalseWhenDoesNotExist() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        boolean actual = underTest.existPersonWithEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomer(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void existCustomerWithId() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existCustomerWithId(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existCustomerWithIdReturnsFalseWhenIdNotPresent() {
        Integer id = 0;

        boolean actual = underTest.existCustomerWithId(id);

        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "Foo";

        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 30;

        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesForCustomer() {
        String newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String email = newEmail;
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        Customer update = new Customer();
        update.setId(id);
        update.setName("Foo");
        update.setEmail(newEmail);
        update.setAge(30);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
            assertThat(updated.getName()).isEqualTo("Foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(30);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        Customer update = new Customer();
        update.setId(id);


        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}