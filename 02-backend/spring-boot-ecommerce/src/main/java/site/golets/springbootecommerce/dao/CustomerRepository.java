package site.golets.springbootecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.golets.springbootecommerce.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer  findByEmail(String email);

}
