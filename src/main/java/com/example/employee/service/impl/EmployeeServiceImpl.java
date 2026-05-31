package com.example.employee.service.impl;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.dto.EmployeeUpdateRequestDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DatabaseException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        log.info("Creating new employee with email: {}", requestDto.getEmail());
        try {
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
            Long id = employeeRepository.createEmployee(
                    requestDto.getFirstName(),
                    requestDto.getLastName(),
                    requestDto.getEmail(),
                    encodedPassword,
                    requestDto.getPhone(),
                    requestDto.getDepartment(),
                    requestDto.getSalary()
            );
            
            Employee createdEmployee = employeeRepository.getEmployeeById(id);
            return employeeMapper.toDto(createdEmployee);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating employee", e);
            throw new DatabaseException("Failed to create employee. Email might already exist or data constraint violated.");
        }
    }

    @Override
    @Transactional
    @Cacheable(value = "employees", key = "#id")
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public List<EmployeeResponseDto> getAllEmployees(int page, int size) {
        log.info("Fetching all employees, page: {}, size: {}", page, size);
        int offset = page * size;
        List<Employee> employees = employeeRepository.getAllEmployees(size, offset);
        return employeeMapper.toDtoList(employees);
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", key = "#id")
    public EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateRequestDto requestDto) {
        log.info("Updating employee with ID: {}", id);
        
        // Verify existence
        Employee existing = employeeRepository.getEmployeeById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        try {
            Integer rowsAffected = employeeRepository.updateEmployee(
                    id,
                    requestDto.getFirstName(),
                    requestDto.getLastName(),
                    requestDto.getEmail(),
                    requestDto.getPhone(),
                    requestDto.getDepartment(),
                    existing.getSalary()
            );

            if (rowsAffected == null || rowsAffected == 0) {
                throw new DatabaseException("Failed to update employee.");
            }

            Employee updatedEmployee = employeeRepository.getEmployeeById(id);
            return employeeMapper.toDto(updatedEmployee);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating employee with ID: " + id, e);
            throw new DatabaseException("Failed to update employee. Email might already exist or data constraint violated.");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        
        Employee existing = employeeRepository.getEmployeeById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        try {
            Integer rowsAffected = employeeRepository.deleteEmployee(id);
            if (rowsAffected == null || rowsAffected == 0) {
                throw new DatabaseException("Failed to delete employee.");
            }
        } catch (Exception e) {
            log.error("Error deleting employee with ID: " + id, e);
            throw new DatabaseException("Failed to delete employee.");
        }
    }
}
