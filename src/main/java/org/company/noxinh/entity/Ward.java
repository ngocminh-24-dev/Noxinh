package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


/**
 * The type Ward.
 */
@Getter
@Setter
@Entity
@Table(name = "[ward]")
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}