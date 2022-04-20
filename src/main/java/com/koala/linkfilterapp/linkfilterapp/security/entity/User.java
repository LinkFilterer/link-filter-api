package com.koala.linkfilterapp.linkfilterapp.security.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    Long id;

    @Column(name = "PROVIDER_USER_ID")
    String providerUserId;

    String email;

    String ipAddress;

    @Column(name = "enabled", columnDefinition = "BIT", length = 1)
    boolean enabled;

    @Column(name = "DISPLAY_NAME")
    String displayName;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    String password;

    String provider;

    // bi-directional many-to-many association to Role
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    Set<Role> roles;
}
