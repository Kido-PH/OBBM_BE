package com.springboot.obbm.dto.dish.request;

import com.springboot.obbm.validator.price.VietnamCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRequest {
    @NotBlank(message = "FIELD_NOT_BLANK")
    @Size(max = 100, message = "FIELD_MAXVALID")
    private String name;

    @VietnamCurrency(message = "PRICE_NOT_VALID")
    private double price;

    private String image;

    @NotBlank(message = "FIELD_NOT_BLANK")
    @Size(max = 250, message = "FIELD_MAXVALID")
    private String description;

    @NotBlank(message = "FIELD_NOT_BLANK")
    @Size(max = 10, message = "FIELD_MAXVALID")
    private String existing;

    @Min(value = 1, message = "FIELD_ID_NOT_VALID")
    private int categoryId;
}
