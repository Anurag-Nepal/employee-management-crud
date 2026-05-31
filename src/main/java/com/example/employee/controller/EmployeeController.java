package com.example.employee.controller;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.dto.EmployeeUpdateRequestDto;
import com.example.employee.response.ApiResponse;
import com.example.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Operations for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create a new employee", description = "Creates a new employee and returns the created record")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Database error (e.g., duplicate email)")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto responseDto = employeeService.createEmployee(requestDto);
        return new ResponseEntity<>(
                ApiResponse.<EmployeeResponseDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .timestamp(LocalDateTime.now())
                        .data(responseDto)
                        .build(), 
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves an employee's details by their ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeById(
            @Parameter(description = "ID of the employee") @PathVariable Long id) {
        EmployeeResponseDto responseDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .timestamp(LocalDateTime.now())
                        .data(responseDto)
                        .build()
        );
    }

    @Operation(summary = "Get all employees", description = "Retrieves a paginated list of all active employees")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees(page, size);
        return ResponseEntity.ok(
                ApiResponse.<List<EmployeeResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .timestamp(LocalDateTime.now())
                        .data(employees)
                        .build()
        );
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee's details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Database error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(
            @Parameter(description = "ID of the employee to update") @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequestDto requestDto) {
        EmployeeResponseDto responseDto = employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .timestamp(LocalDateTime.now())
                        .data(responseDto)
                        .build()
        );
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee by their ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Database error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteEmployee(
            @Parameter(description = "ID of the employee to delete") @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.<Map<String, String>>builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .timestamp(LocalDateTime.now())
                        .data(Map.of("message", "Employee deleted successfully"))
                        .build()
        );
    }
}
