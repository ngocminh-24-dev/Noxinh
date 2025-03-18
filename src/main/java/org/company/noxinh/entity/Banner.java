package org.company.noxinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * The type Banner.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "is_display")
    private Boolean isDisplay;

    @Column(name = "create_time")
    private Instant createTime;

    public Banner(String title, String image, Boolean isDisplay, Instant createTime) {
        this.title = title;
        this.image = image;
        this.isDisplay = isDisplay;
        this.createTime = createTime;
    }
}