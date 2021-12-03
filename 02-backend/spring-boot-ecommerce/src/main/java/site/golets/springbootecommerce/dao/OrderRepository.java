package site.golets.springbootecommerce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import site.golets.springbootecommerce.entity.Order;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByCustomerEmailOrderByDateCreatedDesc(@Param("email") String email, Pageable pageable);

}
