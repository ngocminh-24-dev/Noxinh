package org.company.noxinh.dto.category.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.entity.Category;

/**
 * The type Category response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponseDTO {
    private int id;
    private String name;

    /**
     * Instantiates a new Category response dto.
     *
     * @param category the category
     */
    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
