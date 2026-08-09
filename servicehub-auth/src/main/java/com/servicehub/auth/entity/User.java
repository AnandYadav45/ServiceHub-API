package com.servicehub.auth.entity;

import com.servicehub.common.entity.BaseEntity;
import com.servicehub.common.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Setter @Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @NotNull(message = "Name can't be null")
    @Column(name = "name",length = 150, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    @Column(name = "email", length = 50, nullable = true,unique = true)
    private String email;

    @Column(name = "contact_number", length = 20, unique = true)
    private String contact;

    @Column(name = "password", length = 255 )
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive= true;



}
