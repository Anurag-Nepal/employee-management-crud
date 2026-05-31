package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.dto.EmployeeUpdateRequestDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);
    EmployeeResponseDto getEmployeeById(Long id);
    List<EmployeeResponseDto> getAllEmployees(int page, int size);
    EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateRequestDto requestDto);
    void deleteEmployee(Long id);
}
