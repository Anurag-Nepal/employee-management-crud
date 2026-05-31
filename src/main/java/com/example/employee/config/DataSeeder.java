package com.example.employee.config;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@admin.com";
        Employee admin = employeeRepository.getEmployeeByEmail(adminEmail);
        
        if (admin == null) {
            log.info("Seeding default admin user...");
            String encodedPassword = passwordEncoder.encode("admin123");
            try {
                employeeRepository.createEmployee(
                        "Admin",
                        "User",
                        adminEmail,
                        encodedPassword,
                        "1234567890",
                        "Administration",
                        new BigDecimal("100000.00")
                );
                log.info("Default admin user seeded successfully.");
            } catch (Exception e) {
                log.error("Failed to seed admin user", e);
            }
        } else {
            log.info("Admin user already exists. Skipping seeding.");
        }
    }
}