package com.auth1.auth.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseModel {

    private String value;
    @OneToOne
    private User user;
    private Date expireAt;
    private boolean deleted;

}
