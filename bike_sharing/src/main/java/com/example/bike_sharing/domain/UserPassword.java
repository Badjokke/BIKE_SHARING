package com.example.bike_sharing.domain;

import jakarta.persistence.*;



@Entity
public class UserPassword {

    @Column
    private String password;
    @OneToOne
    @JoinColumn(name="id")
    private User user;
    @Id
    private Long id;

    public UserPassword(String password, User user){
        this.password = password;
        this.user = user;
    }

    public UserPassword() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser(){
        return this.user;
    }
    public String getPasswordHash(){
        return this.password;
    }


}
