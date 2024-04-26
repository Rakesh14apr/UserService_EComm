package com.auth1.auth.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel {

    private String name;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;
    private boolean isEMailVerified;

    public User(){}

    public User(String name, String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;

    }
}
