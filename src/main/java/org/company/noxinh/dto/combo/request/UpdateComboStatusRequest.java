package org.company.noxinh.dto.combo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.ComboStatus;

@Getter
@Setter
@NoArgsConstructor
public class UpdateComboStatusRequest {
    private ComboStatus comboStatus;
}
