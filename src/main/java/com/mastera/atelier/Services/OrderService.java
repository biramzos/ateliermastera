package com.mastera.atelier.Services;

import com.mastera.atelier.Models.Order;
import com.mastera.atelier.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public boolean add(String name, String phone){
        if(name.isEmpty() && phone.isEmpty() || (name.isEmpty() && !phone.isEmpty()) || (!name.isEmpty() && phone.isEmpty())){
            return false;
        }
        else {
            Order order = new Order(name, phone, "None");
            orderRepository.save(order);
            return true;
        }
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUsername(String username){
        return orderRepository.findOrdersByUsername(username);
    }

    public void delete(Long id){
        Order order = orderRepository.findOrderById(id);
        orderRepository.delete(order);
    }

    public void changeUsername(Long id, String username){
        Order order = orderRepository.findOrderById(id);
        order.setUsername(username);
        orderRepository.save(order);
    }

}
