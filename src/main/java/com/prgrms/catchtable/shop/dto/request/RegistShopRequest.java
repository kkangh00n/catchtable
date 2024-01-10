package com.prgrms.catchtable.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record RegistShopRequest(

    String name,
    @Min(0)
    @Max(5)
    BigDecimal rating,
    String category,
    String city,
    String district,
    int capacity,
    @JsonFormat(pattern = "kk:mm:ss")
    LocalTime openingTime,
    @JsonFormat(pattern = "kk:mm:ss")
    LocalTime closingTime

) {

}
