package com.servicehub.crm.vendor.entity;


import com.servicehub.common.entity.BaseEntity;
import com.servicehub.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vendor")
@Getter @Setter
@NoArgsConstructor
public class Vendor extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(unique = true, length = 150)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CUSTOMER;

    @Column(nullable = false)
    private boolean active = true;

}
