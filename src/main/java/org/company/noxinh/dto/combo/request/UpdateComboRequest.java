package org.company.noxinh.dto.combo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateComboRequest {
    private String name;
    private String description;
    private String color;
    private String image;
}
