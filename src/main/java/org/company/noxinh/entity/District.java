package org.company.noxinh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The type District.
 */
@Getter
@Setter
@Entity
@Table(name = "district")
public class District {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}