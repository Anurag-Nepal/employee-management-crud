package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private BigDecimal salary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
