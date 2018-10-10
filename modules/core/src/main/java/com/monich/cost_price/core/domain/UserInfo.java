package com.monich.cost_price.core.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "userInfo")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "surname", nullable = false, length = 20)
    private String surname;

    @Column(name = "birth_date", nullable = false, updatable = false)
    private Date birthDate;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;


}
