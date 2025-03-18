package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.ComboStatus;
import org.company.noxinh.dto.combo.request.CreateComboRequest;

@Getter
@Setter
@Entity
@Table(name = "combo")
@NoArgsConstructor
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "status", nullable = false, length = 20)
    private ComboStatus status;

    public Combo(CreateComboRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.image = request.getImage();
        this.status = ComboStatus.INACTIVE;
        this.color = request.getColor();
    }
}