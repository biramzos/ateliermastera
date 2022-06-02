package com.mastera.atelier.Repositories;

import com.mastera.atelier.Models.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
    Order findOrderById(Long id);
    List<Order> findOrdersByUsername(String username);
}
