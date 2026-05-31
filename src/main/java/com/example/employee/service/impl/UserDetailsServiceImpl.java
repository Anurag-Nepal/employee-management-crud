package com.example.employee.service.impl;

import com.example.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.employee.entity.Employee employee = employeeRepository.getEmployeeByEmail(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found with email: " + username);
        }
        return employee;
    }
}
