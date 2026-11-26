package pl.vistula.firstrestapi.customer;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository customers;

    public CustomerController(CustomerRepository customers) {
        this.customers = customers;
    }

    @GetMapping
    public List<Customer> list(@RequestParam(defaultValue = "") String name) {
        String term = name.strip();
        if (term.isEmpty()) {
            return customers.findAll(Sort.by("id"));
        }
        return customers.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrderById(term, term);
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable long id) {
        return customers.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer " + id + " was not found"));
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerRequest request) {
        Customer customer = new Customer();
        customer.update(request);
        customers.save(customer);
        return ResponseEntity.created(URI.create("/api/customers/" + customer.getId())).body(customer);
    }

    @PutMapping("/{id}")
    @Transactional
    public Customer update(@PathVariable long id, @Valid @RequestBody CustomerRequest request) {
        Customer customer = get(id);
        customer.update(request);
        return customer;
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        customers.delete(get(id));
    }
}
