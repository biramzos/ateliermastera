package com.mastera.atelier.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "usrs")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private String phone;
    @Column
    private String username;
    @Column
    private String password;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;
    @Column
    private String role;

    public User(){}

    public User(String firstname, String lastname, String phone, String username, String password, byte[] image, String role){
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.image = image;
        this.role = role;
    }
}
