package com.example.demo.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T, M> {
    private Boolean success;
    private T data;
    private M metadata;
}
