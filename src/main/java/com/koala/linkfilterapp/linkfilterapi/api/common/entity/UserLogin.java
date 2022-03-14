package com.koala.linkfilterapp.linkfilterapi.api.common.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "users")
@Entity
@Data
public class UserLogin {
    @Id
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    String role;

    @Column(name = "enabled")
    Byte enabled;
}
