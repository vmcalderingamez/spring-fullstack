package com.amigoscode.customer;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                SELECT * FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES(?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                SELECT count(*) FROM customer
                WHERE name = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, customerId);

        System.out.println("jdbcTemplate.update = " + result);

    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        var sql = """
                SELECT count(*) FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null){
            var sql = """
                UPDATE customer
                SET name = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("jdbcTemplate.update = " + result);
        }

        if (customer.getEmail() != null){
            var sql = """
                UPDATE customer
                SET email = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("jdbcTemplate.update = " + result);
        }

        if (customer.getAge() != null){
            var sql = """
                UPDATE customer
                SET age = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("jdbcTemplate.update = " + result);
        }


    }

}
