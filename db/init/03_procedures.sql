DELIMITER //

-- Procedure to create an employee
CREATE PROCEDURE sp_create_employee(
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_email VARCHAR(255),
    IN p_password VARCHAR(255),
    IN p_phone VARCHAR(20),
    IN p_department VARCHAR(100),
    IN p_salary DECIMAL(15,2),
    OUT p_id BIGINT
)
BEGIN
    INSERT INTO employees (first_name, last_name, email, password, phone, department, salary)
    VALUES (p_first_name, p_last_name, p_email, p_password, p_phone, p_department, p_salary);
    
    SET p_id = LAST_INSERT_ID();
END //

-- Procedure to get employee by ID
CREATE PROCEDURE sp_get_employee_by_id(
    IN p_id BIGINT
)
BEGIN
    SELECT id, first_name, last_name, email, password, phone, department, salary, is_deleted, created_at, updated_at 
    FROM employees 
    WHERE id = p_id AND is_deleted = 0;
END //

-- Procedure to get employee by email
CREATE PROCEDURE sp_get_employee_by_email(
    IN p_email VARCHAR(255)
)
BEGIN
    SELECT id, first_name, last_name, email, password, phone, department, salary, is_deleted, created_at, updated_at 
    FROM employees 
    WHERE email = p_email AND is_deleted = 0;
END //

-- Procedure to get all employees
CREATE PROCEDURE sp_get_all_employees(
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT id, first_name, last_name, email, password, phone, department, salary, is_deleted, created_at, updated_at 
    FROM employees 
    WHERE is_deleted = 0
    LIMIT p_limit OFFSET p_offset;
END //

-- Procedure to update an employee
CREATE PROCEDURE sp_update_employee(
    IN p_id BIGINT,
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_email VARCHAR(255),
    IN p_phone VARCHAR(20),
    IN p_department VARCHAR(100),
    IN p_salary DECIMAL(15,2),
    OUT p_rows_affected INT
)
BEGIN
    UPDATE employees 
    SET first_name = p_first_name, 
        last_name = p_last_name, 
        email = p_email, 
        phone = p_phone, 
        department = p_department, 
        salary = p_salary
    WHERE id = p_id AND is_deleted = 0;
    
    SET p_rows_affected = ROW_COUNT();
END //

-- Procedure to delete an employee
CREATE PROCEDURE sp_delete_employee(
    IN p_id BIGINT,
    OUT p_rows_affected INT
)
BEGIN
    UPDATE employees SET is_deleted = 1 WHERE id = p_id;
    
    SET p_rows_affected = ROW_COUNT();
END //

DELIMITER ;
