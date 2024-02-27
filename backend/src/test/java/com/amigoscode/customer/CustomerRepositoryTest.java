package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {


    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.save(customer);

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.MALE);

        underTest.save(customer);

        Integer id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {

        Integer id = 0;

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }
}