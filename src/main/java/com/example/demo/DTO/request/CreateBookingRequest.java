package com.example.demo.DTO.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotBlank
    private Integer tripId, customerId;

    @NotBlank
    private String totalAmount;

    @NotBlank
    private Integer passengerCount;

    @NotEmpty
    private List<String> passengerNames;
}
