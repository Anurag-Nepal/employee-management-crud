package com.example.employee.mapper;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee toEntity(EmployeeRequestDto dto);

    EmployeeResponseDto toDto(Employee entity);

    List<EmployeeResponseDto> toDtoList(List<Employee> entities);
}
