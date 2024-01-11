package com.amigoscode.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void removeCustomer(@PathVariable("id") Integer id) {
        customerService.removeCustomer(id);
    }

    @PutMapping("{id}")
    public void modifyCustomer(@RequestBody CustomerUpdateRequest request, @PathVariable("id") Integer id) {
        customerService.updateCustomer(request,id);
    }
}
