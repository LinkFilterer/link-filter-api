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
    @Column(name = "user_id", length = 32)
    Integer userId;

    @Column(name = "username", length = 32)
    String username;

    @Column(name = "password", length = 64)
    String password;

    @Column(name = "role", length = 16)
    String role;

    @Column(name = "enabled")
    Byte enabled;
}
