package com.springboot.obbm.dto.contract.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.ingredient.response.IngredientResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockRequestForContractResponse {
    Integer stockrequestId;
    Integer quantity;
    IngredientResponse ingredients;
    String approval;
    Date requestdate;
    String status;
}