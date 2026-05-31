package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Procedure(procedureName = "sp_create_employee")
    Long createEmployee(
            @Param("p_first_name") String firstName,
            @Param("p_last_name") String lastName,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_phone") String phone,
            @Param("p_department") String department,
            @Param("p_salary") BigDecimal salary
    );

    @Procedure(procedureName = "sp_get_employee_by_id")
    Employee getEmployeeById(@Param("p_id") Long id);

    @Procedure(procedureName = "sp_get_employee_by_email")
    Employee getEmployeeByEmail(@Param("p_email") String email);

    @Procedure(procedureName = "sp_get_all_employees")
    List<Employee> getAllEmployees(@Param("p_limit") Integer limit, @Param("p_offset") Integer offset);

    @Procedure(procedureName = "sp_update_employee")
    Integer updateEmployee(
            @Param("p_id") Long id,
            @Param("p_first_name") String firstName,
            @Param("p_last_name") String lastName,
            @Param("p_email") String email,
            @Param("p_phone") String phone,
            @Param("p_department") String department,
            @Param("p_salary") BigDecimal salary
    );

    @Procedure(procedureName = "sp_delete_employee")
    Integer deleteEmployee(@Param("p_id") Long id);
}
