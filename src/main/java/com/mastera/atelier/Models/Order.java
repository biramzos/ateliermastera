package com.mastera.atelier.Models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String username;

    public Order(){}

    public Order(String name,String phone, String username){
        this.name = name;
        this.phone = phone;
        this.username = username;
    }
}
