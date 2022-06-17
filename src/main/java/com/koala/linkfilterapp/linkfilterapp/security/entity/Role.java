package com.koala.linkfilterapp.linkfilterapp.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {
    public static final String USER = Roles.USER.toString();
    public static final String ROLE_USER = Roles.ROLE_USER.toString();
    public static final String ROLE_ADMIN = Roles.ROLE_ADMIN.toString();
    public static final String ROLE_MODERATOR = Roles.ROLE_MODERATOR.toString();
    public static final String ROLE_PARTNER = Roles.ROLE_PARTNER.toString();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    private String name;

    // bi-directional many-to-many association to User
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.DETACH)
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        return role.equals(role.name);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role [name=").append(name).append("]").append("[id=").append(roleId).append("]");
        return builder.toString();
    }
}
