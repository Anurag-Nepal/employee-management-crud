package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDto requestDto;
    private EmployeeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("75000.00"));

        requestDto = new EmployeeRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setEmail("john.doe@example.com");
        requestDto.setDepartment("IT");
        requestDto.setSalary(new BigDecimal("75000.00"));

        responseDto = new EmployeeResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setEmail("john.doe@example.com");
        responseDto.setDepartment("IT");
        responseDto.setSalary(new BigDecimal("75000.00"));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.getEmployeeById(1L)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        
        verify(employeeRepository, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.getEmployeeById(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        
        verify(employeeRepository, times(1)).getEmployeeById(1L);
    }
}
